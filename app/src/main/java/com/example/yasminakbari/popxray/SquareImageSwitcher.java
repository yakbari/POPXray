package com.example.yasminakbari.popxray;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageSwitcher;

/**
 * Created by dx4 on 5/15/15.
 */
public class SquareImageSwitcher extends ImageSwitcher {
    public SquareImageSwitcher(final Context context) {
        super(context);
    }

    public SquareImageSwitcher(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, measuredWidth);
    }
}