package checkpoint.andela.com.productivitytracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by andela-cj on 14/10/2015.
 */
public class StartFragment  extends Fragment{
    private ImageButton startButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_fragment, container);
        startButton = (ImageButton)v.findViewById(R.id.playButton);
        startButton.setOnClickListener(onClickListener);
        return v;
    }

    public ImageButton getStartButton() {
        return startButton;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getActivity().getFragmentManager();
            IntervalSettingsFragment intervalSettingsFragment = new IntervalSettingsFragment();
            fm.beginTransaction().add(R.id.mainActivity, intervalSettingsFragment).commit();
        }
    };
}
