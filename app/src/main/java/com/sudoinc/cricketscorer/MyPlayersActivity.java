package com.sudoinc.cricketscorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sudoinc.cricketscorer.commons.Fragment.CreatePlayersDialogFragment;
import com.sudoinc.cricketscorer.commons.RecyclerItemClickListener;
import com.sudoinc.cricketscorer.commons.RecyclerViewAdapter;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;

import java.util.ArrayList;

public class MyPlayersActivity extends AppCompatActivity {

    private int TeamID;
    private SQLiteDBManager dbClass = new SQLiteDBManager(this);
    private Cursor queryCursor = null;
    private int TeamIdTitle;
    ArrayList lineInputOne = new ArrayList();
    ArrayList lineInputTwo = new ArrayList();
    ArrayList hiddenID = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent myIntent = getIntent();
        final SQLiteDatabase db = new SQLiteDBManager(this).getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_players);

        final TextView myPlayersTitle = this.findViewById(R.id.my_players_appbar_text);
        final FloatingActionButton playersFab = findViewById(R.id.my_players_fab);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshPlayers);

        TeamID = Integer.parseInt(myIntent.getStringExtra("teamID"));
        queryCursor = db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM + " where " + SQLiteDBManager.COL_T001_TEAM_ID + "=" + TeamID,null);
        TeamIdTitle=TeamID;
        while(queryCursor.moveToNext())
        {
            myPlayersTitle.setText(queryCursor.getString(0));
        }
        queryCursor.close();

        playersFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        ImageView back_button_click = findViewById(R.id.my_players_button_close);
        back_button_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerView(TeamIdTitle,db);
                pullToRefresh.setRefreshing(false);
            }
        });

        loadRecyclerView(TeamIdTitle, db);
    }


    void loadRecyclerView(int TeamIDTitle, SQLiteDatabase db)
    {
        final RecyclerView recyclerView = findViewById(R.id.my_players_list);

        queryCursor = db.rawQuery("select a." + SQLiteDBManager.COL_T002_PLAYER_NAME + ", a." + SQLiteDBManager.COL_T002_PLAYER_ID + ","
                + " b." + SQLiteDBManager.COL_T997_PLAYER_TYPE_NAME
                + " from " + SQLiteDBManager.T002_PLAYERS + " a, " + SQLiteDBManager.T997_PLAYER_TYPE + " b"
                + " where a." + SQLiteDBManager.COL_T002_TEAM_ID + "=" + TeamIDTitle
                + " and a." + SQLiteDBManager.COL_T002_PLAYER_TYPE_ID + "=b." + SQLiteDBManager.COL_T997_PLAYER_TYPE_ID,null);
        while(queryCursor.moveToNext())
        {
            lineInputOne.add(queryCursor.getString(0));
            lineInputTwo.add(queryCursor.getString(2));
            hiddenID.add(queryCursor.getString(1));
        }
        final RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(MyPlayersActivity.this, lineInputOne,lineInputTwo);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String teamID = hiddenID.get(position).toString();
                        System.out.println("onItemClick position: " + teamID );
                        Intent PlayerStats = new Intent(MyPlayersActivity.this, PlayerStatsActivity.class);
//                            PlayersActivity.putExtra("teamID",teamID);
                        startActivity(PlayerStats);
                    }
                })
        );
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
////        final SQLiteDatabase db = new SQLiteDBManager(this).getWritableDatabase();
//
//        super.onWindowFocusChanged(hasFocus);
//        recreate();
//    }

    void showDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = CreatePlayersDialogFragment.newInstance();
        Bundle bnd = new Bundle();
        bnd.clear();
        bnd.putInt("teamID",TeamID);
        newFragment.setArguments(bnd);
        newFragment.show(ft, "dialog");
        if (newFragment.getDialog() !=null )
        {
            newFragment.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            newFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
