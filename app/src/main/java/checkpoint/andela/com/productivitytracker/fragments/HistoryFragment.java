package checkpoint.andela.com.productivitytracker.fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import checkpoint.andela.com.productivitytracker.DateCount;
import checkpoint.andela.com.productivitytracker.R;
import checkpoint.andela.com.productivitytracker.RVAdapter;
import checkpoint.andela.com.productivitytracker.data.ProductivityContract;
import checkpoint.andela.com.productivitytracker.data.ProductivityDBHelper;

/**
 * Created by tunde on 10/20/15.
 */
public class HistoryFragment extends Fragment {
    RecyclerView rv ;
    private List<DateCount> records = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_history, container, false);
        boolean address = false;
        Bundle bundle;
        rv =  (RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        insertDummyData();
        if (getArguments()!= null) {
            bundle = getArguments();
            address= bundle.getBoolean("address");
        }
        if (address) populateData(address);
        else  populateData(address);
        RVAdapter adapter = new RVAdapter(records);
        rv.setAdapter(adapter);
        rv.setOnClickListener(onClickListener);
        return view;
    }


    RecyclerView.OnClickListener onClickListener = new RecyclerView.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void populateData(boolean val) {
        ProductivityDBHelper dbHelper = new ProductivityDBHelper(getActivity());
        if (val){
            records = dbHelper.getSumLocationWithInterval();
        }else {
            records = dbHelper.getDateWithCount();
        }
    }

    public void insertDummyData(){
        ProductivityDBHelper dbHelper =  new ProductivityDBHelper(getActivity());
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        ContentValues vl = new ContentValues();
        vl.put(ProductivityContract.LocationEntry.COLUMN_DATE_TEXT,"2015-08-09");
        vl.put(ProductivityContract.LocationEntry.COLUMN_CITY_NAME,"Yaba, Lagos");
        vl.put(ProductivityContract.LocationEntry.COLUMN_LONGITUDE,3.380699);
        vl.put(ProductivityContract.LocationEntry.COLUMN_LATITUDE, 6.518296);
        vl.put(ProductivityContract.LocationEntry.COLUMN_INTERVAL, 300000);
        long count = db1.insert(ProductivityContract.LocationEntry.TABLE_NAME, null, vl);
        db1.close();
    }


}
