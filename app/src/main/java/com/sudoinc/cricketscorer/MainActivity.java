package com.sudoinc.cricketscorer;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sudoinc.cricketscorer.commons.SQLiteDBManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button MyTeamsButton = null;
    private Button NewMatchButton = null;
    private Button ResumeMatchButton = null;
    private Button TournametsButton = null;
    private Button HistoryButton = null;
    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To Reset and Initialize Values
        initControls();

        //onClick of My Teams Button
        MyTeamsButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               //Start the My Teams Activity
               Intent myTeamsIntent = new Intent(MainActivity.this, MyTeamsActivity.class);
               startActivity(myTeamsIntent);
           }
        });

        //onClick of NewMatchButton
        NewMatchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent newMatchIntent = new Intent(MainActivity.this, NewMatchActivity.class);
                startActivity(newMatchIntent);
            }
        });

        //onClick of NewMatchButton
        final SQLiteDatabase db = new SQLiteDBManager(this).getWritableDatabase();
        ResumeMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent resumeMatchIntent = new Intent(MainActivity.this, ResumeMatchActivity.class);
                startActivity(resumeMatchIntent);
            }
        });

        //onClick of NewMatchButton
        TournametsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast toast = Toast.makeText(context,"You clicked Tournament Button", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });

        //onClick of NewMatchButton
        HistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast toast = Toast.makeText(context,"You clicked History Button", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    private void initControls()
    {
        if ( MyTeamsButton == null )
        {
            MyTeamsButton = (Button)findViewById(R.id.MyTeams);
        }
        if ( NewMatchButton == null )
        {
            NewMatchButton = (Button)findViewById(R.id.NewMatch);
        }
        if ( ResumeMatchButton == null )
        {
            ResumeMatchButton = (Button)findViewById(R.id.ResumeMatch);
        }
        if ( TournametsButton == null )
        {
            TournametsButton = (Button)findViewById(R.id.Tournamets);
        }
        if ( HistoryButton == null )
        {
            HistoryButton = (Button)findViewById(R.id.History);
        }
        if ( context == null )
        {
            context = getApplicationContext();
        }

        //TODO: SQL Inserts and Table creations - if there is no past entries.
        SQLiteDatabase db = new SQLiteDBManager(this).getWritableDatabase();
    }

    @Override
    public void onBackPressed()
    {
        new MaterialAlertDialogBuilder(this,android.R.style.Theme_Material_Light_Dialog_NoActionBar)
                .setTitle("App Exit")
                .setMessage("Are you sure you want to exit this app?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }
                    })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
