package checkpoint.andela.com.productivitytracker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.fragments.StartFragment;

public class MainActivity extends AppCompatActivity {

    StartFragment startFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(startFragment == null){
            startFragment = new StartFragment();
            getFragmentManager().beginTransaction().add(R.id.container, startFragment).commit();
        }
    }
    public StartFragment getStartFragment() {
        return startFragment;
    }

}
