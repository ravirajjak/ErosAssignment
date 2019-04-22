package com.example.sampleapplication.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sampleapplication.R;
import com.example.sampleapplication.adapter.SectionsPagerAdapter;
import com.example.sampleapplication.bean.MovieResponse;
import com.example.sampleapplication.bean.ResultsBean;
import com.example.sampleapplication.bean.SharedViewModel;
import com.example.sampleapplication.databinding.ActivityHomeBinding;
import com.example.sampleapplication.enumeration.ApiEnum;
import com.example.sampleapplication.fragment.TabOneFragment;
import com.example.sampleapplication.fragment.TabTwoFragment;
import com.example.sampleapplication.interfaces.IOnApiListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ActivityHomeBinding mDataBinding;
    private TabOneFragment mTabOne;
    private TabTwoFragment mTabTwo;

    private String mGlobalSearchKey;
    String mSearchUrl;
    private boolean mSearchFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setPagerAdapter();
        setSupportActionBar(mDataBinding.toolbar);
        setSearchListener();


    }

    private void setPagerAdapter() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(getRespectiveFragment(1), getString(R.string.lbl_movies), 0);
        mSectionsPagerAdapter.addFragment(getRespectiveFragment(2), getString(R.string.lbl_favourite_movies), 1);
        mDataBinding.actHomeViewpager.setAdapter(mSectionsPagerAdapter);
        mDataBinding.actHomeTablayout.setupWithViewPager(mDataBinding.actHomeViewpager);
    }

    private Fragment getRespectiveFragment(int i) {
        if (i == 1) {
            mTabOne = TabOneFragment.newInstance();
            return mTabOne;
        } else {
            mTabTwo = TabTwoFragment.newInstance();
            return mTabTwo;
        }
    }


    private void setSearchListener() {

        mDataBinding.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mGlobalSearchKey = query;
                mSearchUrl = "https://api.themoviedb.org/3/search/movie?api_key=f89407e21b4e54f77b9de9a7bac59707&language=en-US&query=" + mGlobalSearchKey + "&page=1&include_adult=false";
                executeTask(mSearchUrl, ApiEnum.SEARCH_REQUEST, iOnApiListener);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        mDataBinding.searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mDataBinding.searchView.setMenuItem(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    IOnApiListener iOnApiListener = new IOnApiListener() {
        @Override
        public void onSuccess(String response) {
            if (response != null) {
                MovieResponse mMoviesResponse = gson.fromJson(response, MovieResponse.class);
                mTabOne.setSearchAdapter(mMoviesResponse.getResults(), mGlobalSearchKey);
            }
        }

        @Override
        public void onError(String response) {
            Toast.makeText(getApplicationContext(), "Error : " + response, Toast.LENGTH_LONG).show();
        }
    };

    public void openIntent(Class mClass, boolean finishFlag, ResultsBean mData) {
        Intent intent = new Intent(this, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("movie_content", mData);
        startActivity(intent);
        if (finishFlag)
            finish();
    }
}
