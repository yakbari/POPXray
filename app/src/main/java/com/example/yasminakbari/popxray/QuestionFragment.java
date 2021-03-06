package com.example.yasminakbari.popxray;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dx4 on 5/14/15.
 */
/* Question Fragment which shows every question */
public class QuestionFragment extends Fragment implements View.OnClickListener {
    private CXRActivity mParentActivity;
    private String mCategory;

    // UI elements
    TextView mQuestionText, mSupplementalText;
    Button mPrev, mSkip, mNext;
    ArrayList<AnswerItem> mAnswerItems;
    LinearLayout mAnswerLayout;

    private static int THUMBNAIL_SIZE = 150;

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

        loadQuestion();
        return rootView;
    }

    private void loadQuestion() {
        // TODO
        mQuestionText.setText("First question for " + mCategory);
        mSupplementalText.setText("Hint for " + mCategory);

        // Load answer choices
        // TODO
        mAnswerItems = new ArrayList<AnswerItem>(4);
        AnswerItem ans1 = new AnswerItem(mParentActivity, mAnswerItems, AnswerItem.RADIO, "Answer 1\nsecond line of answer", 0);
        AnswerItem ans2 = new AnswerItem(mParentActivity, mAnswerItems, AnswerItem.RADIO, "Answer 2\nsecond line of answer\n3rd line of answer", R.drawable.e_ciq2s1r1);
        AnswerItem ans3 = new AnswerItem(mParentActivity, mAnswerItems, AnswerItem.RADIO, "Answer 3", R.drawable.e_ciq2s1r1);
        AnswerItem ans4 = new AnswerItem(mParentActivity, mAnswerItems, AnswerItem.RADIO, "Answer 4", R.drawable.e1ci2_2);

        ans1.setImageOnClickListener(this);
        ans2.setImageOnClickListener(this);
        ans3.setImageOnClickListener(this);
        ans4.setImageOnClickListener(this);

        // Add all answer choices to view
        for (int i = 0; i < mAnswerItems.size(); i++) {
            mAnswerLayout.addView(mAnswerItems.get(i));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mPrev) {

        } else if (view == mNext) {

        } else if (view == mSkip) {

        } else if (view.getClass().getName().equalsIgnoreCase("android.widget.ImageButton")) {
            for (int i = 0; i < mAnswerItems.size(); i++) {
                if (view == mAnswerItems.get(i).getAnswerImage()) {
                    int answerChoice = i;
                    showExample(answerChoice);
                }
            }
        }
    }

    private void showExample(int answerChoice) {
        Intent i = new Intent(mParentActivity, ExampleActivity.class);
        i.putExtra("answer_choice", answerChoice);
        startActivity(i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = (CXRActivity)activity;
    }

    private static class AnswerItem extends LinearLayout {
        public static int RADIO = 1;
        public static int CHECK = 2;
        private CompoundButton mAnswerRadioCheck;
        private int mAnswerType;
        private ImageButton mAnswerImage;
        private ArrayList<AnswerItem> mAnswerGroup;

        public AnswerItem(Context c, ArrayList<AnswerItem> answerGroup, int answerType, String text, int imageResource) {
            super(c);
            mAnswerGroup = answerGroup;
            mAnswerType = answerType;

            // Add itself to the answer group
            answerGroup.add(this);

            // Set layout of itself (LinearLayout)
            this.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 4, 0, 4);
            this.setLayoutParams(lp);
            this.setMinimumHeight(THUMBNAIL_SIZE);
            this.setOrientation(LinearLayout.HORIZONTAL);

            // Create radio or check button
            if (mAnswerType == RADIO) {
                mAnswerRadioCheck = new RadioButton(c);
            } else {
                mAnswerRadioCheck = new CheckBox(c);
            }
            mAnswerRadioCheck.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
            mAnswerRadioCheck.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Uncheck all other radio buttons in the answer group
                    for (int i = 0; i < mAnswerGroup.size(); i++) {
                        if (AnswerItem.this.mAnswerGroup.get(i).getAnswerRadio() != AnswerItem.this.getAnswerRadio() && AnswerItem.this.mAnswerGroup.get(i).getAnswerType() == RADIO) {
                            AnswerItem.this.mAnswerGroup.get(i).getAnswerRadio().setChecked(false);
                        }
                    }
                }
            });
            setText(text);
            this.addView(mAnswerRadioCheck);

            // Create image button
            mAnswerImage = new ImageButton(c);
            setImage(imageResource);
            this.addView(mAnswerImage);
        }

        public void setText(String text) {
            ((CompoundButton)mAnswerRadioCheck).setText(text);
        }

        public void setImage(int resource) {
            if (resource == 0) {
                mAnswerImage.setVisibility(View.INVISIBLE);
                return;
            }
            // Set image resource and change layout of the image

            mAnswerImage.setVisibility(View.VISIBLE);
            // Create a blue border around the image button
            mAnswerImage.setBackgroundResource(R.drawable.thumbnail_button);
            mAnswerImage.setPadding(5, 5, 5, 5);
            mAnswerImage.setImageResource(resource);
            mAnswerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // Change image to fixed size THUMBNAIL_SIZE X THUMBNAIL_SIZE
            LinearLayout.LayoutParams lp = new LayoutParams(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            mAnswerImage.setLayoutParams(lp);
        }

        public void setImageOnClickListener(OnClickListener listener) {
            mAnswerImage.setOnClickListener(listener);
        }

        public CompoundButton getAnswerRadio() {
            return mAnswerRadioCheck;
        }

        public ImageButton getAnswerImage() {
            return mAnswerImage;
        }

        public int getAnswerType() {
            return mAnswerType;
        }
    }
}
