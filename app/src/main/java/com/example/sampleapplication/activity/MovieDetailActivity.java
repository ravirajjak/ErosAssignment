package com.example.sampleapplication.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.sampleapplication.R;
import com.example.sampleapplication.bean.ResultsBean;
import com.example.sampleapplication.databinding.ActivityHomeBinding;
import com.example.sampleapplication.databinding.ActivityMovieDetailBinding;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding mDataBinding;
    private ResultsBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        setDefaultInitilization();
        getIntentData();
    }

    private void getIntentData() {
        mData = getIntent().getParcelableExtra("movie_content");
        mDataBinding.setMovies(mData);
        mDataBinding.contentMovieDetail.setMovies(mData);
    }


    private void setDefaultInitilization() {
        setAppBarListener();
        setToolbarNavigationListener();


    }


    private void setToolbarNavigationListener() {
        mDataBinding.toolbar.setNavigationIcon(R.drawable.ic_back);
        mDataBinding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        mDataBinding.toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void setAppBarListener() {
        mDataBinding.collapsingLayout.setTitle(" ");
        mDataBinding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {

                    mDataBinding.collapsingLayout.setTitle(mData.getTitle());
                } else if (isShow) {
                    mDataBinding.collapsingLayout.setTitle(" ");
                }
            }
        });
    }
}
