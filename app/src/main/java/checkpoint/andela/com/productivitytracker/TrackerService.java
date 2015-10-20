package checkpoint.andela.com.productivitytracker;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import checkpoint.andela.com.productivitytracker.data.ProductivityContract;
import checkpoint.andela.com.productivitytracker.data.ProductivityDBHelper;

/**
 * Created by andela-cj on 19/10/2015.
 */
public class TrackerService extends Service {
    private GoogleLocationManager locationManager;
    private int interval = 0;
    private long starttime = 0L;
    private long timeInMilliseconds = 0L;
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

    @Override
    public void onCreate() {
        super.onCreate();
        handlerIntent = new Intent("com.andela.checkpoint");
        starttime = SystemClock.uptimeMillis();
        locationManager = new GoogleLocationManager();
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
            interval = intent.getIntExtra("Interval", 0);
        locationManager.setInterval(interval);
        locationManager.connect();
        return super.onStartCommand(intent, flags, startId);
    }


    private void sendNotification() {

        String notificationService = Context.NOTIFICATION_SERVICE;
        RemoteViews notificationView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.clock)
                .setContentTitle("Tracking...").build();
        notification.contentView = notificationView;
        // notification.contentView.setTextViewText(R.id.timer_title, time);

//        Intent clickIntent = new Intent("com.andela.click");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, clickIntent, 0);
//        notificationView.setOnClickPendingIntent(R.id.button, pendingIntent);

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(2343232, notification);
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
        timeInMilliseconds =  SystemClock.uptimeMillis() - presentTime - starttime;
        secs = (int) (timeInMilliseconds / 1000);
        handlerIntent.putExtra("PERCENT", convertToPercentage(interval*60,secs));
        hr = mins / 60;
        mins = secs / 60;
        secs = secs % 60;
        time = String.format("%02d:%02d:%02d", hr, mins, secs);
        handlerIntent.putExtra("TIME", time);

        if (locationManager.didChange()){
            resetTimer();
            locationManager.saveLocation();
        }
        handlerIntent.putExtra("#location",String.format("%d",locationManager.getRecordedLocations()));
        sendBroadcast(handlerIntent);
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

    public class TimerBinder extends Binder {
        public TrackerService getService() {
            return TrackerService.this;
        }
    }

    public class GoogleLocationManager implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener {

        private Location currentLocation;
        private int recordedLocations = 0;
        private boolean once = false;
        boolean didChange = false;
        private GoogleApiClient mGoogleClient;
        private int interval = 0;

        public GoogleLocationManager() {
            mGoogleClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        @Override
        public void onConnected(Bundle bundle) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(getInterval());
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, locationRequest, this);
        }

        public void connect(){
            mGoogleClient.connect();
        }
        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onLocationChanged(Location location) {
            if (!isSetOnce()){
                currentLocation = location;
                once = true;
                return;
            }
            float diff = currentLocation.distanceTo(location);
             if (diff >= 20){
                 locationChanged();
                 currentLocation = location;
             }

        }

        private boolean isSetOnce() {
            return once;
        }

        public boolean locationChanged(){
            didChange = !didChange;
            return didChange;
        }

        public boolean didChange() {
            return didChange;
        }
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
        public int getRecordedLocations(){
            return recordedLocations;
        }

        public void disconnect(){
            mGoogleClient.disconnect();
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        private int getInterval(){
            return (interval/5) * 10000;
        }

        public void saveLocation() {
            SQLiteDatabase db = new ProductivityDBHelper(getApplicationContext())
                    .getWritableDatabase();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.US);
            Date date = new Date();
            String today = dateFormat.format(date).split(" ")[0];
            ContentValues values = ProductivityContract.LocationEntry.createContentFromLocation(
                    currentLocation.getLongitude()
                    , currentLocation.getLatitude()
                    , ""
                    , today);
            long rowId = db.insert(ProductivityContract.LocationEntry.TABLE_NAME, null, values);
            if (rowId !=-1)
                recordedLocations++;
            db.close();
        }
    }

}
