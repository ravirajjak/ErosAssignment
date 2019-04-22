package com.example.sampleapplication.bean;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<ResultsBean> mSelectedItem = new MutableLiveData<ResultsBean>();

    public void select(ResultsBean item) {
        mSelectedItem.setValue(item);
    }

    public LiveData<ResultsBean> getSelected() {
        return mSelectedItem;
    }
}
