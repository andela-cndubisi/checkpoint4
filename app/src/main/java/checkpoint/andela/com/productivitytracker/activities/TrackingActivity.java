package checkpoint.andela.com.productivitytracker.activities;

import android.app.Activity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_fragment);
        locationManager = new GoogleLocationManager(this);
        durationSpent = (TextView)findViewById(R.id.duration);
        pause = (ImageButton) findViewById(R.id.pause_button);
        stopButton = (Button)findViewById(R.id.stop_button);
        playButton = (Button)findViewById(R.id.play_button);
        numberofLocations = (TextView) findViewById(R.id.number_of_locations);
        progressView = (CircleProgressView) findViewById(R.id.circleView);
        setImageButtonOnClickListener();
    }


    private void setImageButtonOnClickListener(){
        stopButton.setOnClickListener(onClickListener);
        pause.setOnClickListener(onClickListener);
        playButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int mode = getResources().getConfiguration().orientation;

                findViewById(R.id.progress_circle).setVisibility(View.GONE);
                findViewById(R.id.action).setVisibility(View.VISIBLE);
            if (mode != 2) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playButton.getLayoutParams();
                params.addRule(11, -1);
                playButton.setLayoutParams(params);

                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) stopButton.getLayoutParams();
                params2.addRule(9, -1);
                stopButton.setLayoutParams(params2);

            }
                if (v.getId() == R.id.stop_button) {

                } else if (v.getId() == R.id.play_button) {

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


    @Override
    public void onStop() {
        locationManager.disconnect();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        locationManager.connect();
    }
}
