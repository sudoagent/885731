package com.sudoinc.cricketscorer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;

import java.util.ArrayList;
import java.util.List;

public class NewMatchActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce=false;
    private String[] tossDecisionArray = {"Select Decision","Bat first","Bowl first"};
    private List<String> TossTeamArray = new ArrayList<String>();
    private MaterialButton startMatchButton;
    private MaterialButton cancelButton;
    private AutoCompleteTextView homeTeamView;
    private AutoCompleteTextView awayTeamView;
//    private AutoCompleteTextView matchTypeView;
//    private AutoCompleteTextView oversView;

    private Spinner tossWinner;
    private Spinner tossDecision;
    private Spinner matchTypeView;

    private SQLiteDBManager dbClass;
    private SQLiteDatabase db;

    private ArrayList homeTeamArray = new ArrayList();
    private ArrayList awayTeamArray = new ArrayList();
    private List<String> matchTypeArray = new ArrayList<String>();
    private ArrayList oversArray = new ArrayList();
    private ArrayList homeTeamIDArray = new ArrayList();
    private ArrayList awayTeamIDArray = new ArrayList();
    private ArrayList matchTypeIDArray = new ArrayList();

//    private String queryString;
    private String selectedMyTeam;
    private String selectedOppTeam;
    private String selectedMatchType;
    private String selectedOvers;
    private String selectedMyTeamID;
    private String selectedOppTeamID;
    private String selectedMatchTypeID;
    private String matchResult=null;

    private final String resumeOrNew="New";
    private Long matchID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        this.dbClass = new SQLiteDBManager(this);
        this.db = new SQLiteDBManager(this).getWritableDatabase();

        this.startMatchButton = findViewById(R.id.startNewMatch);
        this.cancelButton = findViewById(R.id.cancelNewMatch);
        this.homeTeamView = findViewById(R.id.HomeTeam);
        this.awayTeamView = findViewById(R.id.AwayTeam);
        this.matchTypeView = findViewById(R.id.matchType);
//        this.oversView = findViewById(R.id.Overs);
        this.tossWinner = findViewById(R.id.TossWinner);
        this.tossDecision = findViewById(R.id.TossDecision);

