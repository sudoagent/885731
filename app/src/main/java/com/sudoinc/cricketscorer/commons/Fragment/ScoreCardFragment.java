package com.sudoinc.cricketscorer.commons.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sudoinc.cricketscorer.R;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScoreCardFragment extends Fragment implements  GetInitialPlayersFragment.OnCompleteListener,
                                                            GetNewBowlerFragment.OnBowlerFragmentCompleteListener,
                                                            WicketBottomSheetFragment.OnWicketBottomSheetFragmentCompleteListener,
                                                            ExtrasFragment.OnExtrasFragmentCompleteListener
{
    private SQLiteDBManager dbClass;
    private SQLiteDatabase db;

    private List<String> wicketArray = new ArrayList<String>();
    private String[] extrasArray = {"Extras","Wide", "No ball", "Byes", "Leg byes"};

    private int legal_balls_delivered=0;
    private int legal_balls_maximum=0;
    private int runs_scored=0;
    private int target_runs=0;
    private int wickets_first_inning=0;
    private int wickets_second_inning=0;
    private int first_inning_over_display=0;
    private int second_inning_over_display=0;
    private int current_ball_number=0;
    private int number_of_dots;
    private int batsmen_run=0;
    private int batsmen_balls=0;
    private int batsmen_fours=0;
    private int batsmen_six=0;
    private int bowler_over;
    private int bowler_maiden;
    private int bowler_run;
    private int bowler_wickets;
    private int StrikeBatsmenDots;
    private int NonStrikeBatsmenDots;
    private int CurrentBowlerDots;
    private int CurrentBowlerBalls;
    private int CurrentBowlerMaidenCount;
    private int PrevBowlerDots;
    private int maiden_counter;
    private int StrikerBatsmenID=0;
    private int NonStrikerBatsmenID=0;
    private int CurrentBowlerID=0;
    private int currentInningNumber=0;

    private Float bowler_econ;
    private Float batsmen_strikerate;
    private Float scale;

    private String matchResult=null;
    private String dummy_column_one="";
    private String dummy_column_two="";
    private String dummy_column_three="";
    private String dummy_column_four="";
    private String dummy_column_five="";
    private String dummy_column_six="";
    private String matchID;
    private String TeamAID;
    private String TeamBID;
    private String MatchType;
    private String tossWinnerID;
    private String tossDecision;
    private String selectedOvers;
    private String ResumeOrNew;
    private String BattingTeamID=null;
    private String BowlingTeamID=null;

    private boolean end_innings_flag=false;
    private boolean pending_second_innings_selection=false;

    //Variables for Views.
    private TextView FirstBattingTeamName;
    private TextView FirstBattingTeamScore;
    private TextView SecondBattingTeamName;
    private TextView SecondBattingTeamScore;
    private TextView StrikeBatsmenName;
    private TextView StrikeBatsmenRuns;
    private TextView StrikeBatsmenBalls;
    private TextView StrikeBatsmenFours;
    private TextView StrikeBatsmenSixes;
    private TextView StrikeBatsmenStrikeRate;
    private TextView NonStrikeBatsmenName;
    private TextView NonStrikeBatsmenRuns;
    private TextView NonStrikeBatsmenBalls;
    private TextView NonStrikeBatsmenFours;
    private TextView NonStrikeBatsmenSixes;
    private TextView NonStrikeBatsmenStrikeRate;
    private TextView CurrentBowlerName;
    private TextView CurrentBowlerOvers;
    private TextView CurrentBowlerMaidens;
    private TextView CurrentBowlerRuns;
    private TextView CurrentBowlerWickets;
    private TextView CurrentBowlerEcon;
    private TextView PrevBowlerName;
    private TextView PrevBowlerOvers;
    private TextView PrevBowlerMaidens;
    private TextView PrevBowlerRuns;
    private TextView PrevBowlerWickets;
    private TextView PrevBowlerEcon;
    private TextView CurrRunrate;
    private TextView ReqRunrate;
    private TextView toWinTarget;
    private TextView RecentBalls;
    private TextView LastBatsmenName;
    private TextView LastBatsmenScore;
    private TextView FoW;

    private LinearLayout row_one;
    private LinearLayout row_two;
    private LinearLayout bottom_layout;

    private Iterator iterator;

    private MaterialButton dotBallButton;
    private MaterialButton oneRunButton;
    private MaterialButton twoRunButton;
    private MaterialButton threeRunButton;
    private MaterialButton fiveRunButton;
    private MaterialButton fourButton;
    private MaterialButton sixButton;
    private MaterialButton unboBallButton;
    private MaterialButton pending_inning_button;

    private Spinner extrasButton;
    private Spinner wicketButton;

    private Cursor dbCursor;
    private Cursor scoreCursor;

    public static ScoreCardFragment newInstance(String matchID, String selectedMyTeamID,String selectedOppTeamID,String selectedMatchType,
                                                String tossWinnerID,String tossDecision,String selectedOvers,String ResumeOrNew) {
        ScoreCardFragment f = new ScoreCardFragment();
        Bundle args = new Bundle();
        args.putString("matchID", matchID);
        args.putString("selectedMyTeamID", selectedMyTeamID);
        args.putString("selectedOppTeamID", selectedOppTeamID);
        args.putString("selectedMatchType", selectedMatchType);
        args.putString("tossWinnerID", tossWinnerID);
        args.putString("tossDecision", tossDecision);
        args.putString("selectedOvers", selectedOvers);
        args.putString("ResumeOrNew", ResumeOrNew);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        this.dbClass = new SQLiteDBManager(getContext());
        this.db = dbClass.getWritableDatabase();

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.matchID = getArguments().getString("matchID");
            this.TeamAID = getArguments().getString("selectedMyTeamID");
            this.TeamBID = getArguments().getString("selectedOppTeamID");
            this.MatchType = getArguments().getString("selectedMatchType");
            this.tossWinnerID = getArguments().getString("tossWinnerID");
            this.tossDecision = getArguments().getString("tossDecision");
            this.selectedOvers = getArguments().getString("selectedOvers");
            this.ResumeOrNew = getArguments().getString("ResumeOrNew");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_sheet, container, false);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        this.pending_second_innings_selection = false;

        // Main Score board
        this.FirstBattingTeamName = view.findViewById(R.id.FirstBattingTeam);
        this.FirstBattingTeamScore = view.findViewById(R.id.FirstBattingTeamScore);
        this.SecondBattingTeamName = view.findViewById(R.id.SecondBattingTeam);
        this.SecondBattingTeamScore = view.findViewById(R.id.SecondBattingTeamScore);
        this.CurrRunrate = view.findViewById(R.id.currRunRate);
        this.ReqRunrate = view.findViewById(R.id.reqRunrate);
        this.toWinTarget = view.findViewById(R.id.toWin);
        // Striker Batsmen
        this.StrikeBatsmenName = view.findViewById(R.id.striker_name);
        this.StrikeBatsmenRuns = view.findViewById(R.id.striker_runs);
        this.StrikeBatsmenBalls = view.findViewById(R.id.striker_balls);
        this.StrikeBatsmenFours = view.findViewById(R.id.striker_fours);
        this.StrikeBatsmenSixes = view.findViewById(R.id.striker_sixes);
        this.StrikeBatsmenStrikeRate = view.findViewById(R.id.striker_strikerate);
        // Non-Striker Batsmen
        this.NonStrikeBatsmenName = view.findViewById(R.id.non_striker_name);
        this.NonStrikeBatsmenRuns = view.findViewById(R.id.non_striker_runs);
        this.NonStrikeBatsmenBalls = view.findViewById(R.id.non_striker_balls);
        this.NonStrikeBatsmenFours = view.findViewById(R.id.non_striker_fours);
        this.NonStrikeBatsmenSixes = view.findViewById(R.id.non_striker_sixes);
        this.NonStrikeBatsmenStrikeRate = view.findViewById(R.id.non_striker_strikerate);
        // Current bowler
        this. CurrentBowlerName = view.findViewById(R.id.current_bowler_name);
        this.CurrentBowlerOvers = view.findViewById(R.id.current_bowler_overs);
        this.CurrentBowlerMaidens = view.findViewById(R.id.current_bowler_maidens);
        this.CurrentBowlerRuns = view.findViewById(R.id.current_bowler_runs);
        this.CurrentBowlerWickets = view.findViewById(R.id.current_bowler_wickets);
        this.CurrentBowlerEcon = view.findViewById(R.id.current_bowler_economy);
        this.CurrentBowlerBalls = 0;
        // Previous Spell Bowler
        this.PrevBowlerName = view.findViewById(R.id.previous_bowler_name);
        this.PrevBowlerOvers = view.findViewById(R.id.previous_bowler_overs);
        this.PrevBowlerMaidens = view.findViewById(R.id.previous_bowler_maidens);
        this.PrevBowlerRuns = view.findViewById(R.id.previous_bowler_runs);
        this.PrevBowlerWickets = view.findViewById(R.id.previous_bowler_wickets);
        this.PrevBowlerEcon = view.findViewById(R.id.previous_bowler_economy);
        // Recent Balls
        this.RecentBalls = view.findViewById(R.id.recent_deliveries);
        // Last Line of Scorecard
        this.LastBatsmenName = view.findViewById(R.id.last_batsmen_name);
        this.LastBatsmenScore = view.findViewById(R.id.last_batsmen_score);
        this.FoW = view.findViewById(R.id.fall_of_wicket);
        //Buttons initialization
        this.dotBallButton = view.findViewById(R.id.dotBall);
        this.oneRunButton = view.findViewById(R.id.oneRun);
        this.twoRunButton = view.findViewById(R.id.twoRun);
        this.threeRunButton = view.findViewById(R.id.threeRun);
        this.fiveRunButton = view.findViewById(R.id.fiveRuns);
        this.fourButton = view.findViewById(R.id.fourRuns);
        this.sixButton = view.findViewById(R.id.sixRuns);
        this.wicketButton = view.findViewById(R.id.wicket_spinner);
        this.extrasButton = view.findViewById(R.id.extras);
        this.unboBallButton = view.findViewById(R.id.undoBall);
        //Set the Layout IDs
        this.row_one = view.findViewById(R.id.buttons_line_one_layout);
        this.row_two = view.findViewById(R.id.buttons_line_two_layout);
        this.bottom_layout = view.findViewById(R.id.base_layout_score_fragment);

        this.pending_inning_button = (MaterialButton) getLayoutInflater().inflate(R.layout.materialbutton_template,null);
        this.scale = getContext().getResources().getDisplayMetrics().density;

        number_of_dots=0;
        CurrentBowlerMaidenCount=0;

        //Get Arguments passed to Intent here.
        legal_balls_maximum=Integer.parseInt(selectedOvers)*6;
        runs_scored=0;
        target_runs=0;
        wickets_first_inning=0;
        wickets_second_inning=0;
        //get current balls delivered validated from DB before assigning 0
        legal_balls_delivered=0;

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM
                + " where " + SQLiteDBManager.COL_T001_TEAM_ID + "=" + TeamAID,null);
        dbCursor.moveToNext();
        String TeamA=dbCursor.getString(0);
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM
                + " where " + SQLiteDBManager.COL_T001_TEAM_ID + "=" + TeamBID,null);
        dbCursor.moveToNext();
        String TeamB=dbCursor.getString(0);
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM
                + " where " + SQLiteDBManager.COL_T001_TEAM_ID + "=" + tossWinnerID,null);
        dbCursor.moveToNext();
        String tossWinner=dbCursor.getString(0);
        dbCursor.close();

        //TODO: Add condition to start Second Innings by setting a target. handle second innings and target set, intialize curr and req runrates properly.
        FirstBattingTeamScore.setText("0/0 (0.0)");
        SecondBattingTeamScore.setText("0/0 (0.0)");

        CurrRunrate.setText("N/A");
        ReqRunrate.setText("N/A");
        toWinTarget.setText("N/A");

        StrikeBatsmenName.setText("Batsmen one");
        StrikeBatsmenRuns.setText("0");
        StrikeBatsmenBalls.setText("0");
        StrikeBatsmenFours.setText("0");
        StrikeBatsmenSixes.setText("0");
        StrikeBatsmenStrikeRate.setText("0.00");
        StrikeBatsmenDots=0;

        NonStrikeBatsmenName.setText("Batsmen two");
        NonStrikeBatsmenRuns.setText("0");
        NonStrikeBatsmenBalls.setText("0");
        NonStrikeBatsmenFours.setText("0");
        NonStrikeBatsmenSixes.setText("0");
        NonStrikeBatsmenStrikeRate.setText("0.00");
        NonStrikeBatsmenDots=0;

        CurrentBowlerName.setText("Bowler one");
        CurrentBowlerOvers.setText("0.0");
        CurrentBowlerMaidens.setText("0");
        CurrentBowlerRuns.setText("0");
        CurrentBowlerWickets.setText("0");
        CurrentBowlerEcon.setText("0.00");
        CurrentBowlerDots=0;

        PrevBowlerName.setText("Bowler two");
        PrevBowlerOvers.setText("0.0");
        PrevBowlerMaidens.setText("0");
        PrevBowlerRuns.setText("0");
        PrevBowlerWickets.setText("0");
        PrevBowlerEcon.setText("0.00");
        PrevBowlerDots=0;

        LastBatsmenName.setText("N/A");
        LastBatsmenScore.setText("N/A");
        FoW.setText("N/A");

        //Set Button Width
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int ButtonWidth =((screenWidth)/7);
        int ButtonHeight = ButtonWidth - 6;

        row_one.setLayoutParams(new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,ButtonWidth+20));
        row_two.setLayoutParams(new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,ButtonWidth+20));
        // Setting Button width and Height
        dotBallButton.setLayoutParams(new LinearLayout.LayoutParams(ButtonWidth,ButtonHeight));
        oneRunButton.setLayoutParams(new LinearLayout.LayoutParams(ButtonWidth,ButtonHeight));
        twoRunButton.setLayoutParams(new LinearLayout.LayoutParams(ButtonWidth,ButtonHeight));
        threeRunButton.setLayoutParams(new LinearLayout.LayoutParams(ButtonWidth,ButtonHeight));
        fiveRunButton.setLayoutParams(new LinearLayout.LayoutParams(ButtonWidth,ButtonHeight));
        unboBallButton.setLayoutParams(new LinearLayout.LayoutParams(ButtonWidth,ButtonHeight));
        fourButton.setLayoutParams(new LinearLayout.LayoutParams(ButtonWidth+ButtonWidth/3,ButtonHeight));
        sixButton.setLayoutParams(new LinearLayout.LayoutParams(ButtonWidth+ButtonWidth/3,ButtonHeight));
        wicketButton.setLayoutParams(new LinearLayout.LayoutParams((5*ButtonWidth)/3,ButtonHeight-24));
        extrasButton.setLayoutParams(new LinearLayout.LayoutParams((5*ButtonWidth)/3,ButtonHeight-24));
        fourButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.four_six_button_color));
        sixButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.four_six_button_color));
        wicketButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.color_error));
        extrasButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.color_secondary_variant));
        unboBallButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.color_on_background));

        dotBallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else
                {
                    dotball();
                }
            }
        });
        oneRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    oneRun();
                }
            }
        });
        twoRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    twoRun();
                }
            }
        });
        threeRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    threeRun();
                }
            }
        });
        fourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    fourRun();
                }
            }
        });
        fiveRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    fiveRun();
                }
            }
        });
        sixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    sixRun();
                }
            }
        });

        unboBallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    undoLastBall();
                }
            }
        });

        ArrayAdapter extrasAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_selected, extrasArray){
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v;
                if ( position == 0 )
                {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setTextColor(Color.parseColor("#212121"));
                    tv.setTextSize(16);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else
                {
                    TextView tv = new TextView(getContext());
                    tv.setTextColor(Color.parseColor("#212121"));
                    tv.setTextSize(16);
                    v = super.getDropDownView(position, null, parent);
                }
                parent.setVerticalScrollBarEnabled(true);
                return v;
            }
            @Override
            public int getCount() {
                return(extrasArray.length);}
        };
        extrasAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        extrasButton.setAdapter(extrasAdapter);
        extrasButton.setSelection(0);

        extrasButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    if (id != 0) {
                        extrarun(parent.getSelectedItem().toString());
                        extrasButton.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        wicketArray.clear();
        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T996_WICKET_TYPE_NAME + " from " + SQLiteDBManager.T996_WICKET_TYPE
                + " where " + SQLiteDBManager.COL_T996_WICKET_TYPE_NAME + "<>'Not Out'",null);
        while(dbCursor.moveToNext())
        {
            wicketArray.add(dbCursor.getString(0));
        }
        wicketArray.add("Wickets");

        ArrayAdapter wicketsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_selected, wicketArray){
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v;
                if ( position == wicketArray.size()-1)
                {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setTextColor(Color.parseColor("#212121"));
                    tv.setTextSize(16);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else
                {
                    TextView tv = new TextView(getContext());
                    tv.setTextColor(Color.parseColor("#212121"));
                    tv.setTextSize(16);
                    v = super.getDropDownView(position, null, parent);
                }
                parent.setVerticalScrollBarEnabled(true);
                return v;
            }
            @Override
            public int getCount() {
                return(wicketArray.size());}
        };
        wicketsAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        wicketsAdapter.setNotifyOnChange(true);
        wicketButton.setAdapter(wicketsAdapter);
        wicketButton.setSelection(wicketArray.size()-1);

        wicketButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(bottom_layout.getChildCount()==1) {
                    Toast toast = Toast.makeText(getContext(),"End of Current Innings", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else {
                    if (id != wicketArray.size() - 1) {
                        final String selectedItem = parent.getSelectedItem().toString();
                        new MaterialAlertDialogBuilder(getContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar)
                                .setTitle("Wicket confirmation")
                                .setMessage("Are you sure the wicket was - " + selectedItem + " ?")
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        wicket_has_fallen(StrikerBatsmenID, NonStrikerBatsmenID, selectedItem);
                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .show();
                        wicketButton.setSelection(wicketArray.size() - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String dummy;

        if ("New".equals(ResumeOrNew))
        {
            //TODO: Verify that the condition is correct.
            if ( TeamA.equals(tossWinner) && tossDecision.equals("Bat first") )
            {
                setBattNBowlTeams(TeamAID,TeamBID,TeamA,TeamB);
            }
            else if ( TeamA.equals(tossWinner) && tossDecision.equals("Bowl first") )
            {
                setBattNBowlTeams(TeamBID,TeamAID,TeamB,TeamA);
                dummy=TeamAID;
                this.TeamAID=TeamBID;
                this.TeamBID=dummy;
            }
            else if ( TeamB.equals(tossWinner) && tossDecision.equals("Bat first") )
            {
                setBattNBowlTeams(TeamBID,TeamAID,TeamB,TeamA);
                dummy=TeamAID;
                this.TeamAID=TeamBID;
                this.TeamBID=dummy;
            }
            else if ( TeamB.equals(tossWinner) && tossDecision.equals("Bowl first") )
            {
                setBattNBowlTeams(TeamAID,TeamBID,TeamA,TeamB);
            }

            this.StrikeBatsmenRuns.setText("0");
            this.StrikeBatsmenBalls.setText("0");
            this.StrikeBatsmenFours.setText("0");
            this.StrikeBatsmenSixes.setText("0");
            this.StrikeBatsmenStrikeRate.setText("0.00");

            this.NonStrikeBatsmenRuns.setText("0");
            this.NonStrikeBatsmenBalls.setText("0");
            this.NonStrikeBatsmenFours.setText("0");
            this.NonStrikeBatsmenSixes.setText("0");
            this.NonStrikeBatsmenStrikeRate.setText("0.00");

            this.CurrentBowlerOvers.setText("0.0");
            this.CurrentBowlerMaidens.setText("0");
            this.CurrentBowlerRuns.setText("0");
            this.CurrentBowlerWickets.setText("0");
            this.CurrentBowlerEcon.setText("0.00");

            this.PrevBowlerName.setText("");
            this.PrevBowlerOvers.setText("");
            this.PrevBowlerMaidens.setText("");
            this.PrevBowlerRuns.setText("");
            this.PrevBowlerWickets.setText("");
            this.PrevBowlerEcon.setText("");

            currentInningNumber=1;
            getInitialPlayers(Integer.parseInt(BattingTeamID), Integer.parseInt(BowlingTeamID));
        }
        else if ("Resume".equals(ResumeOrNew)) {
            dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "," + SQLiteDBManager.COL_T005_BOWL_TEAM_ID
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID + "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from "
                    + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                    + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + ")",null);
            scoreCursor = db.rawQuery( "select " + SQLiteDBManager.COL_T003_INNING_NUMBER + " from " + SQLiteDBManager.T003_MATCHES
                                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID,null);
            scoreCursor.moveToNext();
            if ( dbCursor.getCount() == 1 )
            {
                dbCursor.moveToNext();
                // TODO: Verify that the conditions are correct.
                if ((TeamA.equals(tossWinner) && tossDecision.equals("Bat first")) || (TeamB.equals(tossWinner) && tossDecision.equals("Bowl first"))) {
                    if ((TeamAID.equals(dbCursor.getString(0)) && TeamBID.equals(dbCursor.getString(1)))  && scoreCursor.getInt(0)==1) {
                        setBattNBowlTeams(TeamAID, TeamBID, TeamA, TeamB);
                        currentInningNumber=1;
                        resume_first_innings();
//                    } else if (TeamBID.equals(dbCursor.getString(0)) && TeamAID.equals(dbCursor.getString(1))) {
                    } else {
                        setBattNBowlTeams(TeamBID, TeamAID, TeamB, TeamA);
                        dummy=TeamAID;
                        this.TeamAID=TeamBID;
                        this.TeamBID=dummy;
                        currentInningNumber=2;
                        if(Integer.parseInt(BattingTeamID)!=dbCursor.getInt(0))
                        {
                            //Initialize submit_button
                            pending_inning_button.setText("Click to Start next Innings");
                            pending_inning_button.setWidth((int) (312 * scale + 0.5f));
                            pending_inning_button.setHeight((int) (52 * scale + 0.5f));
                            pending_inning_button.setElevation(2);
                            pending_inning_button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_primary));
                            pending_inning_button.setTextColor(ContextCompat.getColor(getContext(),R.color.white_color));
                            pending_inning_button.setGravity(Gravity.CENTER);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(16,16,16,16);
                            pending_second_innings_selection=true;

                            bottom_layout.addView(pending_inning_button,params);
                            pending_inning_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AreYouReady();
                                }
                            });
                        }
                        else
                        {
                            System.out.println("ScoreCardFragment - Second Innings 1");
                            resume_second_innings();
                        }
                    }
                }
                else if ( (TeamB.equals(tossWinner) && tossDecision.equals("Bat first")) || (TeamA.equals(tossWinner) && tossDecision.equals("Bowl first"))) {
                    if ((TeamBID.equals(dbCursor.getString(0)) && TeamAID.equals(dbCursor.getString(1))) && scoreCursor.getInt(0)==1) {
                        setBattNBowlTeams(TeamBID, TeamAID, TeamB, TeamA);
                        currentInningNumber=1;
                        resume_first_innings();
//                    } else if (TeamAID.equals(dbCursor.getString(0)) && TeamBID.equals(dbCursor.getString(1))) {
                    } else {
                        setBattNBowlTeams(TeamAID, TeamBID, TeamA, TeamB);
                        dummy=TeamAID;
                        this.TeamAID=TeamBID;
                        this.TeamBID=dummy;
                        currentInningNumber=2;
                        if(Integer.parseInt(BowlingTeamID)!=dbCursor.getInt(0))
                        {
                            //Initialize submit_button
                            pending_inning_button.setText("Click to Start next Innings");
                            pending_inning_button.setWidth((int) (312 * scale + 0.5f));
                            pending_inning_button.setHeight((int) (52 * scale + 0.5f));
                            pending_inning_button.setElevation(2);
                            pending_inning_button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_primary));
                            pending_inning_button.setTextColor(ContextCompat.getColor(getContext(),R.color.white_color));
                            pending_inning_button.setGravity(Gravity.CENTER);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(16,16,16,16);
                            pending_second_innings_selection=true;

                            bottom_layout.addView(pending_inning_button,params);
                            pending_inning_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AreYouReady();
                                }
                            });
                        }
                        else
                        {
                            System.out.println("ScoreCardFragment - Second Innings 2");
                            resume_second_innings();
                        }
                    }
                }
            }
            else
            {
                if ( (TeamA.equals(tossWinner) && tossDecision.equals("Bat first")) || (TeamB.equals(tossWinner) && tossDecision.equals("Bowl first"))) {
                    setBattNBowlTeams(TeamAID,TeamBID,TeamA,TeamB);
                    getInitialPlayers(Integer.parseInt(BattingTeamID), Integer.parseInt(BowlingTeamID));
                }
                else if ( (TeamB.equals(tossWinner) && tossDecision.equals("Bat first")) || (TeamA.equals(tossWinner) && tossDecision.equals("Bowl first"))) {
                    setBattNBowlTeams(TeamBID,TeamAID,TeamB,TeamA);
                    getInitialPlayers(Integer.parseInt(BattingTeamID), Integer.parseInt(BowlingTeamID));
                }
            }
            dbCursor.close();
        }
    }


    public void dotball()
    {
        int temp_ball_counter=legal_balls_delivered+1;
        current_ball_number = temp_ball_counter % 6;

        number_of_dots+=1;
        if ( number_of_dots == 6 )
        {
            number_of_dots=0;
            maiden_counter=1;
        }
        else
        {
            maiden_counter=0;
        }
        if ( current_ball_number == 0 )
        {
            number_of_dots=0;
            // TODO: Get confirmation for Over Complete - No Undo Possible - return with Boolean and validate the output before proceeding.
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(0);
            if (currentInningNumber==1)
            {
                first_inning_over_display=first_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                second_inning_over_display=second_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            this.StrikeBatsmenDots=StrikeBatsmenDots+1;
            setRecents("| . ");
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
            else
            {
                end_innings_flag=false;
                switch_batsmen();
                getNextBowler(Integer.parseInt(this.BowlingTeamID));
            }
        }
        else
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(0);
            if (currentInningNumber==1)
            {
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            this.StrikeBatsmenDots=StrikeBatsmenDots+1;
            setRecents(". ");
            end_innings_flag=false;
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
        }
    }

    public void oneRun()
    {
        int temp_ball_counter=legal_balls_delivered+1;
        current_ball_number = temp_ball_counter % 6;

        if ( current_ball_number == 0 )
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(1);
            if (currentInningNumber==1)
            {
                first_inning_over_display=first_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                second_inning_over_display=second_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            setRecents("| 1 ");
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
            else
            {
                end_innings_flag=false;
                getNextBowler(Integer.parseInt(this.BowlingTeamID));
            }
        }
        else
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(1);
            switch_batsmen();
            if (currentInningNumber==1)
            {
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            setRecents("1 ");
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
        }
    }

    public void twoRun()
    {
        int temp_ball_counter=legal_balls_delivered+1;
        current_ball_number = temp_ball_counter % 6;

        if ( current_ball_number == 0 )
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(2);
            if (currentInningNumber==1)
            {
                first_inning_over_display=first_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                second_inning_over_display=second_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            setRecents("| 2 ");
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
            else
            {
                end_innings_flag=false;
                switch_batsmen();
                getNextBowler(Integer.parseInt(this.BowlingTeamID));
            }
        }
        else
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(2);
            if (currentInningNumber==1)
            {
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            setRecents("2 ");
            end_innings_flag=false;
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
        }
    }

    public void threeRun()
    {
        int temp_ball_counter=legal_balls_delivered+1;
        current_ball_number = temp_ball_counter % 6;

        if ( current_ball_number == 0 )
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(3);
            if (currentInningNumber==1)
            {
                first_inning_over_display=first_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                second_inning_over_display=second_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            setRecents("| 3 ");
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
            else
            {
                end_innings_flag=false;
                switch_batsmen();
                getNextBowler(Integer.parseInt(this.BowlingTeamID));
            }
        }
        else
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(3);
            switch_batsmen();
            if (currentInningNumber==1)
            {
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            setRecents("3 ");
            end_innings_flag=false;
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
        }
    }

    public void fourRun()
    {
        int temp_ball_counter=legal_balls_delivered+1;
        current_ball_number = temp_ball_counter % 6;

        if ( current_ball_number == 0 )
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(4);
            if (currentInningNumber==1)
            {
                first_inning_over_display=first_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                second_inning_over_display=second_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            this.StrikeBatsmenFours.setText(Integer.toString(Integer.parseInt(this.StrikeBatsmenFours.getText().toString()) + 1));
            setRecents("| 4 ");
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
            else
            {
                end_innings_flag=false;
                switch_batsmen();
                getNextBowler(Integer.parseInt(this.BowlingTeamID));
            }
        }
        else
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(4);
            if (currentInningNumber==1)
            {
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            this.StrikeBatsmenFours.setText(Integer.toString(Integer.parseInt(this.StrikeBatsmenFours.getText().toString()) + 1));
            setRecents("4 ");
            end_innings_flag=false;
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
        }
    }

    public void fiveRun()
    {
        int temp_ball_counter=legal_balls_delivered+1;
        current_ball_number = temp_ball_counter % 6;

        if ( current_ball_number == 0 )
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(5);
            if (currentInningNumber==1)
            {
                first_inning_over_display=first_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                second_inning_over_display=second_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            setRecents("| 5 ");
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
            else
            {
                end_innings_flag=false;
                switch_batsmen();
                getNextBowler(Integer.parseInt(this.BowlingTeamID));
            }
        }
        else
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(5);
            if (currentInningNumber==1)
            {
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            setRecents("5 ");
            end_innings_flag=false;
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
        }
    }

    public void sixRun()
    {
        int temp_ball_counter=legal_balls_delivered+1;
        current_ball_number = temp_ball_counter % 6;

        if ( current_ball_number == 0 )
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(6);
            if (currentInningNumber==1)
            {
                first_inning_over_display=first_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                second_inning_over_display=second_inning_over_display+1;
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            this.StrikeBatsmenSixes.setText(Integer.toString(Integer.parseInt(this.StrikeBatsmenSixes.getText().toString()) + 1));
            setRecents("| 6 ");
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
            else
            {
                end_innings_flag=false;
                switch_batsmen();
                getNextBowler(Integer.parseInt(this.BowlingTeamID));
            }
        }
        else
        {
            boolean endOfInnings;
            endOfInnings=add_a_legal_delivery(6);
            if (currentInningNumber==1)
            {
                String dummy=runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")";
                this.FirstBattingTeamScore.setText(dummy);
            }
            else if (currentInningNumber==2)
            {
                String dummy=runs_scored + "/" + wickets_second_inning + " (" + second_inning_over_display
                        + "." + current_ball_number + ")";
                this.SecondBattingTeamScore.setText(dummy);
            }
            this.StrikeBatsmenSixes.setText(Integer.toString(Integer.parseInt(this.StrikeBatsmenSixes.getText().toString()) + 1));
            setRecents("6 ");
            end_innings_flag=false;
            if (endOfInnings)
            {
                end_innings_flag=true;
                CheckEndOfInnings();
            }
        }
    }

    public void switch_batsmen()
    {
        int dummy_batsmen_id_one;
        int dummy_batsmen_id_two;
        int dummy_dotballs_one;
        int dummy_dotballs_two;
        // Assigning Striker Data to Dummy Variables
        dummy_column_one=this.StrikeBatsmenName.getText().toString();
        dummy_column_two=this.StrikeBatsmenRuns.getText().toString();
        dummy_column_three=this.StrikeBatsmenBalls.getText().toString();
        dummy_column_four=this.StrikeBatsmenFours.getText().toString();
        dummy_column_five=this.StrikeBatsmenSixes.getText().toString();
        dummy_column_six=this.StrikeBatsmenStrikeRate.getText().toString();
        dummy_batsmen_id_one=this.StrikerBatsmenID;
        dummy_batsmen_id_two=this.NonStrikerBatsmenID;
        dummy_dotballs_one=this.StrikeBatsmenDots;
        dummy_dotballs_two=this.NonStrikeBatsmenDots;

        // Switching NonStriker Data to Striker Fields
        this.StrikeBatsmenName.setText(this.NonStrikeBatsmenName.getText().toString());
        this.StrikeBatsmenRuns.setText(this.NonStrikeBatsmenRuns.getText().toString());
        this.StrikeBatsmenBalls.setText(this.NonStrikeBatsmenBalls.getText().toString());
        this.StrikeBatsmenFours.setText(this.NonStrikeBatsmenFours.getText().toString());
        this.StrikeBatsmenSixes.setText(this.NonStrikeBatsmenSixes.getText().toString());
        this.StrikeBatsmenStrikeRate.setText(this.NonStrikeBatsmenStrikeRate.getText().toString());

        // Assigning the data from dummy fields to Non Striker Fields
        this.NonStrikeBatsmenName.setText(dummy_column_one);
        this.NonStrikeBatsmenRuns.setText(dummy_column_two);
        this.NonStrikeBatsmenBalls.setText(dummy_column_three);
        this.NonStrikeBatsmenFours.setText(dummy_column_four);
        this.NonStrikeBatsmenSixes.setText(dummy_column_five);
        this.NonStrikeBatsmenStrikeRate.setText(dummy_column_six);

        this.StrikerBatsmenID=dummy_batsmen_id_two;
        this.NonStrikerBatsmenID=dummy_batsmen_id_one;
        this.StrikeBatsmenDots=dummy_dotballs_two;
        this.NonStrikeBatsmenDots=dummy_dotballs_one;
    }

    public void getNewBowlerAndSwitch()
    {
        this.PrevBowlerName.setText(this.CurrentBowlerName.getText().toString());
        this.PrevBowlerOvers.setText(this.CurrentBowlerOvers.getText().toString());
        this.PrevBowlerMaidens.setText(this.CurrentBowlerMaidens.getText().toString());
        this.PrevBowlerRuns.setText(this.CurrentBowlerRuns.getText().toString());
        this.PrevBowlerWickets.setText(this.CurrentBowlerWickets.getText().toString());
        this.PrevBowlerEcon.setText(this.CurrentBowlerEcon.getText().toString());

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME
                + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + CurrentBowlerID,null);
        dbCursor.moveToNext();
        this.CurrentBowlerName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T301_RUNS_CONCEEDED + ", " + SQLiteDBManager.COL_T301_BALLS_BOWLED
                + ", " + SQLiteDBManager.COL_T301_WICKETS
                + " from " + SQLiteDBManager.T301_PLAYER_BOWLER + " where " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID
                + " and " + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID,null);
        if ( dbCursor.getCount() != 0 )
        {
            dbCursor.moveToNext();
            int over;
            int balls;
            Double sr;
            Double Econ;
            Double divisor;
            Double divider;
            DecimalFormat df = new DecimalFormat("###.##");

            over=(int) Math.floor(Integer.parseInt(dbCursor.getString(1))/6);
            balls=Integer.parseInt(dbCursor.getString(1))%6;

            CurrentBowlerBalls=Integer.parseInt(dbCursor.getString(1));

            divisor=Double.parseDouble(dbCursor.getString(0));
            divider=Double.parseDouble(Integer.toString(CurrentBowlerBalls));

            if ( divider == 0 )
            {
                this.CurrentBowlerEcon.setText("0.00");
            }
            else
            {
                Econ = (divisor / divider)*6;
                this.CurrentBowlerEcon.setText(df.format(Econ));
            }

            this.CurrentBowlerOvers.setText(over + "." + balls);
            this.CurrentBowlerRuns.setText(dbCursor.getString(0));
            this.CurrentBowlerWickets.setText(dbCursor.getString(2));
            dbCursor.close();

            //TODO: get Maidens from T005 DB (group by overnum and bowler)
            dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T301_MAIDENS + " from " + SQLiteDBManager.T301_PLAYER_BOWLER
                    + " where " + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID,null);
            dbCursor.moveToNext();
            CurrentBowlerMaidenCount=dbCursor.getInt(0);
            this.CurrentBowlerMaidens.setText(Integer.toString(CurrentBowlerMaidenCount));
            dbCursor.close();
        }
        else
        {
            dbClass.InsertDataToT301(CurrentBowlerID,Integer.parseInt(matchID),2,0,0,0,0,0,0,db);
            this.CurrentBowlerOvers.setText("0.0");
            CurrentBowlerMaidenCount=0;
            this.CurrentBowlerMaidens.setText("0");
            this.CurrentBowlerRuns.setText("0");
            this.CurrentBowlerWickets.setText("0");
            this.CurrentBowlerEcon.setText("0.00");
        }
    }

    public void add_run(int runs)
    {
        //DONOT ADD EXTRAS HERE
        runs_scored=runs_scored+runs;
        batsmen_run=Integer.parseInt(StrikeBatsmenRuns.getText().toString()) + runs;
        bowler_run=Integer.parseInt(CurrentBowlerRuns.getText().toString()) + runs;
        StrikeBatsmenRuns.setText(Integer.toString(batsmen_run));
        CurrentBowlerRuns.setText(Integer.toString(bowler_run));

        if (runs == 0 )
        {
            db.execSQL("update " + SQLiteDBManager.T302_PLAYER_BATSMEN + " set "
                    + " " + SQLiteDBManager.COL_T302_BALLS_FACED + "=" + StrikeBatsmenBalls.getText().toString()
                    + ", " + SQLiteDBManager.COL_T302_DOT_BALLS + "=" + SQLiteDBManager.COL_T302_DOT_BALLS + "+1"
                    + ", " + SQLiteDBManager.COL_T302_RUNS_SCORED + "=" + batsmen_run
                    + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                    + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + StrikerBatsmenID);
            db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER + " set "
                    + SQLiteDBManager.COL_T301_MAIDENS + "=" + SQLiteDBManager.COL_T301_MAIDENS + "+" + maiden_counter + ", "
                    + SQLiteDBManager.COL_T301_DOT_BALLS + "=" + SQLiteDBManager.COL_T301_DOT_BALLS + "+1 where "
                    + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID);
            CurrentBowlerDots=CurrentBowlerDots+1;
        }
        else if (runs == 4 )
        {
            db.execSQL("update " + SQLiteDBManager.T302_PLAYER_BATSMEN + " set "
                    + " " + SQLiteDBManager.COL_T302_BALLS_FACED + "=" + StrikeBatsmenBalls.getText().toString()
                    + ", " + SQLiteDBManager.COL_T302_RUNS_SCORED + "=" + batsmen_run
                    + ", " + SQLiteDBManager.COL_T302_FOURS + "=" + SQLiteDBManager.COL_T302_FOURS + "+1 "
                    + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                    + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + StrikerBatsmenID);
            CurrentBowlerDots=0;
        }
        else if ( runs == 6 )
        {
            db.execSQL("update " + SQLiteDBManager.T302_PLAYER_BATSMEN + " set "
                    + " " + SQLiteDBManager.COL_T302_BALLS_FACED + "=" + StrikeBatsmenBalls.getText().toString()
                    + ", " + SQLiteDBManager.COL_T302_RUNS_SCORED + "=" + batsmen_run
                    + ", " + SQLiteDBManager.COL_T302_SIXES + "=" + SQLiteDBManager.COL_T302_SIXES + "+1 "
                    + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                    + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + StrikerBatsmenID);
            CurrentBowlerDots=0;
        }
        else
        {
            db.execSQL("update " + SQLiteDBManager.T302_PLAYER_BATSMEN + " set "
                    + " " + SQLiteDBManager.COL_T302_BALLS_FACED + "=" + StrikeBatsmenBalls.getText().toString()
                    + ", " + SQLiteDBManager.COL_T302_RUNS_SCORED + "=" + batsmen_run
                    + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                    + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + StrikerBatsmenID);
            CurrentBowlerDots=0;
        }
        db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER + " set "
                + " " + SQLiteDBManager.COL_T301_BALLS_BOWLED + "=" + SQLiteDBManager.COL_T301_BALLS_BOWLED + "+1"
                + ", " + SQLiteDBManager.COL_T301_RUNS_CONCEEDED + "=" + bowler_run
                + " where " + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID);

        batsmen_balls=Integer.parseInt(this.StrikeBatsmenBalls.getText().toString()) + 1;
        this.StrikeBatsmenBalls.setText(Integer.toString(batsmen_balls));

        String BowlerOver=this.CurrentBowlerOvers.getText().toString();
        String[] pattern = BowlerOver.split("\\.");
        if ( Integer.parseInt(pattern[1]) == 5)
        {
            int newover = Integer.parseInt(pattern[0]);
            newover+=1;
            this.CurrentBowlerOvers.setText(Integer.toString(newover) + ".0");
        }
        else
        {
            int ballnum = Integer.parseInt(pattern[1]);
            this.CurrentBowlerOvers.setText(pattern[0] + "." + Integer.toString(ballnum + 1));
        }

        this.CurrentBowlerBalls = CurrentBowlerBalls + 1;
        if(CurrentBowlerDots==6)
        {
            CurrentBowlerMaidenCount=CurrentBowlerMaidenCount + maiden_counter;
            this.CurrentBowlerMaidens.setText(Integer.toString(CurrentBowlerMaidenCount));
        }

        Double sr;
        Double Econ;
        Double CRR;
        Double divisor;
        Double divider;
        DecimalFormat df = new DecimalFormat("###.##");

        divisor=Double.parseDouble(this.StrikeBatsmenRuns.getText().toString());
        divider=Double.parseDouble(this.StrikeBatsmenBalls.getText().toString());

        if ( divider == 0 )
        {
            this.StrikeBatsmenStrikeRate.setText(this.StrikeBatsmenRuns.getText().toString() + ".00");
        }
        else
        {
            sr = (divisor / divider)*100;
            this.StrikeBatsmenStrikeRate.setText(df.format(sr));
        }

        divisor=Double.parseDouble(this.CurrentBowlerRuns.getText().toString());
        divider=Double.parseDouble(Integer.toString(CurrentBowlerBalls));

        if ( divider == 0 )
        {
            this.CurrentBowlerEcon.setText("0.00");
        }
        else
        {
            Econ = (divisor / divider)*6;
            this.CurrentBowlerEcon.setText(df.format(Econ));
        }

        // Setting Current run Rate
        //TODO: set condition for first or second innings - and calculate required run rate as well.
        if(currentInningNumber==2){

        }
        if ( this.CurrRunrate.getText().toString() == "N/A" )
        {
            this.CurrRunrate.setText("0.00");
        }
        else
        {
            divisor = Double.parseDouble(Integer.toString(runs_scored));
            divider = Double.parseDouble(Integer.toString(legal_balls_delivered));
            if (divider>0) {
                CRR = (divisor / divider) * 6;
            }
            else
            {
                CRR=Double.parseDouble(Integer.toString(0));
            }
            this.CurrRunrate.setText(df.format(CRR));
        }
    }

    private void start_first_innings()
    {
        currentInningNumber=1;
        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + StrikerBatsmenID,null);
        dbCursor.moveToNext();
        this.StrikeBatsmenName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + NonStrikerBatsmenID,null);
        dbCursor.moveToNext();
        this.NonStrikeBatsmenName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + CurrentBowlerID,null);
        dbCursor.moveToNext();
        this.CurrentBowlerName.setText(dbCursor.getString(0));
        dbCursor.close();
    }

    private void start_second_innings()
    {
        db.execSQL("update " + SQLiteDBManager.T003_MATCHES + " set " + SQLiteDBManager.COL_T003_INNING_NUMBER + "=" + currentInningNumber
            + " where " + SQLiteDBManager.COL_T003_MATCH_ID + "=" + matchID);
        bottom_layout.removeView(pending_inning_button);

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + StrikerBatsmenID,null);
        dbCursor.moveToNext();
        this.StrikeBatsmenName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + NonStrikerBatsmenID,null);
        dbCursor.moveToNext();
        this.NonStrikeBatsmenName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + CurrentBowlerID,null);
        dbCursor.moveToNext();
        this.CurrentBowlerName.setText(dbCursor.getString(0));
        dbCursor.close();

        CurrentBowlerOvers.setText("0.0");
        CurrentBowlerMaidens.setText("0");
        CurrentBowlerRuns.setText("0");
        CurrentBowlerWickets.setText("0");
        CurrentBowlerEcon.setText("0.00");
        CurrentBowlerDots=0;
        PrevBowlerName.setText("");
        PrevBowlerOvers.setText("");
        PrevBowlerMaidens.setText("");
        PrevBowlerRuns.setText("");
        PrevBowlerWickets.setText("");
        PrevBowlerEcon.setText("");
        PrevBowlerDots=0;
        this.StrikeBatsmenRuns.setText("0");
        this.StrikeBatsmenBalls.setText("0");
        this.StrikeBatsmenFours.setText("0");
        this.StrikeBatsmenSixes.setText("0");
        this.StrikeBatsmenStrikeRate.setText("0.00");
        this.NonStrikeBatsmenRuns.setText("0");
        this.NonStrikeBatsmenBalls.setText("0");
        this.NonStrikeBatsmenFours.setText("0");
        this.NonStrikeBatsmenSixes.setText("0");
        this.NonStrikeBatsmenStrikeRate.setText("0.00");
    }

    private void resume_first_innings()
    {
        System.out.println("ScoreCardFragment - First Innings");
        currentInningNumber=1;
        int over;
        int balls;
        String TeamAScore;
        String TeamAOvers;
        String TeamBScore;
        String TeamBOvers;
        Boolean LastBallFlag=false;
        Double sr;
        Double Econ;
        Double CRR;
        Double divisor;
        Double divider;
        DecimalFormat df = new DecimalFormat("###.##");

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_OVER_NUM + "," + SQLiteDBManager.COL_T005_BALL_NUM + ","
                + SQLiteDBManager.COL_T005_BOWLER_PLAYER_ID + "," + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + ","
                + SQLiteDBManager.COL_T005_NONSTRIKER_PLAYER_ID + "," + SQLiteDBManager.COL_T005_RUNS_SCORED
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID
                    + "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                    + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID + ")"
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID, null);
        if (scoreCursor.getCount() == 1) {
            scoreCursor.moveToNext();
            first_inning_over_display=scoreCursor.getInt(0);
            legal_balls_delivered = (Integer.parseInt(scoreCursor.getString(0)) * 6) + Integer.parseInt(scoreCursor.getString(1));
            TeamAOvers = scoreCursor.getString(0) + "." + scoreCursor.getString(1);
            this.CurrentBowlerID=scoreCursor.getInt(2);
            this.StrikerBatsmenID=scoreCursor.getInt(3);
            this.NonStrikerBatsmenID=scoreCursor.getInt(4);
            if (scoreCursor.getInt(1) == 0)
            {
                if (scoreCursor.getInt(0) >= 1)
                {
                    LastBallFlag = true;
                }
                else
                {
                    LastBallFlag = false;
                }
            }
        } else {
            TeamAOvers = "0.0";
            first_inning_over_display=0;
            legal_balls_delivered=0;
        }
        scoreCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + StrikerBatsmenID,null);
        dbCursor.moveToNext();
        this.StrikeBatsmenName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + NonStrikerBatsmenID,null);
        dbCursor.moveToNext();
        this.NonStrikeBatsmenName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + CurrentBowlerID,null);
        dbCursor.moveToNext();
        this.CurrentBowlerName.setText(dbCursor.getString(0));
        dbCursor.close();

        scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                + SQLiteDBManager.COL_T005_WICKET_FLAG + "='Y' then 1 else 0 end)"
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
//                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + TeamAID
                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " group by " + SQLiteDBManager.COL_T005_MATCH_ID, null);
        if (scoreCursor.getCount() == 1) {
            scoreCursor.moveToNext();
            this.runs_scored = Integer.parseInt(scoreCursor.getString(0));
            this.wickets_first_inning = Integer.parseInt(scoreCursor.getString(1));
            TeamAScore = runs_scored + "/" + wickets_first_inning;
        } else {
            TeamAScore = "0/0";
        }
        scoreCursor.close();

        divisor = Double.parseDouble(Integer.toString(runs_scored));
        divider = Double.parseDouble(Integer.toString(legal_balls_delivered));
        if (divider>0) {
            CRR = (divisor / divider) * 6;
        }
        else
        {
            CRR=Double.parseDouble(Integer.toString(0));
        }
        this.CurrRunrate.setText(df.format(CRR));

        scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                + SQLiteDBManager.COL_T005_WICKET_FLAG + "='Y' then 1 else 0 end)"
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
//                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + TeamBID
                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BowlingTeamID
                + " group by " + SQLiteDBManager.COL_T005_MATCH_ID, null);
        if (scoreCursor.getCount() == 1) {
            scoreCursor.moveToNext();
            this.wickets_second_inning = Integer.parseInt(scoreCursor.getString(1));
            TeamBScore = scoreCursor.getString(0) + "/" + wickets_second_inning;
        } else {
            TeamBScore = "0/0";
        }
        scoreCursor.close();

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_OVER_NUM + "," + SQLiteDBManager.COL_T005_BALL_NUM
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS + " where " + SQLiteDBManager.COL_T005_BALL_ID
                + "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
