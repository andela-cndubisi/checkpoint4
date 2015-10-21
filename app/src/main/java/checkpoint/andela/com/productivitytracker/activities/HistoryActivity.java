package checkpoint.andela.com.productivitytracker.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import checkpoint.andela.com.productivitytracker.DateCount;
import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.RVAdapter;
import checkpoint.andela.com.productivitytracker.data.ProductivityContract;
import checkpoint.andela.com.productivitytracker.data.ProductivityDBHelper;


public class HistoryActivity extends AppCompatActivity {
    RecyclerView rv ;
    private List<DateCount> persons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        rv =  (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        insertDummyData();
        populateData();
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
        rv.setOnClickListener(onClickListener);
    }


    RecyclerView.OnClickListener onClickListener = new RecyclerView.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void populateData() {
        ProductivityDBHelper dbHelper = new ProductivityDBHelper(getApplicationContext());
        persons = dbHelper.getDateWithCount();
    }

    public void insertDummyData(){
        ProductivityDBHelper dbHelper =  new ProductivityDBHelper(getApplicationContext());
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        ContentValues vl = new ContentValues();
        vl.put(ProductivityContract.LocationEntry.COLUMN_DATE_TEXT,"2015-08-09");
        vl.put(ProductivityContract.LocationEntry.COLUMN_CITY_NAME,"Yaba, Lagos");
        vl.put(ProductivityContract.LocationEntry.COLUMN_LONGITUDE,3.380699);
        vl.put(ProductivityContract.LocationEntry.COLUMN_LATITUDE, 6.518296);
        long count = db1.insert(ProductivityContract.LocationEntry.TABLE_NAME, null, vl);
        db1.close();
    }




}
