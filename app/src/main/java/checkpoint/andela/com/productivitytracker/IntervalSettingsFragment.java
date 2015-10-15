package checkpoint.andela.com.productivitytracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by andela-cj on 14/10/2015.
 */
public class IntervalSettingsFragment extends Fragment {

    SeekArc seekBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.interval_setting_fragment, null);
        seekBar = (SeekArc) v.findViewById(R.id.seekArc);
        return v;
    }

    public SeekArc getSeekBar(){
        return seekBar;
    }
}
