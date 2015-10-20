package checkpoint.andela.com.productivitytracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.RVAdapter;


public class HistoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rv ;
    private List<History> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        rv =  (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        initializeData();
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.left);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);

    }
    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new History("Emma Wilson", "23 years old"));
        persons.add(new History("Lavery Maiss", "25 years old"));
        persons.add(new History("Lillie Watts", "35 years old"));
    }


    public class History {
        public String date;
        public String position;

        History(String date, String numberOfPosition) {
            this.date = date;
            this.position = numberOfPosition;
        }
    }

}
