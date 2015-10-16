package checkpoint.andela.com.productivitytracker;

import android.app.FragmentTransaction;
import android.widget.ImageButton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import checkpoint.andela.com.productivitytracker.fragments.TrackingFragment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by andela-cj on 15/10/2015.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TrackingFragmentTest {
    private MainActivity mActivity;
    private TrackingFragment trackingFragment;
    @Before
    public void setUp() throws Exception {
        mActivity = Robolectric.buildActivity(MainActivity.class).create().start().get();
        ImageButton start = (ImageButton) mActivity.findViewById(R.id.start_button);
        start.performClick();
        trackingFragment =(TrackingFragment) mActivity.getFragmentManager().findFragmentById(R.id.container);
    }

    @Test
    public void testActionFragmentNotNull() throws Exception {
        assertThat(trackingFragment).isNotNull();
    }

    @Test
    public void testFragmentIsInLayout() {
        assertThat(trackingFragment.isAdded()).isTrue();}

    @Test
    public void testButtonAreNotNull(){
        ImageButton pause = (ImageButton)mActivity.findViewById(R.id.pause_button);
        ImageButton start = (ImageButton)mActivity.findViewById(R.id.play_button);
        ImageButton stop = (ImageButton)mActivity.findViewById(R.id.stop_button);
        assertThat(pause).isNotNull();
        assertThat(start).isNotNull();
        assertThat(stop).isNotNull();
    }

    @Test
    public void testButtonsHaveClickListeners(){
        ImageButton start = (ImageButton)mActivity.findViewById(R.id.play_button);
        ImageButton stop = (ImageButton)mActivity.findViewById(R.id.stop_button);
        ImageButton pause = (ImageButton)mActivity.findViewById(R.id.pause_button);
        assertThat(pause.hasOnClickListeners()).isTrue();
        assertThat(start.hasOnClickListeners()).isTrue();
        assertThat(stop.hasOnClickListeners()).isTrue();
    }
}
