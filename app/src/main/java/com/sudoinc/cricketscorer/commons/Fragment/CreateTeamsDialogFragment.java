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
import com.sudoinc.cricketscorer.R;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;


public class CreateTeamsDialogFragment extends DialogFragment
{
//    private AutoCompleteTextView countryTextview;
//    private String selectedCountry;
    private String selectedTeamNameString;
    private int selectedTeamTypeInt;
    private SQLiteDBManager dbClass = new SQLiteDBManager(getContext());
//    private SQLiteDatabase db = SQLiteOpenHelper().getWritableDatabase();
//    private SQLiteDBManager dbClass = new SQLiteDBManager(getActivity());

    public static CreateTeamsDialogFragment newInstance() {
        CreateTeamsDialogFragment f = new CreateTeamsDialogFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
//        final SQLiteDatabase db = new SQLiteDBManager(getActivity()).getWritableDatabase();
    }

    @Override
    public void onStart() {
        super.onStart();
        TextInputEditText input1 = (TextInputEditText) getView().findViewById(R.id.team_name);
        input1.setSelected(true);

        // Auto fill with Country List
//        countryTextview = (AutoCompleteTextView)getView().findViewById(R.id.autocomplete_country);
//        String[] countries = getResources().getStringArray(R.array.country_arrays);
//        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,countries);
//        countryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        countryTextview.setAdapter(countryAdapter);

        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_create_team, parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final ImageView close_button_image = view.findViewById(R.id.create_team_dialog_button_close);
        final ImageView accept_button_image = view.findViewById(R.id.create_team_dialog_button_save);
        final TextInputEditText selectedTeamName = view.findViewById(R.id.team_name);
        final RadioGroup selectedTeamTypeGroup = view.findViewById(R.id.home_away_radiogroup);
//        final AutoCompleteTextView selectedCountryLayout = view.findViewById(R.id.autocomplete_country);
//        selectedCountry = null;

//        selectedCountryLayout.setOnItemClickListener(new OnItemClick() {
//                                                public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
//                                                    selectedCountry = parent.getItemAtPosition(position);
//                                                }
//                                            });
        close_button_image.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                        dismiss();
                                                }
                                            });
        accept_button_image.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

//                                                    selectedCountry = selectedCountryLayout.getEditableText().toString();
                                                    selectedTeamNameString = selectedTeamName.getText().toString();
                                                    selectedTeamTypeInt = 0;
                                                    int checkedRadioButtonId = selectedTeamTypeGroup.getCheckedRadioButtonId();

                                                    if ( checkedRadioButtonId == R.id.home_radiobutton )
                                                    {
                                                        selectedTeamTypeInt = 1;
                                                    }
                                                    else if ( checkedRadioButtonId == R.id.away_radiobutton )
                                                    {
                                                        selectedTeamTypeInt = 2;
                                                    }
                                                    else if ( checkedRadioButtonId == R.id.neutral_radiobutton )
                                                    {
                                                        selectedTeamTypeInt = 3;
                                                    }

                                                    if ( TextUtils.isEmpty(selectedTeamNameString) || selectedTeamTypeInt == 0 || TextUtils.isEmpty(selectedTeamNameString) )
                                                    {
                                                        Toast toast = Toast.makeText(getContext(),"Some Inputs Empty", Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER,0,0);
                                                        toast.show();
                                                    }
                                                    else
                                                    {
                                                        final SQLiteDatabase db = new SQLiteDBManager(v.getContext()).getWritableDatabase();
                                                        System.out.println("CreateTeamsDialogFragment - '" + selectedTeamNameString + "'");
                                                        System.out.println("CreateTeamsDialogFragment - '" + selectedTeamTypeInt + "'");
                                                        dbClass.InsertDataToT001(selectedTeamNameString,selectedTeamTypeInt,db);
//                                                            System.out.println("CreateTeamsDialogFragment - '" + selectedCountry + "'");
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