//        tossDecisionArray.add("Decision");
//        tossDecisionArray.add("Bat first");
//        tossDecisionArray.add("Bowl first");
        TossTeamArray.clear();

        populateDropdowns();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        startMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !TextUtils.isEmpty(homeTeamView.getText().toString())
                        && !TextUtils.isEmpty(awayTeamView.getText().toString())
                        && !"Match Type".equals(matchTypeView.getSelectedItem().toString())
                    && !"Toss winner".equals(tossWinner.getSelectedItem().toString())
                    && !"Select Decision".equals(tossDecision.getSelectedItem().toString()))
                {

                    if (!homeTeamArray.contains(homeTeamView.getText().toString()))
                    {
                        System.out.println("Home Team Value not found");
                        Long HomeTeamResponse;
                        HomeTeamResponse=dbClass.InsertDataToT001(homeTeamView.getText().toString(),1,db);
                        selectedMyTeamID=HomeTeamResponse.toString();
                        selectedMyTeam=homeTeamView.getText().toString();
                    }
                    if (!awayTeamArray.contains(awayTeamView.getText().toString()))
                    {
                        System.out.println("Away Team Value not found");
                        Long AwayTeamResponse;
                        AwayTeamResponse=dbClass.InsertDataToT001(awayTeamView.getText().toString(),2,db);
                        selectedOppTeamID=AwayTeamResponse.toString();
                        selectedOppTeam=awayTeamView.getText().toString();
                    }
                    startMatch();
                }
                else
                {
                    Toast makeToast = Toast.makeText(getApplicationContext(),"Some inputs are empty",Toast.LENGTH_SHORT);
                    makeToast.show();
                }
            }
        });

        ArrayAdapter<String> tossDecisionAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, tossDecisionArray){
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v;
                if ( position ==0 )
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
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
            @Override
            public int getCount() {
                return(tossDecisionArray.length); // Truncate the list
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(Color.parseColor("#212121"));
                view.setTextSize(16);
                return view;
            }
        };
        tossDecisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tossDecision.setAdapter(tossDecisionAdapter);
        tossDecision.setSelection(0);

        homeTeamView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (tossWinner.getSelectedItemId()==0) {
                        PopulateTossWinner();
                    }
                }
            }
        });

        awayTeamView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (tossWinner.getSelectedItemId()==0) {
                        PopulateTossWinner();
                    }
                }
            }
        });

        tossWinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PopulateTossWinner();
                return false;
            }
        });
    }

    interface GetText {
        void myText (String output);
    }

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce=true;

        new MaterialAlertDialogBuilder(this,android.R.style.Theme_Material_Light_Dialog_NoActionBar)
                .setTitle("Cancel Match")
                .setMessage("Are you sure you want to cancel this match?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    void getOversInput(String title, String message, final GetText getText)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog_NoActionBar)
                .setPositiveButton(R.string.ok,null)
                .create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        final EditText oversInput = new EditText(this);
        oversInput.setTextColor(getResources().getColor(R.color.primary_text_color,null));
        oversInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        oversInput.setLayoutParams(lp);
        alertDialog.setView(oversInput);
        alertDialog.setCancelable(false);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("oversInput is " + oversInput.getText().toString());
                        if (TextUtils.isEmpty(oversInput.getText().toString()) || "0".equals(oversInput.getText().toString()) )
                        {
                            oversInput.setError("Input cannot be empty or zero");
                        }
                        else
                        {
                            getText.myText(oversInput.getText().toString());
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void startMatch()
    {
        System.out.println("StartMatch Values : " + selectedMyTeam + " with ID - " + selectedMyTeamID
                + ", " + selectedOppTeam + " with ID - " + selectedOppTeamID
                + ", " + selectedMatchType + " with ID - " + selectedMatchTypeID + " and overs - " + selectedOvers
                + ", Toss won by - " + tossWinner.getSelectedItem().toString() + ". Decision made to - " + tossDecision.getSelectedItem().toString());
        String tossWinnerID;
        if (selectedMyTeam.equals(tossWinner.getSelectedItem().toString()))
        {
            tossWinnerID=selectedMyTeamID;
            this.matchID = dbClass.InsertDataToT003(Integer.parseInt(selectedMatchTypeID),1,Integer.parseInt(selectedOvers), 1,0,0,Integer.parseInt(selectedMyTeamID),Integer.parseInt(selectedOppTeamID),Integer.parseInt(selectedMyTeamID),tossDecision.getSelectedItem().toString(),matchResult,db);
        }
        else
        {
            tossWinnerID=selectedOppTeamID;
            this.matchID = dbClass.InsertDataToT003(Integer.parseInt(selectedMatchTypeID),1,Integer.parseInt(selectedOvers), 1,0,0,Integer.parseInt(selectedMyTeamID),Integer.parseInt(selectedOppTeamID),Integer.parseInt(selectedOppTeamID),tossDecision.getSelectedItem().toString(),matchResult,db);
        }
        Intent ScoreSheetIntent = new Intent(this, ScoreSheetActivity.class);
        ScoreSheetIntent.putExtra("matchID",matchID.toString());
        ScoreSheetIntent.putExtra("selectedMatchType",selectedMatchType);
        ScoreSheetIntent.putExtra("selectedMyTeamID",selectedMyTeamID);
        ScoreSheetIntent.putExtra("selectedOppTeamID",selectedOppTeamID);
        ScoreSheetIntent.putExtra("tossWinnerID",tossWinnerID);
        ScoreSheetIntent.putExtra("tossDecision",tossDecision.getSelectedItem().toString());
        ScoreSheetIntent.putExtra("selectedOvers",selectedOvers);
        ScoreSheetIntent.putExtra("ResumeOrNew",resumeOrNew);
        startActivity(ScoreSheetIntent);
        finish();
    }

    public void populateDropdowns()
    {
        Cursor dbCursor;

        dbCursor = this.db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_ID + "," + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM,null);
        while (dbCursor.moveToNext())
        {
            homeTeamIDArray.add(dbCursor.getString(0));
            homeTeamArray.add(dbCursor.getString(1));
        }
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T001_TEAM_ID + "," + SQLiteDBManager.COL_T001_TEAM_NAME + " from " + SQLiteDBManager.T001_TEAM,null);
        while (dbCursor.moveToNext())
        {
            awayTeamIDArray.add(dbCursor.getString(0));
            awayTeamArray.add(dbCursor.getString(1));
        }
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T999_OVERS_MAX + "," + SQLiteDBManager.COL_T999_TOURNAMENT_FORMAT + ","
                + SQLiteDBManager.COL_T999_TOURNAMENT_TYPE_ID + " from " + SQLiteDBManager.T999_TOURNAMENT_TYPE,null);
        while (dbCursor.moveToNext())
        {
            oversArray.add(dbCursor.getString(0));
            matchTypeArray.add(dbCursor.getString(1));
            matchTypeIDArray.add(dbCursor.getString(2));
        }
        dbCursor.close();
        matchTypeArray.add("Match Type");

        ArrayAdapter<String> hometeamadapter = new ArrayAdapter<String>(this, R.layout.textview_autocomplete_customlayout,homeTeamArray);
        homeTeamView.setAdapter(hometeamadapter);
        homeTeamView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                homeTeamView.showDropDown();
                return false;
            }
        });

        homeTeamView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMyTeam=homeTeamArray.get(i).toString();
                selectedMyTeamID=homeTeamIDArray.get(i).toString();
                if (!TextUtils.isEmpty(homeTeamView.getText().toString()) && !TextUtils.isEmpty(awayTeamView.getText().toString()))
                {
                    PopulateTossWinner();
                }
            }
        });

        ArrayAdapter<String> awayteamadapter = new ArrayAdapter<String>(this, R.layout.textview_autocomplete_customlayout,awayTeamArray);
        awayTeamView.setAdapter(awayteamadapter);
        awayTeamView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                awayTeamView.showDropDown();
                return false;
            }
        });

        awayTeamView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOppTeam=awayTeamArray.get(i).toString();
                selectedOppTeamID=awayTeamIDArray.get(i).toString();
                if (!TextUtils.isEmpty(homeTeamView.getText().toString()) && !TextUtils.isEmpty(awayTeamView.getText().toString()))
                {
                    PopulateTossWinner();
                }
            }
        });

        ArrayAdapter<String> matchTypeAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_item_selected, matchTypeArray){
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v;
                if ( position == matchTypeArray.size()-1 )
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
                return(matchTypeArray.size());
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(Color.parseColor("#212121"));
                view.setTextSize(16);
                return view;
            }
        };
        matchTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        matchTypeView.setAdapter(matchTypeAdapter);
        matchTypeView.setSelection(matchTypeArray.size()-1);
        System.out.println("NewMatchActivity - after matchTypeView set - " + matchTypeArray.size());

        matchTypeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if ( i < matchTypeArray.size()-1 ) {
                    selectedMatchTypeID = matchTypeIDArray.get(i).toString();
                    selectedMatchType = matchTypeArray.get(i).toString();
                    if ("Other".equals(parent.getItemAtPosition(i).toString())) {
                        getOversInput("Number of Overs", "Enter overs: ",
                                new GetText() {
                                    @Override
                                    public void myText(String output) {
                                        selectedOvers = output;
                                    }
                                });
                    } else {
                        selectedOvers = oversArray.get(i).toString();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });
    }

    private void PopulateTossWinner()
    {
        if (!TextUtils.isEmpty(homeTeamView.getText().toString()) && !TextUtils.isEmpty(awayTeamView.getText().toString()))
        {
            TossTeamArray.clear();
            TossTeamArray.add("Toss winner");
            TossTeamArray.add(homeTeamView.getText().toString());
            TossTeamArray.add(awayTeamView.getText().toString());

            ArrayAdapter<String> tossWinnerAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_item_selected, TossTeamArray){
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v;
                    if ( position ==0 )
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
                    parent.setVerticalScrollBarEnabled(false);
                    return v;
                }
                @Override
                public int getCount() {
                    return(TossTeamArray.size()); // Truncate the list
                }
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextColor(Color.parseColor("#212121"));
                    view.setTextSize(16);
                    return view;
                }
            };
            tossWinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tossWinnerAdapter.notifyDataSetChanged();
            tossWinner.setAdapter(tossWinnerAdapter);
            tossWinner.setSelection(0);
            tossWinnerAdapter.notifyDataSetChanged();
        }
    }
}
