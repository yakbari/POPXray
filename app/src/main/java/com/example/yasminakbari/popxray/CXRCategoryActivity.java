package com.example.yasminakbari.popxray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;


public class CXRCategoryActivity extends Activity implements View.OnClickListener {
    // UI Elements
    private ListView mCategoryList;
    private Button mContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cxrcategory);

        mCategoryList = (ListView)findViewById(R.id.category_list);

        mContinue = (Button)findViewById(R.id.cxrcategory_continue);
        mContinue.setOnClickListener(this);

        // Get default CXR category list
        List<String> categoryNames = Arrays.asList(getResources().getStringArray(R.array.cxr_category_names));

        // Restore saved category mask
        SharedPreferences s = getSharedPreferences("POPXray", Context.MODE_PRIVATE);
        // Get default mask string
        char[] default_mask = new char[categoryNames.size()];
        Arrays.fill(default_mask, '1');
        String mask = s.getString("cxr_category_mask", new String(default_mask));

        // Set listview's adapter
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this, R.layout.list_category_item, categoryNames);
        mCategoryList.setAdapter(listViewAdapter);

        // Restore which items are checked and which are not
        if (mask.length() == listViewAdapter.getCount()) {
            for (int i = 0; i < listViewAdapter.getCount(); i++) {
                mCategoryList.setItemChecked(i, mask.charAt(i) != '0');
            }
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
            for (int i = 0; i < mCategoryList.getChildCount(); i++) {
                if (((CheckBox)mCategoryList.getChildAt(i)).isChecked()) {
                    mask += "1";
                } else {
                    mask += "0";
                }
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
