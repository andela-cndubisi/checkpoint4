package checkpoint.andela.com.productivitytracker;

import android.widget.ImageButton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import checkpoint.andela.com.productivitytracker.activities.TrackingActivity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by andela-cj on 15/10/2015.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TrackingActivityTest {
    private TrackingActivity tActivity;
    @Before
    public void setUp() throws Exception {
        tActivity = Robolectric.buildActivity(TrackingActivity.class).create().start().get();
    }

    @Test
    public void testActionFragmentNotNull() throws Exception {
        assertThat(tActivity).isNotNull();
    }


    @Test
    public void testButtonAreNotNull(){
        ImageButton pause = (ImageButton)tActivity.findViewById(R.id.pause_button);
        ImageButton start = (ImageButton)tActivity.findViewById(R.id.play_button);
        ImageButton stop = (ImageButton)tActivity.findViewById(R.id.stop_button);
        assertThat(pause).isNotNull();
        assertThat(start).isNotNull();
        assertThat(stop).isNotNull();
    }

    @Test
    public void testButtonsHaveClickListeners(){
        ImageButton start = (ImageButton)tActivity.findViewById(R.id.play_button);
        ImageButton stop = (ImageButton)tActivity.findViewById(R.id.stop_button);
        ImageButton pause = (ImageButton)tActivity.findViewById(R.id.pause_button);
        assertThat(pause.hasOnClickListeners()).isTrue();
        assertThat(start.hasOnClickListeners()).isTrue();
        assertThat(stop.hasOnClickListeners()).isTrue();
    }
}
