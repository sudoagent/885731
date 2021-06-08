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

import com.sudoinc.cricketscorer.R;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;

import java.util.ArrayList;

public class GetNewBowlerFragment extends DialogFragment
{
    // Initializing Variables
    private AutoCompleteTextView nextBowlerView;
    private ImageView accept_button_image;

    private int nextBowlerID;
    private int BowlingTeamID;
    private int CurrentBowlerID;

    private boolean doubleBackToExitPressedOnce = false;
    private ArrayList BowlingTeamArray = new ArrayList();
    private ArrayList BowlingTeamIDArray = new ArrayList();

    private SQLiteDBManager dbClass;
    private SQLiteDatabase db;
    private Cursor dbCursor;

    private OnBowlerFragmentCompleteListener onCompleteListener;

    public static GetNewBowlerFragment newInstance() {
        GetNewBowlerFragment f = new GetNewBowlerFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.dbClass = new SQLiteDBManager(getContext());
        this.db = dbClass.getWritableDatabase();

        super.onCreate(savedInstanceState);

        this.BowlingTeamID = getArguments().getInt("bowlTeamID");
        this.CurrentBowlerID = getArguments().getInt("currentBowlerID");
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
        return inflater.inflate(R.layout.fragment_get_newbowler, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.nextBowlerView = getView().findViewById(R.id.nextBowlerName);
        this.accept_button_image = getView().findViewById(R.id.get_next_bowler_dialog_button_save);

        this.nextBowlerID = 0;

        dbCursor = db.rawQuery("select " + SQLiteDBManager.COL_T002_PLAYER_ID + "," + SQLiteDBManager.COL_T002_PLAYER_NAME
                + " from " + SQLiteDBManager.T002_PLAYERS + " where " + SQLiteDBManager.COL_T002_TEAM_ID + "=" + BowlingTeamID, null);
        while (dbCursor.moveToNext())
        {
            if ( dbCursor.getInt(0) != CurrentBowlerID )
            {
                BowlingTeamIDArray.add(dbCursor.getInt(0));
                BowlingTeamArray.add(dbCursor.getString(1));
            }
        }
        dbCursor.close();

        ArrayAdapter<String> bowlingTeamAdapter = new ArrayAdapter<String>(getContext(), R.layout.textview_autocomplete_customlayout, BowlingTeamArray);

//        openingBatsmenView.setAdapter(battingTeamAdapter);
//        nonStrikingBastmenView.setAdapter(battingTeamAdapter);
        nextBowlerView.setAdapter(bowlingTeamAdapter);

        nextBowlerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                nextBowlerView.showDropDown();
                return false;
            }
        });

        nextBowlerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                nextBowlerID=Integer.parseInt(BowlingTeamIDArray.get(i).toString());
            }
        });

        accept_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCompleteListener = (OnBowlerFragmentCompleteListener) getTargetFragment();
                if ( BowlingTeamArray.contains(nextBowlerView.getText().toString()) )
                {
                    onCompleteListener.getBowlerDataFromFragment(nextBowlerID);
                    getDialog().dismiss();
                }
                else
                {
                    Long GetPlayerId;
                    GetPlayerId=dbClass.InsertDataToT002(nextBowlerView.getText().toString(),BowlingTeamID,2,db);
                    nextBowlerID=Integer.parseInt(GetPlayerId.toString());
                    onCompleteListener.getBowlerDataFromFragment(nextBowlerID);
                    getDialog().dismiss();
                }
            }
        });
    }

    private void cancelDialog() {
        // Do Nothing
    }

    public interface OnBowlerFragmentCompleteListener {
        void getBowlerDataFromFragment(int nextBowlID);
    }

}
