package com.example.sampleapplication.adapter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.sampleapplication.R;
import com.example.sampleapplication.activity.HomeActivity;
import com.example.sampleapplication.activity.MovieDetailActivity;
import com.example.sampleapplication.bean.ResultsBean;
import com.example.sampleapplication.bean.SharedViewModel;
import com.example.sampleapplication.databinding.RowGridMovieBinding;
import com.example.sampleapplication.fragment.TabOneFragment;
import com.example.sampleapplication.interfaces.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private SharedViewModel mSharedModel;
    private List<ResultsBean> dataList;
    private Context context;
    private LayoutInflater layoutInflater;
    String mImgUrl = "https://image.tmdb.org/t/p/w500";
    private int lastPosition;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public MovieAdapter(Context context, List<ResultsBean> dataList, RecyclerView mRecyclerView) {
        this.context = context;
        this.dataList = dataList;
        mSharedModel = ViewModelProviders.of((FragmentActivity) context).get(SharedViewModel.class);
        setRecylerViewListener(mRecyclerView);
    }

    private void setRecylerViewListener(RecyclerView mRecyclerView) {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView
                .getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                if (!loading
                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        RowGridMovieBinding mRowGridBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_grid_movie, viewGroup, false);
        return new MyViewHolder(mRowGridBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        onBind(holder, position);
    }

    private void onBind(MyViewHolder holder, final int position) {
        dataList.get(position).setImageUrl(mImgUrl + dataList.get(position).getPoster_path());
        holder.binding.setMovies(dataList.get(position));
        setAnimation(holder.itemView, position);
        holder.binding.rowMovieFavourite.setOnClickListener(v -> {
            if (dataList.get(position).isFavourite()) {
                dataList.get(position).setFavourite(false);

            } else {
                dataList.get(position).setFavourite(true);
//                mSharedModel.select(dataList.get(position));
            }
            mSharedModel.select(dataList.get(position));

        });
        holder.binding.rowMovieBackground.setOnClickListener(v -> {
            ((HomeActivity) context).openIntent(MovieDetailActivity.class, false, dataList.get(position));
        });

    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        return dataList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowGridMovieBinding binding;

        public MyViewHolder(@NonNull RowGridMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
