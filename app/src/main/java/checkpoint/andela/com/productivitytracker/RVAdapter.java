package checkpoint.andela.com.productivitytracker;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.HistoryViewHolder>{


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HistoryViewHolder vh = new HistoryViewHolder(v);
        return vh;    }

    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.date.setText(item.get(position).date);
        holder.positions.setText(item.get(position).position);

    }
    public int getItemCount() {
        return item.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView date;
        TextView positions;

        HistoryViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            date = (TextView)itemView.findViewById(R.id.date_holder);
            positions = (TextView)itemView.findViewById(R.id.no_of_position);
        }

    }

    List<DateCount> item;

    public RVAdapter(List<DateCount> item){
        this.item = item;
    }


}