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
import checkpoint.andela.com.productivitytracker.activities.TrackingActivity;
import static checkpoint.andela.com.productivitytracker.fragments.SettingsFragment.INTERVAL;

/**
 * Created by andela-cj on 14/10/2015.
 */
public class StartFragment  extends Fragment implements SettingsFragment.iSettings {
    private ImageButton startButton;
    private Button interval;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_fragment, container, false);
        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Gotham-Light.ttf");

        startButton = (ImageButton)v.findViewById(R.id.start_button);
        interval = (Button)v.findViewById(R.id.interval);
        ((TextView)v.findViewById(R.id.description)).setTypeface(face);
        interval.setTypeface(face);
        startButton.setOnClickListener(onClickListener);
        interval.setOnClickListener(onClickListener);

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

        super.onStart();
    }

    private void setInterval(){
        SettingsFragment settings = new SettingsFragment();
        settings.setIntervalDelegate(this);
        if (!settings.isVisible() )
            settings.show(getFragmentManager(), "My Settings Dialog");

    }

    private void start() {
        Intent a = new Intent(getActivity(), TrackingActivity.class);
        startActivity(a);
    }


    public void updateSettings(String progress) {
        interval.setText((progress + " minutes"));
    }
}
