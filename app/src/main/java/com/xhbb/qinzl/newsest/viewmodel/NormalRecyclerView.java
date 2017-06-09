package com.xhbb.qinzl.newsest.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

import com.xhbb.qinzl.newsest.BR;
import com.xhbb.qinzl.newsest.custom.ViewListeners;

/**
 * Created by qinzl on 2017/5/29.
 */

public class NormalRecyclerView extends BaseObservable {

    private boolean mAutoRefreshing;
    private boolean mSwipeRefreshing;
    private boolean mSmoothScrollToTop;
    private boolean mRemoveOnScrollListener;
    private String mErrorText;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private ViewListeners.OnSwipeRefreshListener mOnSwipeRefreshListener;

    public NormalRecyclerView(
            RecyclerView.Adapter recyclerViewAdapter,
            RecyclerView.LayoutManager recyclerViewLayoutManager,
            RecyclerView.OnScrollListener onScrollListener,
            ViewListeners.OnSwipeRefreshListener onSwipeRefreshListener) {

        mRecyclerViewAdapter = recyclerViewAdapter;
        mRecyclerViewLayoutManager = recyclerViewLayoutManager;
        mOnScrollListener = onScrollListener;
        mOnSwipeRefreshListener = onSwipeRefreshListener;

        mAutoRefreshing = true;
    }

    public void setSmoothScrollToTop(boolean smoothScrollToTop) {
        mSmoothScrollToTop = smoothScrollToTop;
        notifyPropertyChanged(BR.smoothScrollToTop);
    }

    @Bindable
    public boolean isSmoothScrollToTop() {
        return mSmoothScrollToTop;
    }

    public void setAutoRefreshing(boolean autoRefreshing) {
        mAutoRefreshing = autoRefreshing;
        notifyPropertyChanged(BR.autoRefreshing);
    }

    @Bindable
    public boolean isAutoRefreshing() {
        return mAutoRefreshing;
    }

    public void setSwipeRefreshing(boolean swipeRefreshing) {
        mSwipeRefreshing = swipeRefreshing;
        notifyPropertyChanged(BR.swipeRefreshing);
    }

    @Bindable
    public boolean isSwipeRefreshing() {
        return mSwipeRefreshing;
    }

    public void setErrorText(String errorText) {
        mErrorText = errorText;
        notifyPropertyChanged(BR.errorText);
    }

    @Bindable
    public String getErrorText() {
        return mErrorText;
    }

    public RecyclerView.OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void setRemoveOnScrollListener(boolean removeOnScrollListener) {
        mRemoveOnScrollListener = removeOnScrollListener;
        notifyPropertyChanged(BR.removeOnScrollListener);
    }

    @Bindable
    public boolean isRemoveOnScrollListener() {
        return mRemoveOnScrollListener;
    }

    public ViewListeners.OnSwipeRefreshListener getOnSwipeRefreshListener() {
        return mOnSwipeRefreshListener;
    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }

    public RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return mRecyclerViewLayoutManager;
    }
}
