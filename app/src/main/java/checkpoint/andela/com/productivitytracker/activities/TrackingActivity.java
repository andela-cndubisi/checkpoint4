package checkpoint.andela.com.productivitytracker.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import checkpoint.andela.com.productivitytracker.managers.ServiceManager;
import checkpoint.andela.com.productivitytracker.TrackerService;
import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.lib.circleprogress.CircleProgressView;

public class TrackingActivity extends AppCompatActivity implements ServiceManager.ServiceManagerDelegate{
    private TextView durationSpent;
    private ImageButton pause;
    private TextView numberofLocations;
    private ImageButton stopButton;
    private ImageButton playButton;
    private CircleProgressView progressView;
    private int interval = 0;
    private Intent trackerIntent;
    private TrackerService trackerService;
    ServiceManager serviceManager = new ServiceManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_fragment);
        interval = getIntent().getIntExtra("Interval", interval);
        setupUI();
        setImageButtonOnClickListener();
        setTextViewTypeFace();
        progressView.setSeekModeEnabled(false);
        progressView.setValue(0);
        serviceManager.setDelegate(this);
        if (savedInstanceState !=null){
            trackerIntent = savedInstanceState.getParcelable("intent");
            durationSpent.setText(savedInstanceState.getString("duration"));
            if (savedInstanceState.getBoolean("paused"))
                pause.performClick();
        }
    }

    private void setupUI(){
        durationSpent = (TextView)findViewById(R.id.duration);
        pause = (ImageButton) findViewById(R.id.pause_button);
        stopButton = (ImageButton)findViewById(R.id.stop_button);
        playButton = (ImageButton)findViewById(R.id.play_button);
        numberofLocations = (TextView) findViewById(R.id.number_of_locations);
        progressView = (CircleProgressView) findViewById(R.id.circleView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_history) {
            startHistory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setTextViewTypeFace(){
        Typeface faceLight= Typeface.createFromAsset(getAssets(), "fonts/Gotham-Light.ttf");
        Typeface faceMedium= Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.ttf");
        ((TextView) (findViewById(R.id.location_count_label))).setTypeface(faceMedium);
        durationSpent.setTypeface(faceLight);
        numberofLocations.setTypeface(faceLight);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable("intent",trackerIntent);
//        outState.putString("duration", durationSpent.getText().toString());
//        outState.putBoolean("paused", trackerService.isPaused());
//        super.onSaveInstanceState(outState);
//    }
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        trackerIntent = savedInstanceState.getParcelable("intent");
//        durationSpent.setText(savedInstanceState.getString("duration"));
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    protected void onResume() {
        bindService(trackerIntent, serviceManager.getServiceConnection(), Context.BIND_AUTO_CREATE);
        registerReceiver(serviceManager.getBroadCastReceiver(), new IntentFilter("com.andela.checkpoint"));
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(serviceManager.getBroadCastReceiver());
        unbindService(serviceManager.getServiceConnection());
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (trackerIntent == null){
            trackerIntent = new Intent(this, TrackerService.class);
            bindService(trackerIntent, serviceManager.getServiceConnection(), Context.BIND_AUTO_CREATE);
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
                trackerService.willStop();
                stopService(trackerIntent);
                TrackerService.isRunning = false;
                startHistory();
                finish();
            } else if (v.getId() == R.id.play_button) {
                params.addRule(11, 0);
                params2.addRule(9, 0);

                findViewById(R.id.progress_circle).setVisibility(View.VISIBLE);
                findViewById(R.id.action).setVisibility(View.GONE);
                trackerService.resumeTimer();
            }
        }
    };

    public void startHistory(){
        Intent a = new Intent(this, LogActivity.class);
        startActivity(a);
    }

    public void showTime(Intent intent){
        String time = intent.getStringExtra("TIME");
        float percent = intent.getFloatExtra("PERCENT", 0);
        String count = intent.getStringExtra("#location");
        durationSpent.setText(time);
        progressView.setValue(percent);
        if (count!=null) numberofLocations.setText(count);
    }


    public void onServiceConnected(ComponentName name, IBinder service) {
        TrackerService.TimerBinder binder = (TrackerService.TimerBinder) service;
        trackerService = binder.getService();
    }
}
