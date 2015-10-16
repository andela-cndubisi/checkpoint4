package checkpoint.andela.com.productivitytracker;

import android.widget.ImageButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import static org.assertj.core.api.Assertions.*;

import checkpoint.andela.com.productivitytracker.fragments.StartFragment;

/**
 * Created by andela-cj on 14/10/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class StartFragmentTest {
    private MainActivity mActivity;
    private StartFragment startFragment;

    @Before
    public void setUp() throws Exception {
        mActivity = Robolectric.buildActivity(MainActivity.class).create().start().get();
        startFragment = mActivity.getStartFragment();
    }

    @Test
    public void testStartFragmentNotNull() throws Exception {
        assertThat(startFragment).isNotNull();
    }

    @Test
    public void testImageButtonIsNotNull() throws Exception {
        ImageButton startButton = startFragment.getStartButton();
        assertThat(startButton).isNotNull();
    }

    @Test
    public void testImageButtonDrawable() throws Exception {
        ImageButton startButton = startFragment.getStartButton();
        assertThat(startButton.getBackground()).isEqualTo(startFragment.getResources().getDrawable(R.drawable.circular_bounds));
        assertThat(startButton.getDrawable())
                .isEqualTo(startButton.getResources()
                        .getDrawable(R.drawable.play));
    }

}
