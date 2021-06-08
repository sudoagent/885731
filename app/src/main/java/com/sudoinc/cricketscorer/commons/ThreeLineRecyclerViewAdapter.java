package com.sudoinc.cricketscorer.commons;

        import android.content.Context;
        import android.text.Layout;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.recyclerview.widget.DefaultItemAnimator;
        import androidx.recyclerview.widget.RecyclerView;
        import com.sudoinc.cricketscorer.R;

        import java.util.ArrayList;

public class ThreeLineRecyclerViewAdapter extends RecyclerView.Adapter<ThreeLineRecyclerViewHolders>
{
    private ArrayList lineOne;
    private ArrayList lineTwo;
    private ArrayList lineThree;
    private Context context;
    private ItemClickListener mClickListener;
//    private Layout myLayout;

    public ThreeLineRecyclerViewAdapter(Context context, ArrayList lineOne, ArrayList lineTwo, ArrayList lineThree)
    {
        this.lineOne = lineOne;
        this.lineTwo = lineTwo;
        this.lineThree = lineThree;
        this.context = context;
//        this.myLayout = myLayout;
    }

    @Override
    public ThreeLineRecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.threeline_recyclerview_layout, null);
        ThreeLineRecyclerViewHolders rcv = new ThreeLineRecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ThreeLineRecyclerViewHolders holder, int position) {
        holder.lineOneDisplay.setText(lineOne.get(position).toString());
        holder.lineTwoDisplay.setText(lineTwo.get(position).toString());
        holder.lineThreeDisplay.setText(lineThree.get(position).toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.threeline_recyclerview_heading);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
            {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.lineOne.size();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
