package com.sudoinc.cricketscorer.commons.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.sudoinc.cricketscorer.R;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;

import java.util.ArrayList;

public class WicketBottomSheetFragment extends BottomSheetDialogFragment
{
    // Initializing Variables
    private LinearLayout wicket_layout;

    private boolean BatsmenCrossed;
    private boolean ValidatefieldInput;

    private int nextBatsmenID;
    private int BattingTeamID;
    private int BowlingTeamID;
    private int StrikeBatsmenID;
    private int NonStrikeBatsmenID;
    private int WicketPlayerID;
    private int FielderID;
    private int FielderTypeID;
    private int AdditionalRuns;
    private int matchID;
    private String WicketType;

    private ArrayList BattingTeamArray = new ArrayList();
    private ArrayList BattingTeamIDArray = new ArrayList();
    private ArrayList fielderArray = new ArrayList();
    private ArrayList fielderIDArray = new ArrayList();

    private SQLiteDBManager dbClass;
    private SQLiteDatabase db;
    private Cursor dbCursor;
    private OnWicketBottomSheetFragmentCompleteListener onCompleteListener;

    public WicketBottomSheetFragment() {
        // Required empty public constructor
    }

    public static WicketBottomSheetFragment newInstance() {
        WicketBottomSheetFragment f = new WicketBottomSheetFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_wicket_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.dbClass = new SQLiteDBManager(getContext());
        this.db = dbClass.getWritableDatabase();

        this.wicket_layout = view.findViewById(R.id.linearlayout_wicket);
        this.BattingTeamID=getArguments().getInt("battTeamID");
        this.StrikeBatsmenID=getArguments().getInt("striker");
        this.NonStrikeBatsmenID=getArguments().getInt("nonstriker");
        this.WicketType=getArguments().getString("wicketType");
        this.BowlingTeamID=getArguments().getInt("bowlTeamID");
        this.matchID=getArguments().getInt("matchID");

        this.ValidatefieldInput=false;
        this.nextBatsmenID = 0;
        this.FielderID=0;

        WicketPlayerID=0;
        AdditionalRuns=0;

        final AutoCompleteTextView nextBatsmenView = (AutoCompleteTextView) getLayoutInflater().inflate(R.layout.autocompletetextview_template,null);
        final AutoCompleteTextView fielderNameView = (AutoCompleteTextView) getLayoutInflater().inflate(R.layout.autocompletetextview_template,null);
        final AutoCompleteTextView addRunsTextView = (AutoCompleteTextView) getLayoutInflater().inflate(R.layout.autocompletetextview_template,null);
        final RadioGroup BatsmenGroup = new RadioGroup((getContext()));
        final RadioButton StrikeBatsmenRadio = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button_template,null);
        final RadioButton NonStrikeBatsmenRadio = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button_template,null);
        final CheckBox BatsmenCrossCheckbox = (CheckBox) getLayoutInflater().inflate(R.layout.checkbox_template,null);
        final TextView textview_topline = new TextView(getContext());
        final MaterialButton submit_button = (MaterialButton) getLayoutInflater().inflate(R.layout.materialbutton_template,null);

        //dps conversion
        final float scale = getContext().getResources().getDisplayMetrics().density;

        // Default Param Initialization
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16,16,16,16);

        //Top Textview Initialization
        textview_topline.setText("Player who got out:");
        textview_topline.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textview_topline.setTextColor(ContextCompat.getColor(getContext(),R.color.primary_text_color));

        // Initialization for nextBatsmenView
        nextBatsmenView.setHint("Next Batsmen");
        nextBatsmenView.setLayoutParams(params);
        nextBatsmenView.setHintTextColor(ContextCompat.getColor(getContext(),R.color.secondary_text_color));
        nextBatsmenView.setTextColor(ContextCompat.getColor(getContext(),R.color.primary_text_color));
        nextBatsmenView.setDropDownHeight((int) (200 * scale + 0.5f));
        nextBatsmenView.setDropDownVerticalOffset(-(int) (208 * scale + 0.5f));

        // Initialization for fielderNameView
        fielderNameView.setHint("Fielder Name");
        fielderNameView.setLayoutParams(params);
        fielderNameView.setHintTextColor(ContextCompat.getColor(getContext(),R.color.secondary_text_color));
        fielderNameView.setTextColor(ContextCompat.getColor(getContext(),R.color.primary_text_color));
        fielderNameView.setDropDownHeight((int) (200 * scale + 0.5f));
        fielderNameView.setDropDownVerticalOffset(-(int) (208 * scale + 0.5f));

        // Initialization for addRunsTextView
        addRunsTextView.setHint("Additional Runs Scored");
        addRunsTextView.setLayoutParams(params);
        addRunsTextView.setHintTextColor(ContextCompat.getColor(getContext(),R.color.secondary_text_color));
        addRunsTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.primary_text_color));
        addRunsTextView.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Initialize Radio Buttons
        StrikeBatsmenRadio.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        StrikeBatsmenRadio.setId(0);
        NonStrikeBatsmenRadio.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        NonStrikeBatsmenRadio.setId(0+1);
        //Initialize RadioGroup
        BatsmenGroup.addView(StrikeBatsmenRadio);
        BatsmenGroup.addView(NonStrikeBatsmenRadio);

        //Initialize CheckBox
        BatsmenCrossCheckbox.setText("Batsmen Crossed over ?");
        BatsmenCrossCheckbox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        //Initialize submit_button
        submit_button.setText("SUBMIT");
        submit_button.setWidth((int) (312 * scale + 0.5f));
        submit_button.setHeight((int) (52 * scale + 0.5f));
        submit_button.setElevation(2);
        submit_button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_primary));
        submit_button.setTextColor(ContextCompat.getColor(getContext(),R.color.white_color));
        submit_button.setGravity(Gravity.CENTER);


        if ("Caught and Bowl".equals(WicketType) || "LBW".equals(WicketType) || "Bowled".equals(WicketType) || "Hit Wicket".equals(WicketType))
        {
            ValidatefieldInput=true;
            FielderTypeID=1;
            FielderID=0;
            wicket_layout.addView(nextBatsmenView);
            wicket_layout.addView(submit_button);
            WicketPlayerID=StrikeBatsmenID;
        }
        else if ("Caught Behind".equals(WicketType) || "Stumped".equals(WicketType))
        {
            ValidatefieldInput=true;
            wicket_layout.addView(nextBatsmenView);
            wicket_layout.addView(submit_button);
            WicketPlayerID=StrikeBatsmenID;
            dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + "," + SQLiteDBManager.COL_T002_PLAYER_ID
                    + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_TEAM_ID + "=" + BowlingTeamID
                    + " and " + SQLiteDBManager.COL_T002_PLAYER_TYPE_ID + "=4",null);
            System.out.println("WicketBottomSheetFragment: Striker - caughtbehind/stumped - " + dbCursor.getCount());
            if (dbCursor.getCount()>0)
            {
                fielderArray.clear();
                fielderIDArray.clear();
                while (dbCursor.moveToNext())
                {
                    fielderArray.add(dbCursor.getString(0));
                    fielderIDArray.add(dbCursor.getInt(1));
                }
                ArrayAdapter fielderAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_selected, fielderArray);
                fielderNameView.setAdapter(fielderAdapter);
                fielderNameView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (TextUtils.isEmpty(fielderNameView.getText().toString())){
                            fielderNameView.showDropDown();
                        }
                        return false;
                    }
                });
                fielderNameView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        FielderID=Integer.parseInt(fielderIDArray.get(i).toString());
                        fielderNameView.setError(null);
                    }
                });
            }
            dbCursor.close();
        }
        else if ("Run Out".equals(WicketType) || "Caught".equals(WicketType))
        {
            if ("Run Out".equals(WicketType))
            {
                wicket_layout.addView(textview_topline,params);
                wicket_layout.addView(BatsmenGroup,params);
                wicket_layout.addView(BatsmenCrossCheckbox,params);
                wicket_layout.addView(nextBatsmenView);
                wicket_layout.addView(fielderNameView);
                wicket_layout.addView(addRunsTextView);
                wicket_layout.addView(submit_button);
            }
            else
            {
                wicket_layout.addView(nextBatsmenView);
                wicket_layout.addView(fielderNameView);
                wicket_layout.addView(submit_button);
                WicketPlayerID=StrikeBatsmenID;
            }
            ValidatefieldInput=true;
            dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME + "," + SQLiteDBManager.COL_T002_PLAYER_ID
                    + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_TEAM_ID + "=" + BowlingTeamID,null);
            if (dbCursor.getCount()>0)
            {
                fielderArray.clear();
                fielderIDArray.clear();
                while (dbCursor.moveToNext())
                {
                    fielderArray.add(dbCursor.getString(0));
                    fielderIDArray.add(dbCursor.getInt(1));
                }
                ArrayAdapter fielderAdapter = new ArrayAdapter<String>(getContext(), R.layout.textview_autocomplete_customlayout, fielderArray);
                fielderNameView.setAdapter(fielderAdapter);
                fielderNameView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (TextUtils.isEmpty(fielderNameView.getText().toString())){
                            fielderNameView.showDropDown();
                        }
                        return false;
                    }
                });
                fielderNameView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        FielderID=Integer.parseInt(fielderIDArray.get(i).toString());
                        fielderNameView.setError(null);
                    }
                });
            }
            dbCursor.close();
        }
        else
        {
            ValidatefieldInput=false;
            wicket_layout.addView(nextBatsmenView);
            wicket_layout.addView(submit_button);
        }

        BatsmenCrossCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    BatsmenCrossed=true;
                }
                else
                {
                    BatsmenCrossed=false;
                }
            }
        });

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME
                + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + StrikeBatsmenID, null);
        dbCursor.moveToNext();
        StrikeBatsmenRadio.setText(dbCursor.getString(0));
        dbCursor.close();
        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_NAME
                + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_PLAYER_ID + "=" + NonStrikeBatsmenID, null);
        dbCursor.moveToNext();
        NonStrikeBatsmenRadio.setText(dbCursor.getString(0));
        dbCursor.close();

        nextBatsmenView.clearListSelection();
        nextBatsmenView.setText("");
        BattingTeamIDArray.clear();
        BattingTeamArray.clear();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + "," + SQLiteDBManager.COL_T002_PLAYER_NAME
                + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_TEAM_ID + "=" + BattingTeamID
                + " and " + SQLiteDBManager.COL_T002_PLAYER_ID + "<>" + StrikeBatsmenID
                + " and " + SQLiteDBManager.COL_T002_PLAYER_ID + "<>" + NonStrikeBatsmenID
                + " and " + SQLiteDBManager.COL_T002_PLAYER_ID + " not in "
                + "(select " + SQLiteDBManager.COL_T006_WICKET_PLAYER_ID + " from " + SQLiteDBManager.T006_WICKETS
                + " where " + SQLiteDBManager.COL_T006_BALL_ID + " in "
                + "(select " + SQLiteDBManager.COL_T005_BALL_ID + " from " + SQLiteDBManager.T005_OVER_BY_BALL_STATS
                + " where " + SQLiteDBManager.COL_T005_MATCH_ID + "=" + matchID
                + " and " + SQLiteDBManager.COL_T005_BATT_TEAM_ID + "=" + BattingTeamID
                + " and " + SQLiteDBManager.COL_T005_WICKET_FLAG + "='Y'))", null);
        while (dbCursor.moveToNext())
        {
            BattingTeamIDArray.add(dbCursor.getInt(0));
            BattingTeamArray.add(dbCursor.getString(1));
        }
        dbCursor.close();

        ArrayAdapter<String> battingTeamAdapter = new ArrayAdapter<String>(getContext(), R.layout.textview_autocomplete_customlayout, BattingTeamArray);

        nextBatsmenView.setAdapter(battingTeamAdapter);
        nextBatsmenView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (TextUtils.isEmpty(fielderNameView.getText().toString())){
                    nextBatsmenView.showDropDown();
                }
                return false;
            }
        });


        nextBatsmenView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                nextBatsmenID=Integer.parseInt(BattingTeamIDArray.get(i).toString());
                nextBatsmenView.setError(null);
            }
        });

        BatsmenGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = BatsmenGroup.findViewById(checkedId);
                int index = BatsmenGroup.indexOfChild(radioButton);

                if (index == 0)
                {
                    WicketPlayerID=StrikeBatsmenID;
                }
                else
                {
                    WicketPlayerID=NonStrikeBatsmenID;
                }
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCompleteListener = (WicketBottomSheetFragment.OnWicketBottomSheetFragmentCompleteListener) getTargetFragment();
                if (TextUtils.isEmpty(nextBatsmenView.getText())) {
                    nextBatsmenView.setError("Required");
                } else if (("Run Out".equals(WicketType) || "Caught".equals(WicketType)) && TextUtils.isEmpty(fielderNameView.getText())) {
                    fielderNameView.setError("Required");
                } else if (WicketPlayerID == 0) {
                    NonStrikeBatsmenRadio.setError("Required");
                } else if ("Run Out".equals(WicketType) && TextUtils.isEmpty(addRunsTextView.getText())) {
                    addRunsTextView.setError("Required");
                } else {
                    if ("Run Out".equals(WicketType)) {
                        AdditionalRuns = Integer.parseInt(addRunsTextView.getText().toString());
                    } else {
                        AdditionalRuns=0;
                    }
                    if (View.VISIBLE == fielderNameView.getVisibility()  && !fielderIDArray.contains(FielderID))
                    {
                        Long FielderInsertResponse;
                        FielderInsertResponse=dbClass.InsertDataToT002(fielderNameView.getText().toString(),BowlingTeamID,FielderTypeID,db);
                        FielderID=Integer.parseInt(FielderInsertResponse.toString());
                        System.out.println("WicketBottomSheetFragment: New Fielder Created - " + FielderID);
                    }
                    if (BattingTeamArray.contains(nextBatsmenView.getText().toString()))
                    {
                        onCompleteListener.NextBatsmenAfterWicket(WicketPlayerID, nextBatsmenID, BatsmenCrossed, WicketType, FielderID,AdditionalRuns);
                        getDialog().dismiss();
                    } else {
                        Long GetPlayerId;
                        GetPlayerId = dbClass.InsertDataToT002(nextBatsmenView.getText().toString(), BattingTeamID, 2, db);
                        nextBatsmenID = Integer.parseInt(GetPlayerId.toString());
                        onCompleteListener.NextBatsmenAfterWicket(WicketPlayerID, nextBatsmenID, BatsmenCrossed, WicketType, FielderID,AdditionalRuns);
                        getDialog().dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCompleteListener = null;
    }

    public interface OnWicketBottomSheetFragmentCompleteListener {
        void NextBatsmenAfterWicket(int wickBattID,int nextBattID,boolean crossed,String WicketType,int FielderID,int AdditionalRuns);
    }
}
