package com.example.yasminakbari.popxray;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;


public class ExampleActivity extends Activity implements View.OnClickListener {
    private static int THUMBNAIL_SIZE = 250;
    private QuestionAdapter mQuestionAdapter = null;
    private int mAnswerChoice = -1;
    private List<Integer> mImageResId;
    private int mCurrImageIndex = 0;

    // UI Elements
    private SquareImageSwitcher mLargeImage;
    private LinearLayout mThumbnailLayout;
    private TextView mText, mReference;
    private Button mCloseButton;
    private LinearLayout mMainLayout;
    private ArrayList<ImageButton> mThumbnails;
    private GestureDetector mGestureDetector;

    public ExampleActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        // Get answer choice
        mAnswerChoice = getIntent().getIntExtra("answer_choice", -1);
        mAnswerChoice = 0;
        // TODO: uncomment
//        if (mAnswerChoice == -1) {
//            finish();
//            return;
//        }

        // Init UI elements
        mLargeImage = (SquareImageSwitcher)findViewById(R.id.large_view);
        mThumbnailLayout = (LinearLayout)findViewById(R.id.thumbnail_layout);
        mText = (TextView)findViewById(R.id.example_text);
        mReference = (TextView)findViewById(R.id.example_reference);
        mCloseButton = (Button)findViewById(R.id.close_button);
        mCloseButton.setOnClickListener(this);
        mMainLayout = (LinearLayout)findViewById(R.id.main_layout);


        // Init main image switcher
        mLargeImage.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                SquareImageView myView = new SquareImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageSwitcher.LayoutParams lp = new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT,
                        ImageSwitcher.LayoutParams.MATCH_PARENT);

                myView.setLayoutParams(lp);
                return myView;
            }
        });
        mGestureDetector = new GestureDetector(this, new SwipeListener());
        mLargeImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        // Load example data
        loadExampleData();
    }

    public void setAdapter(QuestionAdapter adapter) {
        mQuestionAdapter = adapter;
    }

    @Override
    public void onClick(View view) {
        if (view == mCloseButton) {
            finish();
        } else if (view.getClass().getName().equalsIgnoreCase("android.widget.ImageButton")) {
            int thumbnail_index;
            try {
                thumbnail_index = (Integer)view.getTag();
                showImage(thumbnail_index);
            } catch (Exception e) {
                return;
            }
        }
    }

    private void loadExampleData() {
        // TODO: load real example data from mQuestionAdapter

        // Get image resource list from example
        ArrayList<String> imageNames = new ArrayList<>();
        imageNames.add("e1ci2_2");
        imageNames.add("e_ciq2s1r1");
        imageNames.add("e_ciq2s2r2");
        imageNames.add("e_ciq2s1r1");


        mImageResId = new ArrayList<>(imageNames.size());
        for (int i = 0; i < imageNames.size(); i++) {
            try {
                int resId = getResources().getIdentifier(imageNames.get(i), "drawable", getPackageName());
                mImageResId.add(resId);
            } catch (Exception e) {
                // Fail silent, probably mispelled resource name, make sure not to include .jpg extension
            }
        }

        // Load thumbnails
        mThumbnails = new ArrayList<>(mImageResId.size());
        for (int i = 0; i < mImageResId.size(); i++) {
            int resId = mImageResId.get(i);
            ImageButton newThumbnail = new ImageButton(this);
            newThumbnail.setBackgroundResource(R.drawable.thumbnail_unselected);
            newThumbnail.setPadding(5, 5, 5, 5);
            newThumbnail.setImageResource(R.drawable.e1ci2_2);
            newThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
            lp.setMargins(5, 0, 5, 0);
            newThumbnail.setLayoutParams(lp);
            newThumbnail.setTag(i);
            newThumbnail.setOnClickListener(this);
            mThumbnails.add(newThumbnail);
            mThumbnailLayout.addView(newThumbnail);
        }

        // Set large image to first one
        showImage(0);
    }

    private void showImage(int imageIndex) {
        int resId = mImageResId.get(imageIndex);
        Animation fadeOut, fadeIn;
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        mLargeImage.setInAnimation(fadeIn);
        mLargeImage.setOutAnimation(fadeOut);
        mLargeImage.setImageResource(resId);
        mThumbnails.get(mCurrImageIndex).setBackgroundResource(R.drawable.thumbnail_unselected);
        mThumbnails.get(imageIndex).setBackgroundResource(R.drawable.thumbnail_selected);
        mCurrImageIndex = imageIndex;
    }

    private void showNextImage() {
        if (mCurrImageIndex + 1 > mImageResId.size()) {
            // Show overscroll
            return;
        }
        mCurrImageIndex++;
        int resId = mImageResId.get(mCurrImageIndex);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        mLargeImage.setInAnimation(fadeIn);
        mLargeImage.setOutAnimation(fadeOut);
        mLargeImage.setImageResource(resId);

        mThumbnails.get(mCurrImageIndex - 1).setBackgroundResource(R.drawable.thumbnail_unselected);
        mThumbnails.get(mCurrImageIndex).setBackgroundResource(R.drawable.thumbnail_selected);
    }

    private void showPrevImage() {
        if (mCurrImageIndex - 1 < 0) {
            // Show overscroll
            return;
        }
        mCurrImageIndex--;
        int resId = mImageResId.get(mCurrImageIndex);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        mLargeImage.setInAnimation(fadeIn);
        mLargeImage.setOutAnimation(fadeOut);
        mLargeImage.setImageResource(resId);

        mThumbnails.get(mCurrImageIndex + 1).setBackgroundResource(R.drawable.thumbnail_unselected);
        mThumbnails.get(mCurrImageIndex).setBackgroundResource(R.drawable.thumbnail_selected);
    }

    private class SwipeListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 75;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                    return false;
                }
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    showNextImage();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    showPrevImage();
                }
            } catch (Exception e) {

            }
            return false;
        }
    }
}

