package checkpoint.andela.com.productivitytracker.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import checkpoint.andela.com.productivitytracker.fragments.RecordsFragment;
import checkpoint.andela.com.productivitytracker.managers.ServiceManager;
import checkpoint.andela.com.productivitytracker.TrackerService;
import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.lib.circleprogress.CircleProgressView;

public class TrackingActivity extends AppCompatActivity implements ServiceManager.ServiceManagerDelegate{
    private ImageButton pause;
    private TextView numberofLocations;
    private TextView hours, minutes, seconds;
    RelativeLayout locationCounter;
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
        interval = getIntent().getIntExtra(Constants.INTERVAL, interval);
        setupUI();
        setImageButtonOnClickListener();
        setTextViewTypeFace();
        progressView.setSeekModeEnabled(false);
        progressView.setValue(0);
        serviceManager.setDelegate(this);

        if (savedInstanceState !=null){
            trackerIntent = savedInstanceState.getParcelable(Constants.PAUSED);
            hours.setText(savedInstanceState.getString(Constants.HOUR));
            minutes.setText(savedInstanceState.getString(Constants.MINUTES));
            seconds.setText(savedInstanceState.getString(Constants.SECONDS));
            if (savedInstanceState.getBoolean(Constants.PAUSED))
                pause.performClick();
        }
    }


    private void setupUI(){
        pause = (ImageButton) findViewById(R.id.pause_button);
        stopButton = (ImageButton)findViewById(R.id.stop_button);
        playButton = (ImageButton)findViewById(R.id.play_button);
        numberofLocations = (TextView) findViewById(R.id.number_of_locations);
        progressView = (CircleProgressView) findViewById(R.id.circleView);
        hours = (TextView) findViewById(R.id.hour);
        minutes = (TextView) findViewById(R.id.minute);
        seconds = (TextView) findViewById(R.id.seconds);
        locationCounter = (RelativeLayout) findViewById(R.id.location_counter);
        locationCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Location> records = trackerService.savedLocations();
                if (records.size() > 0) {
                    RecordsFragment recordsFragment = new RecordsFragment();
                    recordsFragment.setLocations(records);
                    recordsFragment.show(getFragmentManager(), "My Records Dialog");
                }else {
                    Toast.makeText(v.getContext(),"No Location to Display", Toast.LENGTH_LONG).show();
                }
            }
        });
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
        Typeface faceLight= Typeface.createFromAsset(getAssets(), getResources().getString(R.string.gothom_Light));
        Typeface faceMedium= Typeface.createFromAsset(getAssets(), getResources().getString(R.string.gothom_Medium));
        ((TextView) (findViewById(R.id.location_count_label))).setTypeface(faceMedium);
        hours.setTypeface(faceLight);
        minutes.setTypeface(faceLight);
        seconds.setTypeface(faceLight);

        numberofLocations.setTypeface(faceLight);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.INTENT, trackerIntent);
        outState.putString(Constants.HOUR, hours.getText().toString());
        outState.putString(Constants.MINUTES, minutes.getText().toString());
        outState.putString(Constants.SECONDS, seconds.getText().toString());
        outState.putBoolean(Constants.PAUSED, trackerService.isPaused());
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        trackerIntent = savedInstanceState.getParcelable(Constants.INTENT);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        bindService(trackerIntent, serviceManager.getServiceConnection(), Context.BIND_AUTO_CREATE);
        registerReceiver(serviceManager.getBroadCastReceiver(), new IntentFilter(Constants.SERVICE_KEY));
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
            trackerIntent.putExtra(Constants.INTERVAL, interval);
            TrackerService.isRunning = true;
            startService(trackerIntent);
        }
    }

    private void setImageButtonOnClickListener(){
        stopButton.setOnClickListener(controlListener);
        pause.setOnClickListener(controlListener);
        playButton.setOnClickListener(controlListener);
    }

    View.OnClickListener controlListener = new View.OnClickListener() {
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
        String time = intent.getStringExtra(Constants.TIME);
        float percent = intent.getFloatExtra(Constants.PERCENT, 0);
        String count = intent.getStringExtra(Constants.N0_LOCATION);
        String [] timer = time.split(":");
        hours.setText(timer[0]+":");
        minutes.setText(timer[1]+":");
        seconds.setText(timer[2]);
        progressView.setValue(percent);
        if (count!=null) numberofLocations.setText(count);
    }


    public void onServiceConnected(ComponentName name, IBinder service) {
        TrackerService.TimerBinder binder = (TrackerService.TimerBinder) service;
        trackerService = binder.getService();
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Cannot go back while tracking",Toast.LENGTH_LONG).show();
    }

    public static class Constants {
        public static final String TIME = "TIME";
        public static final String PERCENT = "PERCENT";
        public static final String INTERVAL = "INTERVAL";
        public static final String N0_LOCATION = "#LOCATION";
        public static final String SERVICE_KEY = "com.andela.checkpoint";
        public static final String ADDRESS = "ADDRESS";
        private static final String INTENT = "INTENT";
        private static final String PAUSED = "PAUSED";
        private static final String HOUR = "HOUR";
        private static final String MINUTES = "MINUTE";
        private static final String SECONDS = "SECOND";
    }
}
