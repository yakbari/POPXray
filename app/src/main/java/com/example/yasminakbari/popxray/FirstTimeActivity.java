package com.example.yasminakbari.popxray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class FirstTimeActivity extends Activity implements View.OnClickListener {
    // UI Elements
    private Button mGetStarted, mAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if first time opening app
        SharedPreferences sharedPref = getSharedPreferences("POPXray", Context.MODE_PRIVATE);
        boolean first_time = sharedPref.getBoolean("first_time", true);
        if (!first_time) {
            // If not first time, start CXRCategoryActivity
            getStarted();
            return;
        } else {
            // First time
            // Debug: we don't save this so it shows up every time

//            SharedPreferences.Editor e = sharedPref.edit();
//            e.putBoolean("first_time", false);
//            e.commit();
        }
        setContentView(R.layout.activity_first_time);

        mGetStarted = (Button)findViewById(R.id.get_started);
        mGetStarted.setOnClickListener(this);
        mAbout = (Button)findViewById(R.id.firsttime_about);
        mAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mGetStarted) {
            getStarted();
        } else if (view == mAbout) {
            showAbout();
        }
    }

    private void getStarted() {
        Intent intent = new Intent(this, CXRCategoryActivity.class);
        startActivity(intent);
        finish();
    }

    private void showAbout() {
        View aboutView = getLayoutInflater().inflate(R.layout.dialog_about, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.app_name);
        builder.setView(aboutView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Noop
            }
        });
        builder.create();
        builder.show();
    }
}

