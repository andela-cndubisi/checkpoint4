package checkpoint.andela.com.productivitytracker.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.SeekArc;

/**
 * Created by andela-cj on 14/10/2015.
 */
public class IntervalSettingsFragment extends DialogFragment {
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    SeekArc seekBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.interval_setting_fragment, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        seekBar = (SeekArc) v.findViewById(R.id.seekArc);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_dialog);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setTitle(getResources().getString(R.string.record_every));
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                            dismiss();
                        return true;
                    }
                });
        toolbar.inflateMenu(R.menu.menu_dialog);
        return v;
    }


}
