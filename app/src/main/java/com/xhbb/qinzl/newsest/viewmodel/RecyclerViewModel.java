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
    private String mErrorText;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.OnScrollListener mOnRecyclerViewScrollListener;
    private SwipeRefreshLayout.OnRefreshListener mOnSwipeRefreshListener;

    public RecyclerViewModel(RecyclerView.Adapter recyclerViewAdapter,
                             RecyclerView.LayoutManager recyclerViewLayoutManager,
                             RecyclerView.OnScrollListener onRecyclerViewScrollListener,
                             SwipeRefreshLayout.OnRefreshListener onSwipeRefreshListener) {
        mRecyclerViewAdapter = recyclerViewAdapter;
        mRecyclerViewLayoutManager = recyclerViewLayoutManager;
        mOnRecyclerViewScrollListener = onRecyclerViewScrollListener;
        mOnSwipeRefreshListener = onSwipeRefreshListener;

        mAutoRefreshing = true;
    }

    @BindingAdapter({"android:onRecyclerViewScrolled"})
    public static void onRecyclerViewScrolled(RecyclerView recyclerView, RecyclerViewModel recyclerViewModel) {
        recyclerView.addOnScrollListener(recyclerViewModel.mOnRecyclerViewScrollListener);
    }

    @BindingAdapter({"android:onSwipeRefresh"})
    public static void onSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout, RecyclerViewModel recyclerViewModel) {
        recyclerViewModel.mSwipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(recyclerViewModel.mOnSwipeRefreshListener);
    }

    public void setAutoRefreshing(boolean autoRefreshing) {
        mAutoRefreshing = autoRefreshing;
        notifyPropertyChanged(BR.autoRefreshing);
    }

    @Bindable
    public boolean isAutoRefreshing() {
        return mAutoRefreshing;
    }

    public void setErrorText(String errorText) {
        mErrorText = errorText;
        notifyPropertyChanged(BR.errorText);
    }

    @Bindable
    public String getErrorText() {
        return mErrorText;
    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }

    public RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return mRecyclerViewLayoutManager;
    }

    @Bindable
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }
}
