package com.example.yasminakbari.popxray;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SummaryFragment extends Fragment implements View.OnClickListener {
    private CXRActivity mParentActivity;

    // UI Elements
    private Button mPrev, mNew;

    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);

        mPrev = (Button)rootView.findViewById(R.id.previous_button);
        mPrev.setOnClickListener(this);
        mNew = (Button)rootView.findViewById(R.id.new_button);
        mNew.setOnClickListener(this);
        return rootView;

    }

    @Override
    public void onClick(View view) {
        if (view == mPrev) {

        } else if (view == mNew) {

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = (CXRActivity)activity;
    }
}
