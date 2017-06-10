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
    private String mErrorText;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ViewListeners.OnScrollListener mOnScrollListener;
    private ViewListeners.OnRefreshListener mOnRefreshListener;

    public NormalRecyclerView(
            RecyclerView.Adapter adapter,
            RecyclerView.LayoutManager layoutManager,
            ViewListeners.OnScrollListener onScrollListener,
            ViewListeners.OnRefreshListener onRefreshListener) {

        mAdapter = adapter;
        mLayoutManager = layoutManager;
        mOnScrollListener = onScrollListener;
        mOnRefreshListener = onRefreshListener;

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

    public ViewListeners.OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public ViewListeners.OnRefreshListener getOnRefreshListener() {
        return mOnRefreshListener;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }
}
