package checkpoint.andela.com.productivitytracker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import static org.assertj.core.api.Assertions.*;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {
    private MainActivity mActivity;

    @Before
    public void setUp() throws Exception {
        mActivity = Robolectric.buildActivity(MainActivity.class).create().start().get();
    }

    @Test
    public void testStartFragmentNotNull(){
        StartFragment start = mActivity.getStartFragment();
        assertThat(start).isNotNull();
    }
}