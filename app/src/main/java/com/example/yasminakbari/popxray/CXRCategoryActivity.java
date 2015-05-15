package com.example.yasminakbari.popxray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


public class CXRCategoryActivity extends Activity implements View.OnClickListener {
    // UI Elements
    private CheckBox mImageCharacteristics, mAirways, mBones, mCardiac, mDiaphragm, mEdges,
            mFields, mHardware;
    private Button mContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cxrcategory);
        mImageCharacteristics = (CheckBox)findViewById(R.id.image_characteristics);
        mAirways = (CheckBox)findViewById(R.id.airways);
        mBones = (CheckBox)findViewById(R.id.bones);
        mCardiac = (CheckBox)findViewById(R.id.cardiac);
        mDiaphragm = (CheckBox)findViewById(R.id.diaphragm);
        mEdges = (CheckBox)findViewById(R.id.edges);
        mFields = (CheckBox)findViewById(R.id.fields);
        mHardware = (CheckBox)findViewById(R.id.hardware);
        mContinue = (Button)findViewById(R.id.cxrcategory_continue);
        mContinue.setOnClickListener(this);

        // Restore saved category mask
        SharedPreferences s = getSharedPreferences("POPXray", Context.MODE_PRIVATE);
        String mask = s.getString("cxr_category_mask", "11111111");
        if (mask.charAt(0) != '0') {
            mImageCharacteristics.setChecked(true);
        }
        if (mask.charAt(1) != '0') {
            mAirways.setChecked(true);
        }
        if (mask.charAt(2) != '0') {
            mBones.setChecked(true);
        }
        if (mask.charAt(3) != '0') {
            mCardiac.setChecked(true);
        }
        if (mask.charAt(4) != '0') {
            mDiaphragm.setChecked(true);
        }
        if (mask.charAt(5) != '0') {
            mEdges.setChecked(true);
        }
        if (mask.charAt(6) != '0') {
            mFields.setChecked(true);
        }
        if (mask.charAt(7) != '0') {
            mHardware.setChecked(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cxrcategory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == mContinue) {
            // Build category mask
            String mask = "";
            if (mImageCharacteristics.isChecked()) {
                mask += "1";
            } else {
                mask += "0";
            }
            if (mAirways.isChecked()) {
                mask += "1";
            } else {
                mask += "0";
            }
            if (mBones.isChecked()) {
                mask += "1";
            } else {
                mask += "0";
            }
            if (mCardiac.isChecked()) {
                mask += "1";
            } else {
                mask += "0";
            }
            if (mDiaphragm.isChecked()) {
                mask += "1";
            } else {
                mask += "0";
            }
            if (mEdges.isChecked()) {
                mask += "1";
            } else {
                mask += "0";
            }
            if (mFields.isChecked()) {
                mask += "1";
            } else {
                mask += "0";
            }
            if (mHardware.isChecked()) {
                mask += "1";
            } else {
                mask += "0";
            }

            // Save category mask
            SharedPreferences s = getSharedPreferences("POPXray", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = s.edit();
            e.putString("cxr_category_mask", mask);
            e.commit();

            // Start CXRActivity with category mask
            Bundle extras = new Bundle();
            extras.putString("category_mask", mask);
            Intent intent = new Intent(this, CXRActivity.class);
            intent.putExtras(extras);

            startActivity(intent);
        }
    }

}
