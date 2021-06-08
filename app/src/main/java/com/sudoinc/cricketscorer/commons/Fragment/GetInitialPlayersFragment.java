package com.sudoinc.cricketscorer.commons.Fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sudoinc.cricketscorer.R;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;

import java.util.ArrayList;

public class GetInitialPlayersFragment extends DialogFragment
{
    // Initializing Variables
    private AutoCompleteTextView openingBatsmenView;
    private AutoCompleteTextView nonStrikingBastmenView;
    private AutoCompleteTextView openingBowlerView;
    private ImageView accept_button_image;

    private int openingBatsmenID;
    private int nonStrikingBatsmenID;
    private int openingBowlerID;
    private int BattingTeamID;
    private int BowlingTeamID;

    private ArrayList BattingTeamArray = new ArrayList();
    private ArrayList BowlingTeamArray = new ArrayList();
    private ArrayList BattingTeamIDArray = new ArrayList();
    private ArrayList BowlingTeamIDArray = new ArrayList();

    private SQLiteDBManager dbClass;
    private SQLiteDatabase db;
    private Cursor dbCursor;

    private OnCompleteListener onCompleteListener;

    public static GetInitialPlayersFragment newInstance() {
        GetInitialPlayersFragment f = new GetInitialPlayersFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.dbClass = new SQLiteDBManager(getContext());
        this.db = dbClass.getWritableDatabase();

        super.onCreate(savedInstanceState);

        this.BattingTeamID = getArguments().getInt("battID");
        this.BowlingTeamID = getArguments().getInt("bowlID");
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_get_initial_players, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.openingBatsmenView = getView().findViewById(R.id.openingBatsmenName);
        this.nonStrikingBastmenView = getView().findViewById(R.id.nonStrikingBatsmenName);
        this.openingBowlerView = getView().findViewById(R.id.currentBowlerName);
        this.accept_button_image = getView().findViewById(R.id.get_initial_players_dialog_button_save);

        this.openingBatsmenID = 0;
        this.nonStrikingBatsmenID = 0;
        this.openingBowlerID = 0;

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + "," + SQLiteDBManager.COL_T002_PLAYER_NAME
                + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_TEAM_ID + "=" + BattingTeamID, null);
        while (dbCursor.moveToNext()) {
            BattingTeamIDArray.add(dbCursor.getInt(0));
            BattingTeamArray.add(dbCursor.getString(1));
        }
        dbCursor.close();

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + "," + SQLiteDBManager.COL_T002_PLAYER_NAME
                + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_TEAM_ID + "=" + BowlingTeamID, null);
        while (dbCursor.moveToNext()) {
            BowlingTeamIDArray.add(dbCursor.getInt(0));
            BowlingTeamArray.add(dbCursor.getString(1));
        }
        dbCursor.close();

        ArrayAdapter<String> battingTeamAdapter = new ArrayAdapter<String>(getContext(), R.layout.textview_autocomplete_customlayout, BattingTeamArray);
        ArrayAdapter<String> bowlingTeamAdapter = new ArrayAdapter<String>(getContext(), R.layout.textview_autocomplete_customlayout, BowlingTeamArray);

        openingBatsmenView.setAdapter(battingTeamAdapter);
        nonStrikingBastmenView.setAdapter(battingTeamAdapter);
        openingBowlerView.setAdapter(bowlingTeamAdapter);

        openingBatsmenView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                openingBatsmenView.showDropDown();
                return false;
            }
        });
        nonStrikingBastmenView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                nonStrikingBastmenView.showDropDown();
                return false;
            }
        });
        openingBowlerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                openingBowlerView.showDropDown();
                return false;
            }
        });

        openingBatsmenView.setVerticalScrollBarEnabled(true);
        nonStrikingBastmenView.setVerticalScrollBarEnabled(true);
        openingBowlerView.setVerticalScrollBarEnabled(true);

        openingBatsmenView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                openingBatsmenID=Integer.parseInt(BattingTeamIDArray.get(i).toString());
                openingBatsmenView.setError(null);
            }
        });

        nonStrikingBastmenView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                nonStrikingBatsmenID=Integer.parseInt(BattingTeamIDArray.get(i).toString());
                nonStrikingBastmenView.setError(null);
            }
        });

        openingBowlerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                openingBowlerID=Integer.parseInt(BowlingTeamIDArray.get(i).toString());
                openingBowlerView.setError(null);
            }
        });

        accept_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCompleteListener = (OnCompleteListener) getTargetFragment();
                System.out.println("Input from Fragment - Inside accept_button");
                if("".equals(openingBatsmenView.getText().toString()))
                {
                    openingBatsmenView.setError("Required field");
                }
                else if ("".equals(nonStrikingBastmenView.getText().toString()))
                {
                    nonStrikingBastmenView.setError("Required field");
                }
                else if ("".equals(openingBowlerView.getText().toString()))
                {
                    openingBowlerView.setError("Required field");
                }
                else
                {
                    if ( BattingTeamArray.contains(openingBatsmenView.getText().toString()) && BattingTeamArray.contains(nonStrikingBastmenView.getText().toString()) && BowlingTeamArray.contains(openingBowlerView.getText().toString()) )
                    {
                        System.out.println("Input from Fragment - Inside accept_button(IF)");
                        onCompleteListener.getInitialPlayerDataFromFragment(openingBatsmenID,nonStrikingBatsmenID,openingBowlerID);
                        getDialog().dismiss();
                    }
                    else
                    {
                        if (!BattingTeamArray.contains(openingBatsmenView.getText().toString())) {
                            Long GetPlayerId;
                            GetPlayerId = dbClass.InsertDataToT002(openingBatsmenView.getText().toString(), BattingTeamID, 1, db);
                            openingBatsmenID = Integer.parseInt(GetPlayerId.toString());
                        }
                        if (!BattingTeamArray.contains(nonStrikingBastmenView.getText().toString())) {
                            Long GetPlayerId;
                            GetPlayerId = dbClass.InsertDataToT002(nonStrikingBastmenView.getText().toString(), BattingTeamID, 1, db);
                            nonStrikingBatsmenID = Integer.parseInt(GetPlayerId.toString());
                        }
                        if (!BowlingTeamArray.contains(openingBowlerView.getText().toString())) {
                            Long GetPlayerId;
                            GetPlayerId = dbClass.InsertDataToT002(openingBowlerView.getText().toString(), BowlingTeamID, 2, db);
                            openingBowlerID = Integer.parseInt(GetPlayerId.toString());
                        }
                        System.out.println("Input from Fragment - Inside accept_button(IF-ELSE) - Created Players" + openingBatsmenID + "," + nonStrikingBatsmenID + "," + openingBowlerID);
                        onCompleteListener.getInitialPlayerDataFromFragment(openingBatsmenID, nonStrikingBatsmenID, openingBowlerID);
                        getDialog().dismiss();
                    }
                }
            }
        });
    }

    private void cancelDialog() {
        // Do Nothing
    }

    public interface OnCompleteListener {
        void getInitialPlayerDataFromFragment(int openingBatID, int nonStrikebattID,int openingBowlID);
    }

//    @Override
//    public void onAttach(Activity activity)
//    {
//        super.onAttach(activity);
//
//        try
//        {
//            onCompleteListener = (OnCompleteListener) activity;
//        }
//        catch (ClassCastException e)
//        {
//            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
//        }
//    }
}
