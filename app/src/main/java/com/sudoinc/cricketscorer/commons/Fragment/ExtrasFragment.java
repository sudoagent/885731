package com.sudoinc.cricketscorer.commons.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.sudoinc.cricketscorer.R;

public class ExtrasFragment extends DialogFragment
{
    // Initializing Variables
    private EditText addruns;
    private TextView extra_type_display;
    private ImageView accept_button_image;
    private CheckBox BatsmenCrossCheckbox;

    private boolean BatsmenCrossed;

    private String ExtraType;

    private OnExtrasFragmentCompleteListener onCompleteListener;

    public static ExtrasFragment newInstance() {
        ExtrasFragment f = new ExtrasFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.ExtraType = getArguments().getString("type");
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
        return inflater.inflate(R.layout.fragment_extras, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.accept_button_image = getView().findViewById(R.id.extras_dialog_button_save);
        this.addruns = getView().findViewById(R.id.additionalRuns);
        this.extra_type_display = getView().findViewById(R.id.textView21);
        this.BatsmenCrossCheckbox = getView().findViewById(R.id.batsmenCrossCheckbox);

        addruns.setInputType(InputType.TYPE_CLASS_NUMBER);
        extra_type_display.setText(ExtraType);

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



        accept_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCompleteListener = (OnExtrasFragmentCompleteListener) getTargetFragment();
                if ( ! TextUtils.isEmpty(addruns.getText().toString()))
                {
                    onCompleteListener.ExtrasfromFragment(Integer.parseInt(addruns.getText().toString()),BatsmenCrossed,ExtraType);
                    getDialog().dismiss();
                }
                else
                {
                    onCompleteListener.ExtrasfromFragment(0,BatsmenCrossed,ExtraType);
                    getDialog().dismiss();
                }
            }
        });
    }

    private void cancelDialog() {
        // Do Nothing
    }

    public interface OnExtrasFragmentCompleteListener {
        void ExtrasfromFragment(int extras,boolean crossed,String type);
    }

}
