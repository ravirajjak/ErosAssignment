package com.example.sampleapplication.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.example.sampleapplication.R;


public class AutoFitGridLayoutManager extends GridLayoutManager {

    private int columnWidth;
    private boolean columnWidthChanged = true;

    public AutoFitGridLayoutManager(Context context, int columnNumber) {
        super(context, 1);
        calCalculateView(context, 3);
    }

    public AutoFitGridLayoutManager(Context context, int columnWidth, int columnNumber) {
        super(context, 1);
        calCalculateView(context, columnNumber);
    }

    private void calCalculateView(Context context, int columnNumber) {
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        try {
            if (tabletSize) {
                // do something
                setColumnWidth(context.getResources().getInteger(R.integer.gried_size));
            } else {
                // do something else
                DisplayMetrics metrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                float widthPixels = metrics.widthPixels * (float) 0.9;
                try {
                    if (((Activity) context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        // code for portrait mode
                        widthPixels = metrics.widthPixels * (float) 0.9;
                    } else {
                        // code for landscape mode
                        widthPixels = metrics.heightPixels * (float) 0.9;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setColumnWidth((int) widthPixels / columnNumber);
                //   setColumnWidth((int)context.getResources().getDimension(R.dimen.margin_50));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setColumnWidth(int newColumnWidth) {
        try {
            if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
                columnWidth = newColumnWidth;
                columnWidthChanged = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (columnWidthChanged && columnWidth > 0) {
            int totalSpace;
            if (getOrientation() == VERTICAL) {
                totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            } else {
                totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace / columnWidth);
            setSpanCount(spanCount);
            columnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}