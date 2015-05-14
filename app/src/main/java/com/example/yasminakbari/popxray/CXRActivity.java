package com.example.yasminakbari.popxray;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
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
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cxr);

        // Load CXR category names
        List<String> fullDrawerNames = Arrays.asList(getResources().getStringArray(R.array.cxr_drawer_names));
        List<String> fullTitleNames = Arrays.asList(getResources().getStringArray(R.array.cxr_title_names));

        Bundle extras = getIntent().getExtras();
        // Find which categories were selected by user from calling activity (CXRCategoryActivity)
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
            mDrawerNames = fullDrawerNames;
            mTitleNames = fullTitleNames;
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
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.cxr_drawer_list_item, mDrawerNames));
        mDrawerList.setOnItemClickListener(this);

        mTitle = mTitleNames.get(0);
        setTitle(mTitleNames.get(0));
        getActionBar().setHomeButtonEnabled(true);

        // Load the first category's fragment
        onItemClick(null, null, 0, 0);
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

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // TODO: this only replace's current fragments in the container with the new one, this
        // is not consistent with our intended behavior, will need to fix later
        Fragment questionFragment = new QuestionFragment();

        Bundle args = new Bundle();
        args.putInt("category", position);
        questionFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.cxr_content_frame, questionFragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        mCurrDrawerPosition = position;
        getActionBar().setTitle(mTitleNames.get(position));
        mTitle = mTitleNames.get(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /* Question Fragment which shows every question */

    public static class QuestionFragment extends Fragment implements View.OnClickListener {
        private CXRActivity mParentActivity;
        private String mCategory;

        // UI elements
        TextView mQuestionText, mSupplementalText;
        Button mPrev, mSkip, mNext;
        ArrayList<RadioButton> mAnswerChoices;
        LinearLayout mAnswerLayout;
        RadioGroup mAnswerRadioGroup;
        HorizontalScrollView mImageScroller;

        public QuestionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_question, container, false);
            Bundle args = getArguments();
            int position = args.getInt("category", 0);
            mCategory = mParentActivity.getCategories().get(position);

            // Get UI element handles
            mQuestionText = (TextView)rootView.findViewById(R.id.question_text);
            mSupplementalText = (TextView)rootView.findViewById(R.id.supplemental_text);
            mPrev = (Button)rootView.findViewById(R.id.previous_button);
            mPrev.setOnClickListener(this);
            mSkip = (Button)rootView.findViewById(R.id.skip_button);
            mSkip.setOnClickListener(this);
            mNext = (Button)rootView.findViewById(R.id.next_button);
            mNext.setOnClickListener(this);
            mAnswerLayout = (LinearLayout)rootView.findViewById(R.id.answer_layout);
            mAnswerRadioGroup = (RadioGroup)rootView.findViewById(R.id.answer_radiogroup);

            mImageScroller = (HorizontalScrollView)rootView.findViewById(R.id.thumbnail_scroller);


            loadQuestion();
            return rootView;
        }

        private void loadQuestion() {
            // TODO
            mQuestionText.setText("First question for " + mCategory);
            mSupplementalText.setText("Hint for " + mCategory);

            // Load answer choices
            // TODO
            mAnswerChoices = new ArrayList<RadioButton>(2);
            mAnswerChoices.add(new RadioButton(mParentActivity));
            mAnswerChoices.get(0).setText("Answer 1");
            mAnswerChoices.add(new RadioButton(mParentActivity));
            mAnswerChoices.get(1).setText("Answer 2");
            for (int i = 0; i < mAnswerChoices.size(); i++) {
                mAnswerRadioGroup.addView(mAnswerChoices.get(i));
            }
        }

        @Override
        public void onClick(View view) {
            if (view == mPrev) {

            } else if (view == mNext) {

            } else if (view == mSkip) {

            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mParentActivity = (CXRActivity)activity;
        }


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
                onItemClick(null, null, mCurrDrawerPosition + 1, 0);
            }
            return true;
        } else if (id == R.id.action_prev_category) {
            if (mCurrDrawerPosition >= 1) {
                onItemClick(null, null, mCurrDrawerPosition - 1, 0);
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
