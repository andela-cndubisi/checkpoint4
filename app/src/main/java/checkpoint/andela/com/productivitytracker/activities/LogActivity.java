package checkpoint.andela.com.productivitytracker.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.fragments.HistoryFragment;

/**
 * Created by andela-cj on 21/10/2015.
 */
public class LogActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_tab);
        HistoryPagerAdapter adapter = new HistoryPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    static class HistoryPagerAdapter extends FragmentStatePagerAdapter {

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
                    bundle.putBoolean("address", true);
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
                    return "History by date";
                case 1:
                    return "History by location";
            }
            return null;
        }
    }
}
