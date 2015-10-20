package checkpoint.andela.com.productivitytracker;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by andela-cj on 19/10/2015.
 */
public class TimerService extends Service {
    int interval = 0;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    private long oldSystemTime = 0L;
    String time;
    long presentTime = 0L;
    int hr = 0;
    int secs = 0;
    int mins = 0;
    private Handler mHandler = new Handler();
    private final IBinder binder = new TimerBinder();
    Intent handlerIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        handlerIntent = new Intent("com.andela.checkpoint");
        starttime = SystemClock.uptimeMillis();
        sendNotification();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setUpHandler();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mHandler.removeCallbacks(updateTimer);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        mHandler.postDelayed(updateTimer, 0);
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
            interval = intent.getIntExtra("Interval", 0);
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
        return (value/(float) interval) *100;
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

    private void sendToActivity() {
        timeInMilliseconds =  SystemClock.uptimeMillis() - presentTime - starttime;
        secs = (int) (timeInMilliseconds / 1000);
        hr = mins / 60;
        mins = secs / 60;
        secs = secs % 60;
        time = String.format("%02d:%02d:%02d", hr, mins, secs);
        handlerIntent.putExtra("TIME", time);
        handlerIntent.putExtra("PERCENT", convertToPercentage(interval,mins));
        sendBroadcast(handlerIntent);
    }

    public void resumeTimer() {
        presentTime += SystemClock.uptimeMillis() - oldSystemTime;
        mHandler.postDelayed(updateTimer, 0);
    }

    public void pauseTimer() {
        oldSystemTime = SystemClock.uptimeMillis();
        mHandler.removeCallbacks(updateTimer);
    }


    public class TimerBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }
}
