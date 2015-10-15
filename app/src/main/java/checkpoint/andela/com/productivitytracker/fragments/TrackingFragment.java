package checkpoint.andela.com.productivitytracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import checkpoint.andela.com.productivitytracker.R;


public class TrackingFragment extends Fragment {
    private TextView durationSpent;
    private ImageButton pause;
    private TextView numberofLocations;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tracking_fragment, container, false);
        durationSpent = (TextView)v.findViewById(R.id.duration);
        pause = (ImageButton) v.findViewById(R.id.pause);
        numberofLocations = (TextView) v.findViewById(R.id.number_of_locations);
        return v;
    }
}
