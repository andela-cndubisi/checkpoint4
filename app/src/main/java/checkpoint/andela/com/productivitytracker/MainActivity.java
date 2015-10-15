package checkpoint.andela.com.productivitytracker;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    private StartFragment startFragment;
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
