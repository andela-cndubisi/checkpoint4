package checkpoint.andela.com.productivitytracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_history){
            Intent history = new Intent(this, LogActivity.class);
            startActivity(history);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
