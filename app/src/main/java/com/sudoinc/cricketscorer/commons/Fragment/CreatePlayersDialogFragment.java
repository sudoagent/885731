package com.sudoinc.cricketscorer.commons.Fragment;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
//import com.sudoinc.cricketscorer.MyTeamsActivity;
import com.sudoinc.cricketscorer.R;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;


public class CreatePlayersDialogFragment extends DialogFragment
{

    private boolean mIsHintSet;
//    private AutoCompleteTextView countryTextview;
    private String selectedPlayerNameString;
    private int selectedPlayerTypeInt;
    private int teamID=0;
    private SQLiteDBManager dbClass = new SQLiteDBManager(getContext());

    public static CreatePlayersDialogFragment newInstance() {
        CreatePlayersDialogFragment f = new CreatePlayersDialogFragment();
//        Bundle args = new Bundle();
//        args.putInt("teamID", teamID);
//        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextInputEditText input1 = (TextInputEditText) getView().findViewById(R.id.player_name);
        input1.setSelected(true);

        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        teamID = args.getInt("teamID");
        System.out.println("chumma : " + teamID);
        return inflater.inflate(R.layout.fragment_dialog_create_players, parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final ImageView close_button_image = view.findViewById(R.id.create_players_dialog_button_close);
        final ImageView accept_button_image = view.findViewById(R.id.create_players_dialog_button_save);
        final TextInputEditText selectedPlayerName = view.findViewById(R.id.player_name);
        final RadioGroup selectedPlayerTypeGroup = view.findViewById(R.id.player_select_radiogroup_line1);
//        final RadioGroup selectedPlayerTypeGroup_two = view.findViewById(R.id.player_select_radiogroup_line2);
        selectedPlayerTypeGroup.clearCheck();
//        selectedPlayerTypeGroup_two.clearCheck();

        close_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        accept_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPlayerNameString = selectedPlayerName.getText().toString();
                selectedPlayerTypeInt = 0;
                int checkedRadioButtonId = selectedPlayerTypeGroup.getCheckedRadioButtonId();
//                int checkedRadioButtonId_two = selectedPlayerTypeGroup_two.getCheckedRadioButtonId();

                if ( checkedRadioButtonId == R.id.batsmen_radiobutton )
                {
                    selectedPlayerTypeInt = 1;
                }
                else if ( checkedRadioButtonId == R.id.bowler_radiobutton )
                {
                    selectedPlayerTypeInt = 2;
                }
                else if ( checkedRadioButtonId == R.id.allrounder_radiobutton )
                {
                    selectedPlayerTypeInt = 3;
                }
                else if ( checkedRadioButtonId == R.id.wk_radiobutton )
                {
                    selectedPlayerTypeInt = 4;
                }

                if ( selectedPlayerTypeInt == 0 || TextUtils.isEmpty(selectedPlayerNameString) )
                {
                    Toast toast = Toast.makeText(getContext(),"Some Inputs Empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else
                {
                    final SQLiteDatabase db = new SQLiteDBManager(v.getContext()).getWritableDatabase();
                    System.out.println("CreatePlayersDialogFragment - '" + selectedPlayerNameString + "'");
                    System.out.println("CreatePlayersDialogFragment - '" + selectedPlayerTypeInt + "'");
                    dbClass.InsertDataToT002(selectedPlayerNameString,teamID,selectedPlayerTypeInt,db);
                    dismiss();
                }
            }
        });
    }

    private void cancelDialog()
    {
        dismiss();
    }

}
