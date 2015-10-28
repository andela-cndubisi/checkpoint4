package checkpoint.andela.com.productivitytracker;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import checkpoint.andela.com.productivitytracker.activities.MapActivity;
import checkpoint.andela.com.productivitytracker.db.ProductivityDBHelper;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.HistoryViewHolder>{

    List<LocationItem> item;

    public RVAdapter(List<LocationItem> item){
        this.item = item;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        v.setOnClickListener(onClickListener);
        HistoryViewHolder vh = new HistoryViewHolder(v);
        return vh;    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String time = ((TextView) v.findViewById(R.id.time_spent)).getText().toString();
            if (time.isEmpty()){
                TextView date = (TextView)v.findViewById(R.id.date_holder);
                String name = date.getText().toString();
                Intent mappedHistoryIntent = new Intent(v.getContext().getApplicationContext(), MapActivity.class);
                mappedHistoryIntent.putExtra("list", new ProductivityDBHelper(v.getContext()).getDateLocations(name));
                mappedHistoryIntent.putExtra("title", name);
                v.getContext().startActivity(mappedHistoryIntent);
            }
        }
    };

    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        LocationItem lItem = item.get(position);
        holder.date.setText(lItem.date);
        holder.positions.setText(lItem.position);
        if(lItem.timespent != 0){
            holder.timespent.setText(String.format("%d minutes",lItem.timespent));
            holder.positions.setText("");
        }
    }

    public int getItemCount() {
        return item.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder   {


        CardView cv;
        TextView date;
        TextView positions;
        TextView timespent;

        HistoryViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            date = (TextView)itemView.findViewById(R.id.date_holder);
            positions = (TextView)itemView.findViewById(R.id.no_of_position);
            timespent = (TextView)itemView.findViewById(R.id.time_spent);
        }
    }
}