package checkpoint.andela.com.productivitytracker.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import checkpoint.andela.com.productivitytracker.TrackerService;
import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.circleprogress.CircleProgressView;


public class TrackingActivity extends AppCompatActivity{
    private TextView durationSpent;
    private ImageButton pause;
    private TextView numberofLocations;
    private ImageButton stopButton;
    private ImageButton playButton;
    private CircleProgressView progressView;
    private int interval = 0;
    private Intent trackerIntent;
    private TrackerService trackerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_fragment);
        interval = getIntent().getIntExtra("Interval", interval);
        Typeface faceLight= Typeface.createFromAsset(getAssets(), "fonts/Gotham-Light.ttf");
        Typeface faceMedium= Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.ttf");
        durationSpent = (TextView)findViewById(R.id.duration);
        pause = (ImageButton) findViewById(R.id.pause_button);
        stopButton = (ImageButton)findViewById(R.id.stop_button);
        playButton = (ImageButton)findViewById(R.id.play_button);
        numberofLocations = (TextView) findViewById(R.id.number_of_locations);
        ((TextView) (findViewById(R.id.location_count_label))).setTypeface(faceMedium);
        durationSpent.setTypeface(faceLight);
        numberofLocations.setTypeface(faceLight);
        progressView = (CircleProgressView) findViewById(R.id.circleView);
        progressView.setSeekModeEnabled(false);
        progressView.setValue(0);
        setImageButtonOnClickListener();
        if (savedInstanceState !=null){
            trackerIntent = savedInstanceState.getParcelable("intent");
            durationSpent.setText(savedInstanceState.getString("duration"));
            if (savedInstanceState.getBoolean("paused"))
                pause.performClick();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("intent",trackerIntent);
        outState.putString("duration", durationSpent.getText().toString());
        outState.putBoolean("paused", trackerService.isPaused());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        trackerIntent = savedInstanceState.getParcelable("intent");
        durationSpent.setText(savedInstanceState.getString("duration"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        bindService(trackerIntent, mConnection, Context.BIND_AUTO_CREATE);
        registerReceiver(mBroadCastReceiver, new IntentFilter("com.andela.checkpoint"));
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mBroadCastReceiver);
        unbindService(mConnection);
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (trackerIntent == null){
            trackerIntent = new Intent(this, TrackerService.class);
            bindService(trackerIntent, mConnection, Context.BIND_AUTO_CREATE);
            trackerIntent.putExtra("Interval", interval);
            TrackerService.isRunning = true;
            startService(trackerIntent);
        }
    }

    private void setImageButtonOnClickListener(){
        stopButton.setOnClickListener(onClickListener);
        pause.setOnClickListener(onClickListener);
        playButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playButton.getLayoutParams();
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) stopButton.getLayoutParams();

            if (v.getId() == R.id.pause_button){
               int mode = getResources().getConfiguration().orientation;

               findViewById(R.id.progress_circle).setVisibility(View.GONE);
               findViewById(R.id.action).setVisibility(View.VISIBLE);
               if (mode != 2) {
                   params.addRule(11, -1);
                   playButton.setLayoutParams(params);

                   params2.addRule(9, -1);
                   stopButton.setLayoutParams(params2);
               }
                if (trackerService!=null)
                     trackerService.pauseTimer();
            }

            if (v.getId() == R.id.stop_button) {
                stopService(trackerIntent);
                TrackerService.isRunning = false;
                startHistory();
            } else if (v.getId() == R.id.play_button) {
                params.addRule(11, 0);
                params2.addRule(9, 0);

                findViewById(R.id.progress_circle).setVisibility(View.VISIBLE);
                findViewById(R.id.action).setVisibility(View.GONE);
                trackerService.resumeTimer();
            }
        }
    };

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            //TODO  add nike's long hold on stop to stop, show progress window around pause button
            return true;
        }
    };

    public void startHistory(){
        Intent a = new Intent(this, HistoryActivity.class);
        startActivity(a);
        finish();
    }

    public BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showTime(intent);
        }
    };

    private void showTime(Intent intent){

        String time = intent.getStringExtra("TIME");
        float percent = intent.getFloatExtra("PERCENT",0);
        String count = intent.getStringExtra("#location");
        durationSpent.setText(time);
        progressView.setValueAnimated(percent);
        numberofLocations.setText(count);
    }

    private ServiceConnection mConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackerService.TimerBinder binder = (TrackerService.TimerBinder) service;
            trackerService = binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


}
