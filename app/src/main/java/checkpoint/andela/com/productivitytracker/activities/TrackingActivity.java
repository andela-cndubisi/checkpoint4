package checkpoint.andela.com.productivitytracker.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import checkpoint.andela.com.productivitytracker.google.manager.GoogleLocationManager;
import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.circleprogress.CircleProgressView;


public class TrackingActivity extends Activity{
    private TextView durationSpent;
    private ImageButton pause;
    private TextView numberofLocations;
    private Button stopButton;
    private Button playButton;
    private CircleProgressView progressView;
    private GoogleLocationManager locationManager;
    int interval = 60;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    private long oldSystemTime = 0L;
    long presentTime = 0L;
    int hr = 0;
    int secs = 0;
    int mins = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_fragment);
        Typeface faceLight= Typeface.createFromAsset(getAssets(), "fonts/Gotham-Light.ttf");
        Typeface faceMedium= Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.ttf");
        locationManager = new GoogleLocationManager(this);
        durationSpent = (TextView)findViewById(R.id.duration);
        pause = (ImageButton) findViewById(R.id.pause_button);
        stopButton = (Button)findViewById(R.id.stop_button);
        playButton = (Button)findViewById(R.id.play_button);
        numberofLocations = (TextView) findViewById(R.id.number_of_locations);
        ((TextView) (findViewById(R.id.location_count_label))).setTypeface(faceMedium);
        durationSpent.setTypeface(faceLight);
        numberofLocations.setTypeface(faceLight);
        progressView = (CircleProgressView) findViewById(R.id.circleView);
        progressView.setValue(0);

        if(savedInstanceState !=null){
            starttime = savedInstanceState.getLong("Time");
        }
        setImageButtonOnClickListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("Time",starttime);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState !=null)
            starttime = savedInstanceState.getLong("Time");
        super.onRestoreInstanceState(savedInstanceState);
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
           oldSystemTime = SystemClock.uptimeMillis();
           handler.removeCallbacks(updateTimer);
       }

        if (v.getId() == R.id.stop_button) {

        } else if (v.getId() == R.id.play_button) {
            params.addRule(11, 0);
            params2.addRule(9, 0);

            findViewById(R.id.progress_circle).setVisibility(View.VISIBLE);
            findViewById(R.id.action).setVisibility(View.GONE);
            presentTime += SystemClock.uptimeMillis() - oldSystemTime;

            handler.postDelayed(updateTimer, 0);
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


    private float convertToPercentage(int interval, int value){
        return (value/(float) interval) *100;
    }

    @Override
    public void onStop() {
        locationManager.disconnect();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        starttime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimer, 0);
        locationManager.connect();
    }

    public Runnable updateTimer = new Runnable() {

        public void run() {
            timeInMilliseconds =  SystemClock.uptimeMillis() - presentTime - starttime;
            secs = (int) (timeInMilliseconds / 1000);
            hr = mins / 60;
            mins = secs / 60;
            secs = secs % 60;
            progressView.setValue(convertToPercentage(interval, mins));
            durationSpent.setText(String.format("%02d:%02d:%02d",0,mins, secs));
            handler.postDelayed(this, 0);
        }

    };

}
