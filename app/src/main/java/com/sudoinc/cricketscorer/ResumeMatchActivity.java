package com.sudoinc.cricketscorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import com.sudoinc.cricketscorer.commons.RecyclerItemClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.sudoinc.cricketscorer.commons.ThreeLineRecyclerViewAdapter;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;

import java.util.ArrayList;
import java.util.Map;

public class ResumeMatchActivity extends AppCompatActivity {

    ArrayList lineInputOne = new ArrayList();
    ArrayList lineInputTwo = new ArrayList();
    ArrayList lineInputThree = new ArrayList();
    ArrayList hiddenID = new ArrayList();

    private Cursor queryCursor = null;
    private Cursor teamCursor;
    private Cursor scoreCursor;

    private final String resumeOrNew="Resume";
    private String noData;
    private String matchID;
    private String matchFormat;
    private String tournamentName;
    private String teamA;
    private String teamB;
    private String tossWinner;
    private String teamAID;
    private String teamBID;
    private String tossWinnerID;
    private String tossDecision;
    private String selectedOvers;
    private String matchDate;
    private String teamAScore;
    private String teamBScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SQLiteDatabase db = new SQLiteDBManager(this).getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_match);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshMatches);

        ImageView back_button_click = findViewById(R.id.matches_list_button_close);
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

        loadRecyclerView(db);
    }

    void loadRecyclerView(SQLiteDatabase db)
    {
        final RecyclerView recyclerView = findViewById(R.id.open_matches_list);

        queryCursor = db.rawQuery("select " + SQLiteDBManager.COL_T003_MATCH_ID
                    + ",(select " + SQLiteDBManager.COL_T999_TOURNAMENT_FORMAT + " from " + SQLiteDBManager.T999_TOURNAMENT_TYPE
                        + " where " + SQLiteDBManager.COL_T999_TOURNAMENT_TYPE_ID + " = a." + SQLiteDBManager.COL_T003_TOURNAMENT_TYPE_ID + "),"
                    + "(select " + SQLiteDBManager.COL_T004_TOURNAMENT_NAME + " from " + SQLiteDBManager.T004_TOURNAMENT
                        + " where " + SQLiteDBManager.COL_T004_TOURNAMENT_ID + " = a." + SQLiteDBManager.COL_T003_TOURNAMENT_ID + ")"
                + ",a." + SQLiteDBManager.COL_T003_TEAM_A_ID+ ",a." + SQLiteDBManager.COL_T003_TEAM_B_ID + ",a." + SQLiteDBManager.COL_T003_TOSS_WINNER_TEAM_ID
                + ",a." + SQLiteDBManager.COL_T003_TOSS_DECISION + ",a." + SQLiteDBManager.COL_T003_OVERS_MAX + ",a." + SQLiteDBManager.COL_T003_MATCH_DATE
                + " from " + SQLiteDBManager.T003_MATCHES + " a"
                + " where a." + SQLiteDBManager.COL_T003_MATCH_RESULT + " is null",null);
        System.out.println("ResumeMatchActivity : Input - " + queryCursor.getCount());
        if (queryCursor.getCount()>0)
        {
            noData="N";
            while (queryCursor.moveToNext()) {
                matchID=queryCursor.getString(0);
                matchFormat=queryCursor.getString(1);
                tournamentName=queryCursor.getString(2);

                // Getting Team A Name
                teamAID=queryCursor.getString(3);
                teamCursor=db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM
                        + " where " + SQLiteDBManager.COL_T001_TEAM_ID + "=" + teamAID,null);
                teamCursor.moveToNext();
                teamA=teamCursor.getString(0);
                teamCursor.close();

                // Getting Team B Name
                teamBID=queryCursor.getString(4);
                teamCursor=db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM
                        + " where " + SQLiteDBManager.COL_T001_TEAM_ID + "=" + teamBID,null);
                teamCursor.moveToNext();
                teamB=teamCursor.getString(0);
                teamCursor.close();

                // Getting Toss winner Team Name
                tossWinnerID=queryCursor.getString(5);
                teamCursor=db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM
                        + " where " + SQLiteDBManager.COL_T001_TEAM_ID + "=" + tossWinnerID,null);
                teamCursor.moveToNext();
                tossWinner=teamCursor.getString(0);
                teamCursor.close();

                tossDecision=queryCursor.getString(6);
                selectedOvers=queryCursor.getString(7);
                matchDate=queryCursor.getString(8);
                String TeamAScore;
                String TeamAOvers;
                String TeamBScore;
                String TeamBOvers;
                scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_OVER_NUM + "||'.'||" + SQLiteDBManager.COL_T005_BALL_NUM
                        + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS + " where " + SQLiteDBManager.COL_T005_BALL_ID
                            + "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID +") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + teamAID +")"
                        + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + teamAID,null);
                if (scoreCursor.getCount() == 1)
                {
                    scoreCursor.moveToNext();
                    TeamAOvers =  scoreCursor.getString(0);
                }
                else
                {
                    TeamAOvers =  "0.0";
                }
                scoreCursor.close();

                scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                        + SQLiteDBManager.COL_T005_WICKET_FLAG +"='Y' then 1 else 0 end)"
                        + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                        + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
                        + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + teamAID
                        + " group by " + SQLiteDBManager.COL_T005_MATCH_ID,null);
                if (scoreCursor.getCount() == 1)
                {
                    scoreCursor.moveToNext();
                    TeamAScore =  scoreCursor.getString(0) + "/" + scoreCursor.getString(1);
                }
                else
                {
                    TeamAScore = "0/0";
                }
                scoreCursor.close();

                scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_OVER_NUM + "||'.'||" + SQLiteDBManager.COL_T005_BALL_NUM
                        + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS + " where " + SQLiteDBManager.COL_T005_BALL_ID
                        + "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID +") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                        + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + teamBID +")"
                        + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + teamBID,null);
                if (scoreCursor.getCount() == 1)
                {
                    scoreCursor.moveToNext();
                    TeamBOvers =  scoreCursor.getString(0);
                }
                else
                {
                    TeamBOvers =  "0.0";
                }
                scoreCursor.close();

                scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                        + SQLiteDBManager.COL_T005_WICKET_FLAG +"='Y' then 1 else 0 end)"
                        + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                        + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
                        + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + teamBID
                        + " group by " + SQLiteDBManager.COL_T005_MATCH_ID,null);
                if (scoreCursor.getCount() == 1)
                {
                    scoreCursor.moveToNext();
                    TeamBScore =  scoreCursor.getString(0) + "/" + scoreCursor.getString(1);
                }
                else
                {
                    TeamBScore = "0/0";
                }
                scoreCursor.close();

                hiddenID.add(matchID);
                lineInputOne.add(teamA + " v/s " + teamB);
                lineInputTwo.add(teamA + " - " + TeamAScore + " (" + TeamAOvers + ") & " + teamB + " - " + TeamBScore + " (" + TeamBOvers + ")");
                lineInputThree.add("Match held on - " + matchDate);
            }
        }
        else
        {
            noData="Y";
            hiddenID.add("1");
            lineInputOne.add("No Data Available");
            lineInputTwo.add("No Data Available");
            lineInputThree.add("No Data Available");
        }

        if ( noData.equals("N") )
        {
            final ThreeLineRecyclerViewAdapter myAdapter = new ThreeLineRecyclerViewAdapter(ResumeMatchActivity.this, lineInputOne,lineInputTwo,lineInputThree);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(llm);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {

                            System.out.println("ResumeMatchActivity : MatchID - " + hiddenID.get(position).toString());

                            Intent ScoreSheetIntent = new Intent(ResumeMatchActivity.this, ScoreSheetActivity.class);
                            ScoreSheetIntent.putExtra("matchID",hiddenID.get(position).toString());
                            ScoreSheetIntent.putExtra("selectedMatchType",matchFormat);
                            ScoreSheetIntent.putExtra("selectedMyTeamID",teamAID);
                            ScoreSheetIntent.putExtra("selectedOppTeamID",teamBID);
                            ScoreSheetIntent.putExtra("tossWinnerID",tossWinnerID);
                            ScoreSheetIntent.putExtra("tossDecision",tossDecision);
                            ScoreSheetIntent.putExtra("selectedOvers",selectedOvers);
                            ScoreSheetIntent.putExtra("ResumeOrNew",resumeOrNew);
                            startActivity(ScoreSheetIntent);
                            finish();
                        }
                    })
            );
        }
    }
}
