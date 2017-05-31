package com.xhbb.qinzl.newsest.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.xhbb.qinzl.newsest.BR;

/**
 * Created by qinzl on 2017/5/29.
 */

public class RecyclerViewModel extends BaseObservable {

    private boolean mAutoRefreshing;
    private String mAbnormalText;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener mOnSwipeRefreshListener;

    public RecyclerViewModel(RecyclerView.Adapter recyclerViewAdapter,
                             RecyclerView.LayoutManager recyclerViewLayoutManager,
                             SwipeRefreshLayout.OnRefreshListener onSwipeRefreshListener) {
        mRecyclerViewAdapter = recyclerViewAdapter;
        mRecyclerViewLayoutManager = recyclerViewLayoutManager;
        mOnSwipeRefreshListener = onSwipeRefreshListener;

        mAutoRefreshing = true;
        mAbnormalText = "";
    }

    public void setAutoRefreshing(boolean autoRefreshing) {
        mAutoRefreshing = autoRefreshing;
        notifyPropertyChanged(BR.autoRefreshing);
    }

    @BindingAdapter({"android:onSwipeRefresh"})
    public static void onSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout, RecyclerViewModel recyclerViewModel) {
        recyclerViewModel.mSwipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(recyclerViewModel.mOnSwipeRefreshListener);
    }

    @Bindable
    public boolean getAutoRefreshing() {
        return mAutoRefreshing;
    }

    public void setAbnormalText(String abnormalText) {
        mAbnormalText = abnormalText;
        notifyPropertyChanged(BR.abnormalText);
    }

    @Bindable
    public String getAbnormalText() {
        return mAbnormalText;
    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }

    public RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return mRecyclerViewLayoutManager;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }
}
