package com.example.sampleapplication.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sampleapplication.R;
import com.example.sampleapplication.adapter.AutoFitGridLayoutManager;
import com.example.sampleapplication.adapter.MovieAdapter;
import com.example.sampleapplication.bean.MovieResponse;
import com.example.sampleapplication.bean.ResultsBean;
import com.example.sampleapplication.databinding.FragmentHomeBinding;
import com.example.sampleapplication.enumeration.ApiEnum;
import com.example.sampleapplication.interfaces.IOnApiListener;
import com.example.sampleapplication.interfaces.OnLoadMoreListener;
import com.example.sampleapplication.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class TabOneFragment extends BaseFragment implements IOnApiListener, OnLoadMoreListener {

    private static String PARAM1;
    private FragmentHomeBinding mFragHomeBinding;
    String mTopRatedUrl = "https://api.themoviedb.org/3/movie/top_rated?api_key=f89407e21b4e54f77b9de9a7bac59707&language=en-US&page=";

    private MovieAdapter mMoviesAdapter;
    private int mPageNext = 1;
    private List<ResultsBean> dataList;
    private boolean mGlobalSearchFlag;
    private String mSearchUrl;
    private String mGlobalSearchKey;


    public static TabOneFragment newInstance() {
        TabOneFragment fragment = new TabOneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,
                container, false);
        View view = mFragHomeBinding.getRoot();
        setDefaultInitilization();
        return view;

    }

    private void setDefaultInitilization() {
        if (mGlobalSearchFlag) {
            mSearchUrl = "https://api.themoviedb.org/3/search/movie?api_key=f89407e21b4e54f77b9de9a7bac59707&language=en-US&query="
                    + mGlobalSearchKey + "&page=" + mPageNext + "&include_adult=false";
            executeTask(mSearchUrl, ApiEnum.GET_TOP_RATED, this);
        } else {
            mTopRatedUrl = mTopRatedUrl + mPageNext;
            executeTask(mTopRatedUrl, ApiEnum.GET_TOP_RATED, this);
        }
    }

    @Override
    public void onSuccess(String response) {
        MovieResponse mResponse = gson.fromJson(response, MovieResponse.class);
        if (mResponse != null) {
            if (mPageNext == 1) {
                if (dataList == null)
                    dataList = new ArrayList<>();

                mFragHomeBinding.fragHomeRecylerview.setLayoutManager(new AutoFitGridLayoutManager(getActivity(), 3));

                dataList = mResponse.getResults();
                mMoviesAdapter = new MovieAdapter(getActivity(), dataList, mFragHomeBinding.fragHomeRecylerview);
                mFragHomeBinding.fragHomeRecylerview.setAdapter(mMoviesAdapter);
                mMoviesAdapter.setOnLoadMoreListener(this);

            } else {
                dataList.addAll(dataList);
                mMoviesAdapter.notifyDataSetChanged();
                mMoviesAdapter.setLoaded();
            }
        }
    }

    @Override
    public void onError(String response) {
        Toast.makeText(getActivity(), "Error " + response, Toast.LENGTH_LONG).show();
    }

    public void setSearchAdapter(List<ResultsBean> mDataList, String mGlobalSearchKey) {
        mPageNext = 1;
        mGlobalSearchFlag = true;
        this.mGlobalSearchKey = mGlobalSearchKey;
        mMoviesAdapter = new MovieAdapter(getActivity(), mDataList, mFragHomeBinding.fragHomeRecylerview);
        mFragHomeBinding.fragHomeRecylerview.setAdapter(mMoviesAdapter);

    }

    @Override
    public void onLoadMore() {
        if (mPageNext != 0) {
            ++mPageNext;
            setDefaultInitilization();
        }
    }
}
