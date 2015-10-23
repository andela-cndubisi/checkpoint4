package checkpoint.andela.com.productivitytracker.fragments;

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
import checkpoint.andela.com.productivitytracker.activities.TrackingActivity;
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
        if (getArguments()!= null) {
            bundle = getArguments();
            address= bundle.getBoolean(TrackingActivity.Constants.ADDRESS);
        }
        if (address) populateData(address);
        else  populateData(address);
        RVAdapter adapter = new RVAdapter(records);
        rv.setAdapter(adapter);
        return view;
    }

    private void populateData(boolean val) {
        ProductivityDBHelper dbHelper = new ProductivityDBHelper(getActivity());
        if (val){
            records = dbHelper.getSumLocationWithInterval();
        }else {
            records = dbHelper.getDateWithCount();
        }
    }

}
