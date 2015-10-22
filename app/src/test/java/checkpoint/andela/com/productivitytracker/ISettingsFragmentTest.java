package checkpoint.andela.com.productivitytracker;

import android.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import checkpoint.andela.com.productivitytracker.activities.MainActivity;
import checkpoint.andela.com.productivitytracker.fragments.SettingsFragment;
import checkpoint.andela.com.productivitytracker.lib.SeekArc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by andela-cj on 14/10/2015.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ISettingsFragmentTest {
    private MainActivity mActivity;
    private SeekArc arc;

    @Before
    public void setUp() throws Exception {
        mActivity = Robolectric.buildActivity(MainActivity.class).create().start().get();
        FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
        SettingsFragment iSF= new SettingsFragment();
        ft.add(R.id.mainActivity, iSF).commit();
    }

    @Test
    public void testFragmentTransition() throws Exception {
        assertThat(mActivity.findViewById(R.id.intervalLayout)).isNotNull();
    }

    @Test
    public void testSeekBarIsNotNull() {
        assertThat(mActivity.findViewById(R.id.intervalLayout)).isNotNull();
        arc = (SeekArc) mActivity.findViewById(R.id.seekArc);
        assertThat(arc).isNotNull();
    }

    @Test
    public void testSeekArcAttributes() {
        arc = (SeekArc) mActivity.findViewById(R.id.seekArc);
        assertThat(arc).isNotNull();
        assertThat(arc.getStartAngle()).isEqualTo(0);
        assertThat(arc.getArcRotation()).isEqualTo(30);
        assertThat(arc.getSweepAngle()).isEqualTo(330);
        assertThat(arc.getArcWidth()).isEqualTo(2);
        assertThat(arc.getProgressWidth()).isEqualTo(4);
    }
}
