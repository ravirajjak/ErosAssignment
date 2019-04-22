package com.example.sampleapplication.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sampleapplication.R;
import com.example.sampleapplication.adapter.AutoFitGridLayoutManager;
import com.example.sampleapplication.adapter.MovieAdapter;
import com.example.sampleapplication.bean.ResultsBean;
import com.example.sampleapplication.bean.SharedViewModel;
import com.example.sampleapplication.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabTwoFragment extends Fragment {

    private static String PARAM1;
    private FragmentHomeBinding mFragHomeBinding;
    private List<ResultsBean> dataList;
    SparseArray<ResultsBean> mHash;

    public static TabTwoFragment newInstance() {
        TabTwoFragment fragment = new TabTwoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedViewModel model = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        model.getSelected().observe(this, resultsBean -> {
            Toast.makeText(getActivity(), "Result ", Toast.LENGTH_LONG).show();
            setAdapter(resultsBean);
        });
    }


    private void getDataList(SparseArray mp) {
        dataList = new ArrayList<>();
        for (int i = 0; i < mp.size(); i++) {
            int key = mp.keyAt(i);
            // get the object by the key.
            ResultsBean obj = (ResultsBean) mp.get(key);
            dataList.add(obj);
        }
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
        dataList = new ArrayList<>();
    }

    private void setAdapter(ResultsBean resultsBean) {

        if (mHash == null) {
            mHash = new SparseArray<>();
        }
        if (resultsBean != null) {
            if (resultsBean.isFavourite()) {
                mHash.put(resultsBean.getId(), resultsBean);

            } else {
                mHash.remove(resultsBean.getId());
            }
            getDataList(mHash);
        }
        mFragHomeBinding.fragHomeRecylerview.setLayoutManager(new AutoFitGridLayoutManager(getActivity(), 3));
        mFragHomeBinding.fragHomeRecylerview.setAdapter(new MovieAdapter(getActivity(), dataList, mFragHomeBinding.fragHomeRecylerview));
    }
}