//                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + TeamBID + ")"
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BowlingTeamID + ")"
//                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + TeamBID, null);
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BowlingTeamID, null);
        if (scoreCursor.getCount() == 1) {
            scoreCursor.moveToNext();
            TeamBOvers = scoreCursor.getString(0) + "." + scoreCursor.getString(1);
        } else {
            TeamBOvers = "0.0";
        }
        scoreCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "="
                + "(select " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID + "="
                + "(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + "))", null);
        dbCursor.moveToNext();
        this.StrikerBatsmenID = Integer.parseInt(dbCursor.getString(0));
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "="
                + "(select " + SQLiteDBManager.COL_T005_NONSTRIKER_PLAYER_ID + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID + "="
                + "(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + "))", null);
        dbCursor.moveToNext();
        this.NonStrikerBatsmenID = Integer.parseInt(dbCursor.getString(0));
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "="
                + "(select " + SQLiteDBManager.COL_T005_BOWLER_PLAYER_ID + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID + "="
                + "(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + "))", null);
        dbCursor.moveToNext();
        this.CurrentBowlerID = Integer.parseInt(dbCursor.getString(0));
        dbCursor.close();

        this.FirstBattingTeamScore.setText(TeamAScore + "(" + TeamAOvers + ")");
        this.SecondBattingTeamScore.setText(TeamBScore + "(" + TeamBOvers + ")");

        //Getting StrikeBatsmen details.
        scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                + SQLiteDBManager.COL_T005_LEGAL_BALL_FLAG + "='Y' then 1 else 0 end)"
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + "=" + StrikerBatsmenID
                + " group by " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID,null);
        scoreCursor.moveToNext();
        this.StrikeBatsmenRuns.setText(scoreCursor.getString(0));
        this.StrikeBatsmenBalls.setText(scoreCursor.getString(1));
        scoreCursor.close();

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T302_FOURS + "," + SQLiteDBManager.COL_T302_SIXES
                + " from " + SQLiteDBManager.T302_PLAYER_BATSMEN
                + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + StrikerBatsmenID,null);
        scoreCursor.moveToNext();
        this.StrikeBatsmenFours.setText(scoreCursor.getString(0));
        this.StrikeBatsmenSixes.setText(scoreCursor.getString(1));
        scoreCursor.close();

        divisor=Double.parseDouble(this.StrikeBatsmenRuns.getText().toString());
        divider=Double.parseDouble(this.StrikeBatsmenBalls.getText().toString());

        if ( divider == 0 )
        {
            this.StrikeBatsmenStrikeRate.setText(this.StrikeBatsmenRuns.getText().toString() + ".00");
        }
        else
        {
            sr = (divisor / divider)*100;
            this.StrikeBatsmenStrikeRate.setText(df.format(sr));
        }

        scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                + SQLiteDBManager.COL_T005_LEGAL_BALL_FLAG + "='Y' then 1 else 0 end)"
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + "=" + NonStrikerBatsmenID
                + " group by " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID,null);
        if (scoreCursor.getCount()>0)
        {
            scoreCursor.moveToNext();
            this.NonStrikeBatsmenRuns.setText(scoreCursor.getString(0));
            this.NonStrikeBatsmenBalls.setText(scoreCursor.getString(1));
            scoreCursor.close();
        }
        else
        {
            this.NonStrikeBatsmenRuns.setText("0");
            this.NonStrikeBatsmenBalls.setText("0");
        }

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T302_FOURS + "," + SQLiteDBManager.COL_T302_SIXES
                + " from " + SQLiteDBManager.T302_PLAYER_BATSMEN
                + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + NonStrikerBatsmenID,null);
        if (scoreCursor.getCount()>0) {
            scoreCursor.moveToNext();
            this.NonStrikeBatsmenFours.setText(scoreCursor.getString(0));
            this.NonStrikeBatsmenSixes.setText(scoreCursor.getString(1));
            scoreCursor.close();
        }
        else
        {
            this.NonStrikeBatsmenFours.setText("0");
            this.NonStrikeBatsmenSixes.setText("0");
        }

        divisor=Double.parseDouble(this.NonStrikeBatsmenRuns.getText().toString());
        divider=Double.parseDouble(this.NonStrikeBatsmenBalls.getText().toString());

        if ( divider == 0 )
        {
            this.NonStrikeBatsmenStrikeRate.setText(this.NonStrikeBatsmenRuns.getText().toString() + ".00");
        }
        else
        {
            sr = (divisor / divider)*100;
            this.NonStrikeBatsmenStrikeRate.setText(df.format(sr));
        }

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T301_BALLS_BOWLED + ","  + SQLiteDBManager.COL_T301_MAIDENS + ","
                + SQLiteDBManager.COL_T301_RUNS_CONCEEDED + "," + SQLiteDBManager.COL_T301_WICKETS
                + " from " + SQLiteDBManager.T301_PLAYER_BOWLER
                + " where " + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID,null);
        scoreCursor.moveToNext();
        CurrentBowlerBalls=Integer.parseInt(scoreCursor.getString(0));
        over=(int) Math.floor(Integer.parseInt(scoreCursor.getString(0))/6);
        balls=Integer.parseInt(scoreCursor.getString(0))%6;
        this.CurrentBowlerMaidens.setText(scoreCursor.getString(1));
        this.CurrentBowlerRuns.setText(scoreCursor.getString(2));
        divisor=Double.parseDouble(scoreCursor.getString(2));
        this.CurrentBowlerWickets.setText(scoreCursor.getString(3));
        scoreCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_RUNS_SCORED
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS + " where " + SQLiteDBManager.COL_T005_OVER_NUM + "=" + first_inning_over_display
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BOWLER_PLAYER_ID + "=" + CurrentBowlerID
                + " order by " + SQLiteDBManager.COL_T005_BALL_ID,null);
        while (dbCursor.moveToNext())
        {
            int run=dbCursor.getInt(0);
            if ( run == 0 )
            {
                number_of_dots+=1;
            }
            else
            {
                number_of_dots=0;
            }
        }
        dbCursor.close();

        divider=Double.parseDouble(Integer.toString(CurrentBowlerBalls));
        this.CurrentBowlerOvers.setText(over + "." + balls);

        if ( divider == 0 )
        {
            this.CurrentBowlerEcon.setText("0.00");
        }
        else
        {
            Econ = (divisor / divider)*6;
            this.CurrentBowlerEcon.setText(df.format(Econ));
        }

        this.PrevBowlerName.setText("");
        this.PrevBowlerOvers.setText("");
        this.PrevBowlerMaidens.setText("");
        this.PrevBowlerRuns.setText("");
        this.PrevBowlerWickets.setText("");
        this.PrevBowlerEcon.setText("");

        if (LastBallFlag)
        {
            getNextBowler(Integer.parseInt(this.BowlingTeamID));
        }
    }

    private void resume_second_innings()
    {
        //TODO: resume after the app is closed after selecting initial players(with no balls bowled) - is not working.
        currentInningNumber=2;
        if (String.valueOf(StrikerBatsmenID).equals("0") || String.valueOf(NonStrikerBatsmenID).equals("0") || String.valueOf(CurrentBowlerID).equals("0"))
        {
            getInitialPlayers(Integer.parseInt(BattingTeamID), Integer.parseInt(BowlingTeamID));
            return;
        }
        else
        {
            if (bottom_layout.getChildCount()>0) {
                bottom_layout.removeView(pending_inning_button);
            }
        }
        db.execSQL("update " + SQLiteDBManager.T003_MATCHES + " set " + SQLiteDBManager.COL_T003_INNING_NUMBER + "=" + currentInningNumber
                + " where " + SQLiteDBManager.COL_T003_MATCH_ID + "=" + matchID);
        int over;
        int balls;
        String TeamAScore;
        String TeamAOvers;
        String TeamBScore;
        String TeamBOvers;
        Boolean LastBallFlag=false;
        Double sr;
        Double Econ;
        Double CRR;
        Double divisor;
        Double divider;
        DecimalFormat df = new DecimalFormat("###.##");

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_BOWL_TEAM_ID +"," + SQLiteDBManager.COL_T005_BATT_TEAM_ID
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS + " where "
                + SQLiteDBManager.COL_T005_BALL_ID + "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from "
                    + SQLiteDBManager.T005_OVER_BY_BALL_STATS + " where " + SQLiteDBManager.COL_T005_MATCH_ID +"=" + matchID + ")",null);
        dbCursor.moveToNext();

        System.out.println("ScoreCardFragment: Data from T005 DB - " + dbCursor.getInt(0) + ", " + dbCursor.getInt(0));
        System.out.println("ScoreCardFragment: Data from Function call - " + BowlingTeamID + "," + BattingTeamID);

        if (Integer.parseInt(BowlingTeamID)!=dbCursor.getInt(0))
        {
            getInitialPlayers(Integer.parseInt(BattingTeamID), Integer.parseInt(BowlingTeamID));
        }
        dbCursor.close();

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_OVER_NUM + "," + SQLiteDBManager.COL_T005_BALL_NUM + ","
                + SQLiteDBManager.COL_T005_BOWLER_PLAYER_ID + "," + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + ","
                + SQLiteDBManager.COL_T005_NONSTRIKER_PLAYER_ID + "," + SQLiteDBManager.COL_T005_RUNS_SCORED
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID
                + "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BowlingTeamID + ")"
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BowlingTeamID, null);
        if (scoreCursor.getCount() == 1) {
            scoreCursor.moveToNext();
            first_inning_over_display=scoreCursor.getInt(0);
            TeamAOvers = scoreCursor.getString(0) + "." + scoreCursor.getString(1);
        } else {
            TeamAOvers = "0.0";
            first_inning_over_display=0;
            legal_balls_delivered=0;
        }
        scoreCursor.close();

        scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                + SQLiteDBManager.COL_T005_WICKET_FLAG + "='Y' then 1 else 0 end)"
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
//                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + TeamAID
                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BowlingTeamID
                + " group by " + SQLiteDBManager.COL_T005_MATCH_ID, null);
        if (scoreCursor.getCount() == 1) {
            scoreCursor.moveToNext();
            this.target_runs = Integer.parseInt(scoreCursor.getString(0));
            this.wickets_first_inning = Integer.parseInt(scoreCursor.getString(1));
            TeamAScore = target_runs + "/" + wickets_first_inning;
            this.target_runs = target_runs+1;
        } else {
            TeamAScore = "0/0";
        }
        scoreCursor.close();

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_OVER_NUM + "," + SQLiteDBManager.COL_T005_BALL_NUM + ","
                + SQLiteDBManager.COL_T005_BOWLER_PLAYER_ID + "," + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + ","
                + SQLiteDBManager.COL_T005_NONSTRIKER_PLAYER_ID + "," + SQLiteDBManager.COL_T005_RUNS_SCORED
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID
                + "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID + ")"
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID, null);
        if (scoreCursor.getCount() == 1) {
            scoreCursor.moveToNext();
            second_inning_over_display=scoreCursor.getInt(0);
            legal_balls_delivered = (Integer.parseInt(scoreCursor.getString(0)) * 6) + Integer.parseInt(scoreCursor.getString(1));
            TeamBOvers = scoreCursor.getString(0) + "." + scoreCursor.getString(1);
            this.CurrentBowlerID=scoreCursor.getInt(2);
            this.StrikerBatsmenID=scoreCursor.getInt(3);
            this.NonStrikerBatsmenID=scoreCursor.getInt(4);
            System.out.println("ScoreCardFragment - " + scoreCursor.getInt(0) +"."+scoreCursor.getInt(1));
            if (scoreCursor.getInt(1) == 0)
            {
                if (scoreCursor.getInt(0) >= 1)
                {
                    LastBallFlag = true;
                }
                else
                {
                    LastBallFlag = false;
                }
            }
        } else {
            TeamBOvers = "0.0";
            second_inning_over_display=0;
            legal_balls_delivered=0;
        }
        scoreCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "="
                + "(select " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID + "="
                + "(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + "))", null);
        dbCursor.moveToNext();
        this.StrikerBatsmenID = Integer.parseInt(dbCursor.getString(0));
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "="
                + "(select " + SQLiteDBManager.COL_T005_NONSTRIKER_PLAYER_ID + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID + "="
                + "(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + "))", null);
        dbCursor.moveToNext();
        this.NonStrikerBatsmenID = Integer.parseInt(dbCursor.getString(0));
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "="
                + "(select " + SQLiteDBManager.COL_T005_BOWLER_PLAYER_ID + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BALL_ID + "="
                + "(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + "))", null);
        dbCursor.moveToNext();
        this.CurrentBowlerID = Integer.parseInt(dbCursor.getString(0));
        dbCursor.close();

        scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                + SQLiteDBManager.COL_T005_WICKET_FLAG + "='Y' then 1 else 0 end)"
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
//                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + TeamAID
                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " group by " + SQLiteDBManager.COL_T005_MATCH_ID, null);
        if (scoreCursor.getCount() == 1) {
            scoreCursor.moveToNext();
            this.runs_scored = Integer.parseInt(scoreCursor.getString(0));
            this.wickets_second_inning = Integer.parseInt(scoreCursor.getString(1));
            TeamBScore = runs_scored + "/" + wickets_second_inning;
        } else {
            TeamBScore = "0/0";
            runs_scored=0;
        }
        scoreCursor.close();

        // Set Required Runrate.
        if(runs_scored>target_runs)
        {
            this.ReqRunrate.setText("N/A");
        }
        else
        {
            divisor = Double.parseDouble(Integer.toString(target_runs-runs_scored));
            divider = Double.parseDouble(Integer.toString(legal_balls_maximum-legal_balls_delivered));
            if (divider>0) {
                CRR = (divisor / divider) * 6;
            }
            else
            {
                CRR=Double.parseDouble(Integer.toString(0));
            }
            this.ReqRunrate.setText(df.format(CRR));
        }

        // Set Current Runrate
        divisor = Double.parseDouble(Integer.toString(runs_scored));
        divider = Double.parseDouble(Integer.toString(legal_balls_delivered));
        if (divider>0) {
            CRR = (divisor / divider) * 6;
        }
        else
        {
            CRR=Double.parseDouble(Integer.toString(0));
        }
        this.CurrRunrate.setText(df.format(CRR));

        this.FirstBattingTeamScore.setText(TeamAScore + "(" + TeamAOvers + ")");
        this.SecondBattingTeamScore.setText(TeamBScore + "(" + TeamBOvers + ")");

        //Getting StrikeBatsmen details.
        scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                + SQLiteDBManager.COL_T005_LEGAL_BALL_FLAG + "='Y' then 1 else 0 end)"
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + "=" + StrikerBatsmenID
                + " group by " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID,null);
        scoreCursor.moveToNext();
        this.StrikeBatsmenRuns.setText(scoreCursor.getString(0));
        this.StrikeBatsmenBalls.setText(scoreCursor.getString(1));
        scoreCursor.close();

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T302_FOURS + "," + SQLiteDBManager.COL_T302_SIXES
                + " from " + SQLiteDBManager.T302_PLAYER_BATSMEN
                + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + StrikerBatsmenID,null);
        scoreCursor.moveToNext();
        this.StrikeBatsmenFours.setText(scoreCursor.getString(0));
        this.StrikeBatsmenSixes.setText(scoreCursor.getString(1));
        scoreCursor.close();

        divisor=Double.parseDouble(this.StrikeBatsmenRuns.getText().toString());
        divider=Double.parseDouble(this.StrikeBatsmenBalls.getText().toString());

        if ( divider == 0 )
        {
            this.StrikeBatsmenStrikeRate.setText(this.StrikeBatsmenRuns.getText().toString() + ".00");
        }
        else
        {
            sr = (divisor / divider)*100;
            this.StrikeBatsmenStrikeRate.setText(df.format(sr));
        }

        scoreCursor = db.rawQuery("select sum(" + SQLiteDBManager.COL_T005_RUNS_SCORED + "),sum(case when "
                + SQLiteDBManager.COL_T005_LEGAL_BALL_FLAG + "='Y' then 1 else 0 end)"
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + "=" + NonStrikerBatsmenID
                + " group by " + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID,null);
        if (scoreCursor.getCount()>0)
        {
            scoreCursor.moveToNext();
            this.NonStrikeBatsmenRuns.setText(scoreCursor.getString(0));
            this.NonStrikeBatsmenBalls.setText(scoreCursor.getString(1));
            scoreCursor.close();
        }
        else
        {
            this.NonStrikeBatsmenRuns.setText("0");
            this.NonStrikeBatsmenBalls.setText("0");
        }

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T302_FOURS + "," + SQLiteDBManager.COL_T302_SIXES
                + " from " + SQLiteDBManager.T302_PLAYER_BATSMEN
                + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + NonStrikerBatsmenID,null);
        if (scoreCursor.getCount()>0) {
            scoreCursor.moveToNext();
            this.NonStrikeBatsmenFours.setText(scoreCursor.getString(0));
            this.NonStrikeBatsmenSixes.setText(scoreCursor.getString(1));
            scoreCursor.close();
        }
        else
        {
            this.NonStrikeBatsmenFours.setText("0");
            this.NonStrikeBatsmenSixes.setText("0");
        }

        divisor=Double.parseDouble(this.NonStrikeBatsmenRuns.getText().toString());
        divider=Double.parseDouble(this.NonStrikeBatsmenBalls.getText().toString());

        if ( divider == 0 )
        {
            this.NonStrikeBatsmenStrikeRate.setText(this.NonStrikeBatsmenRuns.getText().toString() + ".00");
        }
        else
        {
            sr = (divisor / divider)*100;
            this.NonStrikeBatsmenStrikeRate.setText(df.format(sr));
        }

        scoreCursor = db.rawQuery("select " + SQLiteDBManager.COL_T301_BALLS_BOWLED + ","  + SQLiteDBManager.COL_T301_MAIDENS + ","
                + SQLiteDBManager.COL_T301_RUNS_CONCEEDED + "," + SQLiteDBManager.COL_T301_WICKETS
                + " from " + SQLiteDBManager.T301_PLAYER_BOWLER
                + " where " + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID,null);
        scoreCursor.moveToNext();
        CurrentBowlerBalls=Integer.parseInt(scoreCursor.getString(0));
        over=(int) Math.floor(Integer.parseInt(scoreCursor.getString(0))/6);
        balls=Integer.parseInt(scoreCursor.getString(0))%6;
        this.CurrentBowlerMaidens.setText(scoreCursor.getString(1));
        this.CurrentBowlerRuns.setText(scoreCursor.getString(2));
        divisor=Double.parseDouble(scoreCursor.getString(2));
        this.CurrentBowlerWickets.setText(scoreCursor.getString(3));
        scoreCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_RUNS_SCORED
                + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS + " where " + SQLiteDBManager.COL_T005_OVER_NUM + "=" + second_inning_over_display
                + " and " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + " and " + SQLiteDBManager.COL_T005_BOWLER_PLAYER_ID + "=" + CurrentBowlerID
                + " order by " + SQLiteDBManager.COL_T005_BALL_ID,null);
        while (dbCursor.moveToNext())
        {
            int run=dbCursor.getInt(0);
            if ( run == 0 )
            {
                number_of_dots+=1;
            }
            else
            {
                number_of_dots=0;
            }
        }
        dbCursor.close();

        divider=Double.parseDouble(Integer.toString(CurrentBowlerBalls));
        this.CurrentBowlerOvers.setText(over + "." + balls);

        if ( divider == 0 )
        {
            this.CurrentBowlerEcon.setText("0.00");
        }
        else
        {
            Econ = (divisor / divider)*6;
            this.CurrentBowlerEcon.setText(df.format(Econ));
        }

        this.PrevBowlerName.setText("");
        this.PrevBowlerOvers.setText("");
        this.PrevBowlerMaidens.setText("");
        this.PrevBowlerRuns.setText("");
        this.PrevBowlerWickets.setText("");
        this.PrevBowlerEcon.setText("");

        if (LastBallFlag)
        {
            getNextBowler(Integer.parseInt(this.BowlingTeamID));
        }
    }

    private boolean add_a_legal_delivery(int run)
    {
        legal_balls_delivered=legal_balls_delivered+1;
        add_run(run);
        int overnum;
        overnum=(int) Math.floor(this.legal_balls_delivered/6);
        dbClass.InsertDataToT005(Integer.parseInt(this.matchID),Integer.parseInt(this.BattingTeamID),Integer.parseInt(this.BowlingTeamID),this.StrikerBatsmenID,this.CurrentBowlerID,this.NonStrikerBatsmenID,overnum,current_ball_number,run,"N","Y",db);

        if ( legal_balls_delivered >= legal_balls_maximum )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void add_a_nonlegal_delivery()
    {

    }

    private void setRecents(String appender)
    {
        String trimmedText;
        String currentText;
        currentText=this.RecentBalls.getText().toString();
        if ( this.RecentBalls.getText().toString().length() >= 50 )
        {
            trimmedText = currentText.substring(0,currentText.length() - 3 );
        }
        else
        {
            trimmedText = currentText;
        }
        this.RecentBalls.setText(appender + trimmedText);
    }

    private void undoLastBall()
    {
        //TODO: undo the balls by using max BALL_ID and deleting from T005 table.
        int max_ball_id;
        int battplayerid;
        int nonstrikeplayerid;
        int bowlplayerid;
        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T005_BALL_ID + "," + SQLiteDBManager.COL_T005_BATTING_PLAYER_ID + ","
                + SQLiteDBManager.COL_T005_LEGAL_BALL_FLAG + "," + SQLiteDBManager.COL_T005_BOWLER_PLAYER_ID + ","
                + SQLiteDBManager.COL_T005_NONSTRIKER_PLAYER_ID + "," + SQLiteDBManager.COL_T005_WICKET_FLAG + ","
                + SQLiteDBManager.COL_T005_RUNS_SCORED + "," + SQLiteDBManager.COL_T005_OVER_NUM + ","
                + SQLiteDBManager.COL_T005_BALL_NUM + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS +" where " + SQLiteDBManager.COL_T005_BALL_ID +
                "=(select max(" + SQLiteDBManager.COL_T005_BALL_ID + ") from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + ")",null);
        dbCursor.moveToNext();
        if (dbCursor.getInt(8) != 0) {
            max_ball_id = dbCursor.getInt(0);
            battplayerid = dbCursor.getInt(1);
            bowlplayerid = dbCursor.getInt(3);
            nonstrikeplayerid = dbCursor.getInt(4);
            //TODO do this at the last
//            if (dbCursor.getString(2).equals("Y")) {
//                legal_balls_delivered = legal_balls_delivered - 1;
//            }
        }
        else
        {
            System.out.println("ScoreCardFragment: you can only undo deliveries of this over");
            //TODO: resume innings.
        }
        dbCursor.close();

        // NO UNDO before 1st ball of the over (i.e. back to previous over)
        // TODO: Subtract data from T006 - If Wicket had fallen
        // TODO: Subtract data from T301
        // TODO: Subtract data from T302
        // TODO: Subtract data from T303
        // TODO: Subtract data from T304
        //Finally
        // TODO: Subtract data from T005
        // TODO: repopulate batsmen, team scores and

    }

    public void getInitialPlayerDataFromFragment(int OpeningBatsmenID, int NonStrikingBatsmenID, int OpeningBowlerID)
    {
        this.StrikerBatsmenID=OpeningBatsmenID;
        this.NonStrikerBatsmenID=NonStrikingBatsmenID;
        this.CurrentBowlerID=OpeningBowlerID;

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + StrikerBatsmenID,null);
        dbCursor.moveToNext();
        this.StrikeBatsmenName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + NonStrikerBatsmenID,null);
        dbCursor.moveToNext();
        this.NonStrikeBatsmenName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + CurrentBowlerID,null);
        dbCursor.moveToNext();
        this.CurrentBowlerName.setText(dbCursor.getString(0));
        dbCursor.close();

        dbClass.InsertDataToT005(Integer.parseInt(matchID),Integer.parseInt(BattingTeamID),Integer.parseInt(BowlingTeamID),StrikerBatsmenID,CurrentBowlerID,NonStrikerBatsmenID,0,0,0,"N","N",db);

        dbClass.InsertDataToT302(OpeningBatsmenID,Integer.parseInt(matchID),1,0,0,0,0,0,0,0,0,db);
        dbClass.InsertDataToT302(NonStrikingBatsmenID,Integer.parseInt(matchID),1,0,0,0,0,0,0,0,0,db);
        dbClass.InsertDataToT301(OpeningBowlerID,Integer.parseInt(matchID),2,0,0,0,0,0,0,db);

        if(bottom_layout.getChildCount()==1)
        {
            resume_second_innings();
        }
        else
        {
            if (currentInningNumber==1)
            {
                start_first_innings();
            }
            else if (currentInningNumber==2)
            {
                start_second_innings();
            }
        }
    }

    public void getBowlerDataFromFragment ( int nextBowlerID )
    {
        this.CurrentBowlerID=nextBowlerID;

        dbCursor = db.rawQuery("select * from " + SQLiteDBManager.T301_PLAYER_BOWLER + " where "
                + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + nextBowlerID + " and " + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID ,null);
        if ( dbCursor.getCount() == 0 )
        {
            dbClass.InsertDataToT301(nextBowlerID,Integer.parseInt(matchID),2,0,0,0,0,0,0,db);
        }
        getNewBowlerAndSwitch();
    }

    public void NextBatsmenAfterWicket ( int WicketBattID, int nextBatsmenID , boolean crossed,String WicketType,int FielderID, int AdditionalRuns)
    {
        //TODO: start from here - last ball wicket change of bowler and add 1 over
        int wicket_count;
        int overnum;

        if(currentInningNumber==1)
        {
            wickets_first_inning = wickets_first_inning +1;
        }
        else if(currentInningNumber==1)
        {
            wickets_second_inning = wickets_second_inning +1;
        }
        legal_balls_delivered=legal_balls_delivered+1;
        overnum=(int) Math.floor(this.legal_balls_delivered/6);
        current_ball_number = legal_balls_delivered % 6;

        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + WicketBattID,null);
        scoreCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + nextBatsmenID,null);
        dbCursor.moveToNext();
        scoreCursor.moveToNext();
        dbCursor.close();
        scoreCursor.close();

        // Get Wicket count
        dbCursor=db.rawQuery("select count(*) from " + SQLiteDBManager.T006_WICKETS
                + " where " + SQLiteDBManager.COL_T006_BALL_ID + " in "
                + "(select " + SQLiteDBManager.COL_T005_BALL_ID + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID + ")",null);

        if ( dbCursor.getCount() == 0 ) {wicket_count = 0;} else{dbCursor.moveToNext(); wicket_count = dbCursor.getInt(0);}
        dbCursor.close();

        // Update or Insert Data to T301_PLAYER_BOWLER
        dbCursor=db.rawQuery("select * from " + SQLiteDBManager.T301_PLAYER_BOWLER
                + " where " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID,null);
        if (dbCursor.getCount() == 0 )
        {
            dbClass.InsertDataToT301(CurrentBowlerID,Integer.parseInt(matchID),2,0,0,0,0,0,0,db);
        }
        dbCursor.close();

        // Insert Data to T302_PLAYER_BATSMEN
        dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_TYPE_ID + " from " + SQLiteDBManager.T002_PLAYERS
                + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + nextBatsmenID,null);
        if ( dbCursor.getCount()>0)
        {
            dbCursor.moveToNext();
            dbClass.InsertDataToT302(nextBatsmenID,Integer.parseInt(matchID),dbCursor.getInt(0),0,0,0,0,0,0,0,0,db);
        }
        else
        {
            dbClass.InsertDataToT302(nextBatsmenID,Integer.parseInt(matchID),1,0,0,0,0,0,0,0,0,db);
        }
        dbCursor.close();

        // Insert Data to T005_OVER_BY_BALL_STATS and T006_WICKETS
        if (wicket_count <9)
        {
            Long response=dbClass.InsertDataToT005(Integer.parseInt(matchID),Integer.parseInt(BattingTeamID),Integer.parseInt(BowlingTeamID),
                    StrikerBatsmenID,CurrentBowlerID,NonStrikerBatsmenID,overnum,current_ball_number,AdditionalRuns,"Y","Y",db);
            dbClass.InsertDataToT006(Integer.parseInt(Long.toString(response)),CurrentBowlerID,"Y",WicketBattID,FielderID,
                    StrikerBatsmenID,NonStrikerBatsmenID,"N",WicketType,db);
            this.end_innings_flag=false;
        } else
        {
            Long response=dbClass.InsertDataToT005(Integer.parseInt(matchID),Integer.parseInt(BattingTeamID),Integer.parseInt(BowlingTeamID),
                    StrikerBatsmenID,CurrentBowlerID,NonStrikerBatsmenID,overnum,current_ball_number,AdditionalRuns,"Y","Y",db);
            dbClass.InsertDataToT006(Integer.parseInt(Long.toString(response)),CurrentBowlerID,"Y",WicketBattID,FielderID,
                    StrikerBatsmenID,NonStrikerBatsmenID,"Y",WicketType,db);
            this.end_innings_flag=true;
        }

        // Add Additional Runs to the Bowler.
        if (AdditionalRuns == 0) {
            db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER
                    + " set " + SQLiteDBManager.COL_T301_BALLS_BOWLED + "=" + SQLiteDBManager.COL_T301_BALLS_BOWLED + "+1"
                    + " , " + SQLiteDBManager.COL_T301_DOT_BALLS + "=" + SQLiteDBManager.COL_T301_DOT_BALLS + "+1"
                    + " where " + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID
                    + " and " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID);
        }
        else
        {
            db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER
                    + " set " + SQLiteDBManager.COL_T301_BALLS_BOWLED + "=" + SQLiteDBManager.COL_T301_BALLS_BOWLED + "+1"
                    + " , " + SQLiteDBManager.COL_T301_RUNS_CONCEEDED + "=" + bowler_run
                    + " where " + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID
                    + " and " + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID);
        }
        dbCursor.close();

        // Update T302_PLAYER_BATSMEN
        db.execSQL("update " + SQLiteDBManager.T302_PLAYER_BATSMEN + " set "
                + " " + SQLiteDBManager.COL_T302_BALLS_FACED + "=" + SQLiteDBManager.COL_T302_BALLS_FACED + "+1"
                + ", " + SQLiteDBManager.COL_T302_DOT_BALLS + "=" + SQLiteDBManager.COL_T302_DOT_BALLS + "+1"
                + ", " + SQLiteDBManager.COL_T302_RUNS_SCORED + "=" + SQLiteDBManager.COL_T302_RUNS_SCORED + "+" + AdditionalRuns
                + ", " + SQLiteDBManager.COL_T302_WICKET_BOWLER_ID + "=" + CurrentBowlerID
                + ", " + SQLiteDBManager.COL_T302_WICKET_PLAYER_ID + "=" + FielderID
                + ", " + SQLiteDBManager.COL_T302_PLAYER_STATUS_ID + "="
                + "(select " + SQLiteDBManager.COL_T996_WICKET_TYPE_ID + " from " + SQLiteDBManager.T996_WICKET_TYPE
                + " where " + SQLiteDBManager.COL_T996_WICKET_TYPE_NAME + "='" + WicketType + "'" + ")"
                + " where " + SQLiteDBManager.COL_T302_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T302_PLAYER_ID + "=" + WicketBattID);

        if ("Caught and Bowl".equals(WicketType) || "LBW".equals(WicketType) || "Bowled".equals(WicketType) || "Hit Wicket".equals(WicketType))
        {
            // Update T301_PLAYER_BOWLER
            db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER + " set "
                    + SQLiteDBManager.COL_T301_WICKETS + "=" + SQLiteDBManager.COL_T301_WICKETS + "+1, "
                    + SQLiteDBManager.COL_T301_DOT_BALLS + "=" + SQLiteDBManager.COL_T301_DOT_BALLS + "+1 where "
                    + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID + " and "
                    + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID);
            CurrentBowlerDots=CurrentBowlerDots+1;
        }else if ("Caught Behind".equals(WicketType) || "Stumped".equals(WicketType))
        {
            CurrentBowlerDots=CurrentBowlerDots+1;
            // Update T301_PLAYER_BOWLER
            db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER + " set "
                    + SQLiteDBManager.COL_T301_WICKETS + "=" + SQLiteDBManager.COL_T301_WICKETS + "+1, "
                    + SQLiteDBManager.COL_T301_DOT_BALLS + "=" + SQLiteDBManager.COL_T301_DOT_BALLS + "+1 where "
                    + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID + " and "
                    + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID);
            // Update WK Details.
            dbCursor=db.rawQuery("select * from " + SQLiteDBManager.T303_PLAYER_WK
                    + " where " + SQLiteDBManager.COL_T303_PLAYER_ID + "=" + FielderID
                    + " and " + SQLiteDBManager.COL_T303_MATCH_ID + "=" + matchID,null);
            if(dbCursor.getCount()==0)
            {
                dbClass.InsertDataToT303(FielderID,Integer.parseInt(matchID),4,0,0,0,db);
            }
            dbCursor.close();
            if ("Caught Behind".equals(WicketType)) {
                db.execSQL("update " + SQLiteDBManager.T303_PLAYER_WK + " set "
                        + SQLiteDBManager.COL_T303_CAUGHT_BEHIND + "=" + SQLiteDBManager.COL_T303_CAUGHT_BEHIND + "+1 where "
                        + SQLiteDBManager.COL_T303_MATCH_ID + "=" + matchID + " and "
                        + SQLiteDBManager.COL_T303_PLAYER_ID + "=" + FielderID);
            }
            else {
                db.execSQL("update " + SQLiteDBManager.T303_PLAYER_WK + " set "
                        + SQLiteDBManager.COL_T303_STUMPED + "=" + SQLiteDBManager.COL_T303_STUMPED + "+1 where "
                        + SQLiteDBManager.COL_T303_MATCH_ID + "=" + matchID + " and "
                        + SQLiteDBManager.COL_T303_PLAYER_ID + "=" + FielderID);
            }
        } else if ("Run Out".equals(WicketType)) {
            // Update or Insert Data to T301_PLAYER_BOWLER -- no wicket to the bowler
            if (AdditionalRuns == 0) {
                db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER + " set "
                        + SQLiteDBManager.COL_T301_DOT_BALLS + "=" + SQLiteDBManager.COL_T301_DOT_BALLS + "+1 where "
                        + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID + " and "
                        + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID);
                CurrentBowlerDots=CurrentBowlerDots+1;
            }
            else
            {
                CurrentBowlerDots=0;
            }
            // Update or Insert Data to T304_PLAYER_FIELDER OR T303_PLAYER_WK
            dbCursor=db.rawQuery("select * from " + SQLiteDBManager.T303_PLAYER_WK + " where "
                    + SQLiteDBManager.COL_T303_PLAYER_ID + "=" + FielderID + " and "
                    + SQLiteDBManager.COL_T303_MATCH_ID + "=" + matchID,null);
            if (dbCursor.getCount()==1) {
                // Update T303
                db.execSQL("update " + SQLiteDBManager.T303_PLAYER_WK + " set "
                        + SQLiteDBManager.COL_T303_RUN_OUTS + "=" + SQLiteDBManager.COL_T303_RUN_OUTS + "+1 where "
                        + SQLiteDBManager.COL_T303_PLAYER_ID + "=" + FielderID + " and "
                        + SQLiteDBManager.COL_T303_MATCH_ID + "=" + matchID);
            }
            else {
                scoreCursor=db.rawQuery("select * from " + SQLiteDBManager.T304_PLAYER_FIELDER + " where "
                        + SQLiteDBManager.COL_T304_PLAYER_ID + "=" + FielderID + " and "
                        + SQLiteDBManager.COL_T304_MATCH_ID + "=" + matchID,null);
                if ( scoreCursor.getCount()==1){
                    // Update T304
                    db.execSQL("update " + SQLiteDBManager.T304_PLAYER_FIELDER + " set "
                            + SQLiteDBManager.COL_T304_RUN_OUTS + "=" + SQLiteDBManager.COL_T304_RUN_OUTS + "+1 where "
                            + SQLiteDBManager.COL_T304_PLAYER_ID + "=" + FielderID + " and "
                            + SQLiteDBManager.COL_T304_MATCH_ID + "=" + matchID);
                }
                else {
                    // Insert data to T304
                    dbClass.InsertDataToT304(FielderID,Integer.parseInt(matchID),3,0,0,db);
                    // Update T304
                    db.execSQL("update " + SQLiteDBManager.T304_PLAYER_FIELDER + " set "
                            + SQLiteDBManager.COL_T304_RUN_OUTS + "=" + SQLiteDBManager.COL_T304_RUN_OUTS + "+1 where "
                            + SQLiteDBManager.COL_T304_PLAYER_ID + "=" + FielderID + " and "
                            + SQLiteDBManager.COL_T304_MATCH_ID + "=" + matchID);
                }
            }
            dbCursor.close();
            scoreCursor.close();
        } else if ("Caught".equals(WicketType))
        {
            CurrentBowlerDots=CurrentBowlerDots+1;
            // Update T301_PLAYER_BOWLER
            db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER + " set "
                    + SQLiteDBManager.COL_T301_WICKETS + "=" + SQLiteDBManager.COL_T301_WICKETS + "+1, "
                    + SQLiteDBManager.COL_T301_DOT_BALLS + "=" + SQLiteDBManager.COL_T301_DOT_BALLS + "+1 where "
                    + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID + " and "
                    + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID);
            // Update or Insert Data to T304_PLAYER_FIELDER
            dbCursor=db.rawQuery("select * from " + SQLiteDBManager.T304_PLAYER_FIELDER
                    + " where " + SQLiteDBManager.COL_T304_PLAYER_ID + "=" + FielderID
                    + " and " + SQLiteDBManager.COL_T304_MATCH_ID + "=" + matchID,null);
            if(dbCursor.getCount()==0)
            {
                dbClass.InsertDataToT304(FielderID,Integer.parseInt(matchID),3,0,0,db);
            }
            dbCursor.close();
            db.execSQL("update " + SQLiteDBManager.T304_PLAYER_FIELDER + " set "
                    + SQLiteDBManager.COL_T304_CATCHES + "=" + SQLiteDBManager.COL_T304_CATCHES + "+1 where "
                    + SQLiteDBManager.COL_T304_MATCH_ID + "=" + matchID + " and "
                    + SQLiteDBManager.COL_T304_PLAYER_ID + "=" + FielderID);
        } else
        {
            CurrentBowlerDots=CurrentBowlerDots+1;
            // Insert Data to T301_PLAYER_BOWLER - but no wicket to the bowler.
            if (AdditionalRuns == 0) {
                db.execSQL("update " + SQLiteDBManager.T301_PLAYER_BOWLER + " set "
                        + SQLiteDBManager.COL_T301_DOT_BALLS + "=" + SQLiteDBManager.COL_T301_DOT_BALLS + "+1 where "
                        + SQLiteDBManager.COL_T301_PLAYER_ID + "=" + CurrentBowlerID + " and "
                        + SQLiteDBManager.COL_T301_MATCH_ID + "=" + matchID);
            }
        }
        // Populate Screen.
        if (StrikerBatsmenID==WicketBattID)
        {
            dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                    + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + nextBatsmenID, null);
            dbCursor.moveToNext();
            this.StrikeBatsmenName.setText(dbCursor.getString(0));
            dbCursor.close();
            this.StrikeBatsmenRuns.setText("0");
            this.StrikeBatsmenBalls.setText("0");
            this.StrikeBatsmenFours.setText("0");
            this.StrikeBatsmenSixes.setText("0");
            this.StrikeBatsmenStrikeRate.setText("0");
            StrikerBatsmenID = nextBatsmenID;
        }
        else if (NonStrikerBatsmenID==WicketBattID)
        {
            dbCursor=db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + " from " + SQLiteDBManager.T002_PLAYERS
                    + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + nextBatsmenID,null);
            dbCursor.moveToNext();
            this.NonStrikeBatsmenName.setText(dbCursor.getString(0));
            dbCursor.close();
            this.NonStrikeBatsmenRuns.setText("0");
            this.NonStrikeBatsmenBalls.setText("0");
            this.NonStrikeBatsmenFours.setText("0");
            this.NonStrikeBatsmenSixes.setText("0");
            this.NonStrikeBatsmenStrikeRate.setText("0");
            NonStrikerBatsmenID=nextBatsmenID;
        }
        current_ball_number = legal_balls_delivered % 6;
        current_ball_number = current_ball_number + 1;
        runs_scored=runs_scored + AdditionalRuns;
        if ( current_ball_number == 0 )
        {
            first_inning_over_display = first_inning_over_display+1;
            this.FirstBattingTeamScore.setText(runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")");
        }
        else
        {
            this.FirstBattingTeamScore.setText(runs_scored + "/" + wickets_first_inning + " (" + first_inning_over_display + "." + current_ball_number + ")");
        }

        if (crossed)
        {
            switch_batsmen();
        }
        if (current_ball_number == 0)
        {
            switch_batsmen();
        }
        CheckEndOfInnings();
    }

    public void ExtrasfromFragment ( int addRuns, boolean crossed, String type )
    {
        // TODO: next batsmen
        System.out.println("ScoreCardFragment: Extras input received - " + addRuns + "," + crossed + "," + type);
    }

    private void AreYouReady()
    {
        getInitialPlayers(Integer.parseInt(BattingTeamID), Integer.parseInt(BowlingTeamID));
    }

    private void setBattNBowlTeams(String BattTeam, String BowlTeam, String BattTeamName, String BowlTeamName)
    {
        this.BattingTeamID = BattTeam;
        this.BowlingTeamID = BowlTeam;
        this.FirstBattingTeamName.setText(BattTeamName);
        this.SecondBattingTeamName.setText(BowlTeamName);
    }

    private void getNextBowler(int bowlTeamID)
    {
        System.out.println("ScoreCardFragment - GetNextBowler");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = GetNewBowlerFragment.newInstance();
        newFragment.setTargetFragment(this,0);
        Bundle args = new Bundle();
        args.putInt("bowlTeamID", bowlTeamID);
        args.putInt("currentBowlerID", CurrentBowlerID);
        newFragment.setArguments(args);
        newFragment.setCancelable(false);
        newFragment.show(ft, "dialog");
        if (newFragment.getDialog() !=null )
        {
            newFragment.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            newFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void wicket_has_fallen(final int striker, final int nonstriker,final String wicketType)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = WicketBottomSheetFragment.newInstance();
        newFragment.setTargetFragment(this,0);
        Bundle args = new Bundle();
        args.putInt("striker", striker);
        args.putInt("nonstriker", nonstriker);
        args.putInt("battTeamID",Integer.parseInt(BattingTeamID));
        args.putString("wicketType",wicketType);
        args.putInt("bowlTeamID",Integer.parseInt(BowlingTeamID));
        args.putInt("matchID",Integer.parseInt(matchID));
        newFragment.setArguments(args);
        newFragment.setCancelable(false);
        newFragment.show(ft, "dialog");
        if (newFragment.getDialog() !=null )
        {
            newFragment.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            newFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void getInitialPlayers(int battid, int bowlid)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = GetInitialPlayersFragment.newInstance();
        newFragment.setTargetFragment(this,0);
        Bundle args = new Bundle();
        args.putInt("battID", battid);
        args.putInt("bowlID", bowlid);
        newFragment.setArguments(args);
        newFragment.setCancelable(false);
        newFragment.show(ft, "dialog");
        if (newFragment.getDialog() !=null )
        {
            newFragment.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            newFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void extrarun(String type)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = ExtrasFragment.newInstance();
        newFragment.setTargetFragment(this,0);
        Bundle args = new Bundle();
        args.putString("type", type);
        newFragment.setArguments(args);
        newFragment.setCancelable(false);
        newFragment.show(ft, "dialog");
        if (newFragment.getDialog() !=null )
        {
            newFragment.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            newFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void CheckEndOfInnings()
    {
        if (end_innings_flag)
        {
            if("Test".equals(MatchType))
            {
                //TODO: Test Match functionality.
            }
            else if(currentInningNumber==1)
            {
                currentInningNumber=2;
                this.RecentBalls.setText("");
                runs_scored=0;
                // Swap Bowling & Batting Team ID.
                String dummy=BattingTeamID;
                BattingTeamID=BowlingTeamID;
                BowlingTeamID=dummy;

                StrikerBatsmenID=0;
                NonStrikerBatsmenID=0;
                CurrentBowlerID=0;

                db.execSQL("update " + SQLiteDBManager.T003_MATCHES + " set " + SQLiteDBManager.COL_T003_INNING_NUMBER + "=" + currentInningNumber
                        + " where " + SQLiteDBManager.COL_T003_MATCH_ID + "=" + matchID);

                //Initialize submit_button
                pending_inning_button.setText("Click to Start next Innings");
                pending_inning_button.setWidth((int) (312 * scale + 0.5f));
                pending_inning_button.setHeight((int) (52 * scale + 0.5f));
                pending_inning_button.setElevation(2);
                pending_inning_button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_primary));
                pending_inning_button.setTextColor(ContextCompat.getColor(getContext(),R.color.white_color));
                pending_inning_button.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(16,16,16,16);
                pending_second_innings_selection=true;

                bottom_layout.addView(pending_inning_button,params);
                pending_inning_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AreYouReady();
                    }
                });
            }
            else if (currentInningNumber==2)
            {
                // TODO: create a new fragment for end of match
            }
        }
    }
}
