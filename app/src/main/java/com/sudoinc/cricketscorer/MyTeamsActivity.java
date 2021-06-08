package com.sudoinc.cricketscorer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.sudoinc.cricketscorer.commons.Fragment.CreateTeamsDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sudoinc.cricketscorer.commons.RecyclerItemClickListener;
import com.sudoinc.cricketscorer.commons.RecyclerViewAdapter;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;

import java.util.ArrayList;

public class MyTeamsActivity extends AppCompatActivity {

    CreateTeamsDialogFragment viewDialog;
    private RecyclerView MyTeamsrecyclerView;
    private RecyclerView.Adapter MyTeamsAdapter;
    private RecyclerView.LayoutManager MyTeamsRecyclerLayoutManager;
    private SQLiteDBManager dbClass = new SQLiteDBManager(this);

//    ArrayList lineInputOne = new ArrayList<>(Arrays.asList("Team 1","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2"));
//    ArrayList lineInputTwo = new ArrayList<>(Arrays.asList("This is my line one","This is my line two","This is my line two","This is my line two","This is my line two","This is my line two","This is my line two","This is my line two","This is my line two","This is my line two","This is my line two","This is my line two"));
//    ArrayList hiddenID = new ArrayList<>(Arrays.asList("Team 1","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2","Team 2"));

    ArrayList lineInputOne = new ArrayList();
    ArrayList lineInputTwo = new ArrayList();
    ArrayList hiddenID = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

        final FloatingActionButton fab = findViewById(R.id.fab);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        final SQLiteDatabase db = new SQLiteDBManager(this).getWritableDatabase();
//        final Animation mAnim_in = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_show);
//        final Animation mAnim_out = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_hide);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewDialog = new CreateTeamsDialogFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                transaction.replace(android.R.id.content, viewDialog).addToBackStack(null).commit();
                showDialog();
            }
        });

        loadRecyclerView(db);

        ImageView back_button_click = (ImageView) findViewById(R.id.my_teams_button_close);
        back_button_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerView(db);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        recreate();
//    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    void showDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = CreateTeamsDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
        if (newFragment.getDialog() !=null )
        {
            newFragment.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            newFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    void loadRecyclerView(SQLiteDatabase db)
    {
        final RecyclerView recyclerView = findViewById(R.id.my_teams_list);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        Cursor lineOneCursor;
        Cursor lineTwoCursor;
        lineInputOne.clear();
        lineInputTwo.clear();
        hiddenID.clear();
        lineOneCursor = db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_NAME + "," + SQLiteDBManager.COL_T001_TEAM_ID + " from " + SQLiteDBManager.T001_TEAM ,null);
//        int i =0 ;
        while(lineOneCursor.moveToNext())
        {
//            System.out.println("onItemClick position 1 : " + lineOneCursor.getString(0));
//            System.out.println("onItemClick position 2 : " + lineOneCursor.getString(1));
            lineInputOne.add(lineOneCursor.getString(0));
            hiddenID.add(lineOneCursor.getString(1));
            lineTwoCursor = db.rawQuery("select * from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_TEAM_ID + "=" + lineOneCursor.getString(1) ,null);
            lineInputTwo.add("This team currently has " + lineTwoCursor.getCount() + " players registered");
//            i++;
        }

        final RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(MyTeamsActivity.this, lineInputOne,lineInputTwo);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        myAdapter.notifyDataSetChanged();;
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, llm.getOrientation()));
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String teamID = hiddenID.get(position).toString();
                        System.out.println("onItemClick position: " + teamID );
                        Intent PlayersActivity = new Intent(MyTeamsActivity.this, MyPlayersActivity.class);
                        PlayersActivity.putExtra("teamID",teamID);
                        startActivity(PlayersActivity);
                    }
                })
        );

        //        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy >0) {
//                    if (fab.isShown()) {
//                        fab.hide();
//                        fab.startAnimation(mAnim_in);
//                    }
//                }
//                else if (dy <0) {
//                    if (!fab.isShown()) {
//                        fab.show();
//                        fab.startAnimation(mAnim_out);
//                    }
//                }
//            }
//        });
    }
}
