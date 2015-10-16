package checkpoint.andela.com.productivitytracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.circleprogress.CircleProgressView;


public class TrackingFragment extends Fragment {
    private TextView durationSpent;
    private ImageButton pause;
    private TextView numberofLocations;
    private ImageButton stopButton;
    private ImageButton playButton;
    private CircleProgressView progressView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tracking_fragment, container, false);
        durationSpent = (TextView)v.findViewById(R.id.duration);
        pause = (ImageButton) v.findViewById(R.id.pause_button);
        stopButton = (ImageButton)v.findViewById(R.id.stop_button);
        playButton = (ImageButton)v.findViewById(R.id.play_button);
        numberofLocations = (TextView) v.findViewById(R.id.number_of_locations);
        progressView = (CircleProgressView) v.findViewById(R.id.circleView);
        setImageButtonOnClickListener();
        return v;
    }


    private void setImageButtonOnClickListener(){
        stopButton.setOnClickListener(onClickListener);
        pause.setOnClickListener(onClickListener);
        playButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().findViewById(R.id.progress_circle).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.pause_play_layout).setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)playButton.getLayoutParams();
            params.addRule(11,-1);
            playButton.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)stopButton.getLayoutParams();
            params2.addRule(9,-1);
            stopButton.setLayoutParams(params2);
            if (v.getId() == R.id.stop_button){

            }else if(v.getId() == R.id.play_button){

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

    final int newLeftMargin = 20;

}
