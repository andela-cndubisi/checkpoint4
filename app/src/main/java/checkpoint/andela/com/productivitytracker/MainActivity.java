package checkpoint.andela.com.productivitytracker;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import checkpoint.andela.com.productivitytracker.fragments.StartFragment;

public class MainActivity extends FragmentActivity {

    StartFragment startFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (startFragment == null){
            startFragment = new StartFragment();
            getFragmentManager().beginTransaction().add(R.id.container, startFragment).commit();
        }
    }

    public StartFragment getStartFragment() {
        return startFragment;
    }
}
