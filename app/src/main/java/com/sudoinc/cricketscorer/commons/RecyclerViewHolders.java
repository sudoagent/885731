package com.sudoinc.cricketscorer.commons;

import android.view.View;
import android.widget.TextView;
import com.sudoinc.cricketscorer.R;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class RecyclerViewHolders extends RecyclerView.ViewHolder
{
    public TextView lineOneDisplay;
    public TextView lineTwoDisplay;

    public RecyclerViewHolders(View itemView) {
        super(itemView);

        lineOneDisplay = (TextView)itemView.findViewById(R.id.myteams_recyclerview_heading);
        lineTwoDisplay = (TextView)itemView.findViewById(R.id.myteams_recyclerview_subtitle);
    }
}
