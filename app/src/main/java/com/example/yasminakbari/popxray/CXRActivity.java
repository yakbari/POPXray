package com.example.yasminakbari.popxray;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CXRActivity extends Activity
        implements ListView.OnItemClickListener {

    private List<String> mDrawerNames;
    private List<String> mTitleNames;
    private int mCurrDrawerPosition = 0;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mDrawerListAdapter;
    private ActionBarDrawerToggle mDrawerToggle;


    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cxr);

        // Load full list CXR category names from resource
        List<String> fullDrawerNames = Arrays.asList(getResources().getStringArray(R.array.cxr_drawer_names));
        List<String> fullTitleNames = Arrays.asList(getResources().getStringArray(R.array.cxr_title_names));

        // Find which categories were selected by user from calling activity (CXRCategoryActivity)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Default 1111111 mask string
            char[] default_mask = new char[fullDrawerNames.size()];
            Arrays.fill(default_mask, '1');

            // Get category mask from calling activity
            String mask = extras.getString("category_mask", new String(default_mask));

            // Set mDrawerNames and mTitleNames
            applyCategoryMask(mask, fullDrawerNames, fullTitleNames);
        } else {
            // By default select all categories (mainly ends up here when debugging)
            mDrawerNames = new ArrayList<String>(fullDrawerNames);
            mTitleNames = new ArrayList<String>(fullTitleNames);
        }
        mDrawerNames.add("Summary");
        mTitleNames.add("Summary");

        mDrawerLayout = (DrawerLayout)findViewById(R.id.cxr_drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList = (ListView) findViewById(R.id.cxr_left_drawer);
        mDrawerListAdapter = new ArrayAdapter<String>(this, R.layout.cxr_drawer_list_item, mDrawerNames);
        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(this);

        mTitle = mTitleNames.get(0);
        getActionBar().setTitle(mTitleNames.get(0));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Load the first category's fragment without adding this transaction to android's fragment backstack
        showCategory(0);
    }

    /* Applies a string mask to which CXR categories should be included in the drawer list based
    *  on what the user has selected in the prior activity (CXRCategoryActivity) */
    private void applyCategoryMask(String mask, List<String> fullDrawerNames, List<String> fullTitleNames) {
        mDrawerNames = new ArrayList<String>(fullDrawerNames.size());
        mTitleNames = new ArrayList<String>(fullDrawerNames.size());
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) != '0') {
                mDrawerNames.add(fullDrawerNames.get(i));
                mTitleNames.add(fullTitleNames.get(i));
            }
        }
    }

    /* Event Listener for Drawer's ListView which changes the current QuestionFragment being displayed.
       Other functions call this function with null parent and view, so don't depend on those
      */
    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        showCategory(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /* Show the QuestionFragment associated with the category position */
    private void showCategory(int position) {
        if (position < mDrawerNames.size() - 1) {
            /* Show a specific category by loading QuestionFragment */

            // Create new question fragment
            QuestionFragment questionFragment = new QuestionFragment();

            // Pass any args
            Bundle args = new Bundle();
            args.putInt("category", position);
            questionFragment.setArguments(args);

            // Add fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction t = fragmentManager.beginTransaction();
            t.replace(R.id.cxr_content_frame, questionFragment, mDrawerNames.get(position));
            t.commit();
        } else {
            /* Show the SummaryFragment */

            // Create new summary fragment
            SummaryFragment summaryFragment = new SummaryFragment();

            // Add fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction t = fragmentManager.beginTransaction();
            t.replace(R.id.cxr_content_frame, summaryFragment, mDrawerNames.get(position));
            t.commit();
        }

        mCurrDrawerPosition = position;
        getActionBar().setTitle(mTitleNames.get(position));
        mTitle = mTitleNames.get(position);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.cxr, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next_category) {
            if (mCurrDrawerPosition < mDrawerNames.size() - 1) {
                showCategory(mCurrDrawerPosition + 1);
            }
            return true;
        } else if (id == R.id.action_prev_category) {
            if (mCurrDrawerPosition >= 1) {
                showCategory(mCurrDrawerPosition - 1);
            }
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public List<String> getCategories() {
        return mTitleNames;
    }
}
