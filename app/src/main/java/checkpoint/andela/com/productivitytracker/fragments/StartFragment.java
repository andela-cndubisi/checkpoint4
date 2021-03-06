package checkpoint.andela.com.productivitytracker.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.TrackerService;
import checkpoint.andela.com.productivitytracker.activities.TrackingActivity;
import static checkpoint.andela.com.productivitytracker.activities.TrackingActivity.Constants.*;

/**
 * Created by andela-cj on 14/10/2015.
 */
public class StartFragment  extends Fragment implements SettingsFragment.iSettings {
    private ImageButton startButton;
    private Button setting;
    private int interval;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_fragment, container, false);
        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.gothom_Light));
        startButton = (ImageButton)v.findViewById(R.id.start_button);
        setting = (Button)v.findViewById(R.id.interval);
        ((TextView)v.findViewById(R.id.description)).setTypeface(face);
        setting.setTypeface(face);
        startButton.setOnClickListener(onClickListener);
        setting.setOnClickListener(onClickListener);
        return v;
    }

    public ImageButton getStartButton() {
        return startButton;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.start_button) {
                start();
            }else if (v.getId() == R.id.interval){
                setInterval();
            }
        }

    };

    @Override
    public void onStart() {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int currentInterval = sharedpreferences.getInt(INTERVAL, 5);
        updateSettings(String.format("%d",currentInterval));
        setInterval(currentInterval);
        if (TrackerService.isRunning){
            start();
        }

        super.onStart();
    }

    private void setInterval(){
        SettingsFragment settings = new SettingsFragment();
        settings.setIntervalDelegate(this);
        if (!settings.isVisible() )
            settings.show(getFragmentManager(), "Interval Dialog");
    }

    private void start() {
        Intent a = new Intent(getActivity(), TrackingActivity.class);
        a.putExtra(INTERVAL, interval);
        getActivity();
        startActivity(a);
    }

    @Override
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void updateSettings(String progress) {
        setting.setText((progress + " minutes"));
    }
}
