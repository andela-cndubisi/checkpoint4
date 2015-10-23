package checkpoint.andela.com.productivitytracker.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.fragments.HistoryFragment;

/**
 * Created by andela-cj on 21/10/2015.
 */
public class LogActivity extends AppCompatActivity{
    private final String TITLE = "History";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        setContentView(R.layout.history_tab);
        HistoryPagerAdapter adapter = new HistoryPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    static class HistoryPagerAdapter extends FragmentStatePagerAdapter {

        private final String TAB1 = "By date";
        private final String TAB2 = "By by location";

        public HistoryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new HistoryFragment();
                case 1:
                    HistoryFragment history = new HistoryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TrackingActivity.Constants.ADDRESS, true);
                    history.setArguments(bundle);
                    return history;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return TAB1;
                case 1:
                    return TAB2;
            }
            return null;
        }
    }
}
