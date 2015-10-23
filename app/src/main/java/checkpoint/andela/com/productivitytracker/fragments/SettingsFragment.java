package checkpoint.andela.com.productivitytracker.fragments;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;

import checkpoint.andela.com.productivitytracker.R;
import static checkpoint.andela.com.productivitytracker.activities.TrackingActivity.Constants.*;
import checkpoint.andela.com.productivitytracker.lib.SeekArc;

/**
 * Created by andela-cj on 14/10/2015.
 */
public class SettingsFragment extends DialogFragment {
    private Toolbar toolbar;
    private SeekArc seekBar;
    private TextView minutes;
    private iSettings intervalDelegate;

    private final int MAX = 60;
    private final int MIN = 5;
    private int progress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.gothom_Light));
        View v = inflater.inflate(R.layout.interval_setting_fragment, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        seekBar = (SeekArc) v.findViewById(R.id.seekArc);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_dialog);
        seekBar.setMax(MAX - MIN);
        minutes = (TextView) v.findViewById(R.id.default_value);

        minutes.setTypeface(face);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setTitle(getResources().getString(R.string.record_every));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    final int minute;
                    minute = NumberFormat.getInstance().parse(minutes.getText().toString()).intValue();
                    updateSharedPreferences(minute);
                    intervalDelegate.updateSettings(String.format("%d",minute));
                    intervalDelegate.setInterval(minute);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                dismiss();
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.menu_dialog);
        seekBar.setOnSeekArcChangeListener(seekArcChangeListener);

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        progress = sharedpreferences.getInt(INTERVAL, 5);
        seekBar.setProgress(progress - MIN);
        return v;
    }

    SeekArc.OnSeekArcChangeListener seekArcChangeListener = new SeekArc.OnSeekArcChangeListener() {
        @Override
        public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
            Log.i(getClass().getSimpleName(), String.format("progress: %d from user %b",progress, fromUser));
            minutes.setText(String.format("%d",progress+MIN));
        }
        public void onStartTrackingTouch(SeekArc seekArc) {}
        public void onStopTrackingTouch(SeekArc seekArc) {}
    };

    public void setIntervalDelegate(iSettings intervalDelegate){
        this.intervalDelegate = intervalDelegate;
    }
    public void updateSharedPreferences(int interval){
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(INTERVAL, interval);
        editor.commit();
    }

    public interface iSettings {
        void updateSettings(String progress);
        void setInterval (int interval);
    }
}
