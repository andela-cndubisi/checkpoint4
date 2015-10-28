package checkpoint.andela.com.productivitytracker;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.ArrayList;

import checkpoint.andela.com.productivitytracker.managers.GoogleLocationManager;
import static checkpoint.andela.com.productivitytracker.activities.TrackingActivity.Constants;
/**
 * Created by andela-cj on 19/10/2015.
 */
public class TrackerService extends Service{
    private GoogleLocationManager locationManager;
    private int interval = 0;
    private long starttime = 0L;
    private long timeInMilliseconds = 0L;
    private float percent;
    private long oldSystemTime = 0L;
    private String time;
    private long presentTime = 0L;
    private int hr = 0;
    private int secs = 0;
    private int mins = 0;
    private Handler mHandler = new Handler();
    private final IBinder binder = new TimerBinder();
    private Intent handlerIntent;
    public static boolean isRunning = false ;
    private boolean pause =false;
    private final int FOREGROUND_ID =  2343232;

    @Override
    public void onCreate() {
        super.onCreate();
        handlerIntent = new Intent(Constants.SERVICE_KEY);
        starttime = SystemClock.uptimeMillis();
        locationManager = new GoogleLocationManager(getApplicationContext());
        sendNotification();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (!pause)
            setUpHandler();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mHandler.removeCallbacks(updateTimer);
        if (pause) locationManager.disconnect();
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        if (!pause){
            mHandler.postDelayed(updateTimer, 0);
            locationManager.connect();
        }
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
            interval = intent.getIntExtra(Constants.INTERVAL, 0);
            locationManager.setInterval(interval);
            locationManager.connect();
        return super.onStartCommand(intent, flags, startId);
    }


    private void sendNotification() {

        RemoteViews notificationView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.clock)
                .setContentTitle(getResources().getString(R.string.tracking)).build();
        notification.contentView = notificationView;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(FOREGROUND_ID, notification);
    }

    private float convertToPercentage(int interval, int value){
        return (value/((float) interval)) *100;
    }

    private void setUpHandler() {
        mHandler.removeCallbacks(updateTimer);
        mHandler.postDelayed(updateTimer, 0);
    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            sendToActivity();
            mHandler.postDelayed(this, 0);
        }
    };

    public boolean isPaused(){
        return pause;
    }

    private void sendToActivity() {
        final int min2sec = 60;
        timeInMilliseconds =  SystemClock.uptimeMillis() - presentTime - starttime;
        secs = (int) (timeInMilliseconds / 1000);
        percent = convertToPercentage(interval*min2sec,secs);
        handlerIntent.putExtra(Constants.PERCENT, percent);
        hr = mins / min2sec;
        mins = secs / min2sec;
        secs = secs % min2sec;
        time = String.format("%02d:%02d:%02d", hr, mins, secs);
        handlerIntent.putExtra(Constants.TIME, time);
        verifyLocationState(locationManager.didChange());
        sendBroadcast(handlerIntent);
    }

    private void verifyLocationState(boolean didChange) {
        if (didChange){
            resetTimer();
            locationManager.reset();
        }
        if (percent >=100 && !locationManager.isRecorded()) {
            recordLocation();
            locationManager.updateWithChange(didChange);
        }

    }

    public ArrayList<Location> savedLocations(){
        return locationManager.getSavedLocations();
    }
    public void recordLocation(){
        locationManager.saveLocation();
        handlerIntent.putExtra(Constants.N0_LOCATION, String.format("%d", locationManager.getRecordsCount()));
    }

    public void resumeTimer() {
        presentTime += SystemClock.uptimeMillis() - oldSystemTime;
        mHandler.postDelayed(updateTimer, 0);
        locationManager.connect();
        pause = false;
    }

    public void pauseTimer() {
        pause = true;
        oldSystemTime = SystemClock.uptimeMillis();

        locationManager.disconnect();
        mHandler.removeCallbacks(updateTimer);
    }

    public void resetTimer() {
        starttime = SystemClock.uptimeMillis();
        presentTime = 0L;
    }

    public void willStop() {
        if (percent >= 100){
            locationManager.saveLocation();
        }
    }

    public class TimerBinder extends Binder {
        public TrackerService getService() {
            return TrackerService.this;
        }
    }
}
