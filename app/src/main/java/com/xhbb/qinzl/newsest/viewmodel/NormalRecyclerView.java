package com.xhbb.qinzl.newsest.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

import com.xhbb.qinzl.newsest.BR;

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
    private OnNormalRecyclerViewListener mOnNormalRecyclerViewListener;

    public NormalRecyclerView(RecyclerView.Adapter adapter,
                              RecyclerView.LayoutManager layoutManager,
                              OnNormalRecyclerViewListener onNormalRecyclerViewListener) {

        mAdapter = adapter;
        mLayoutManager = layoutManager;
        mOnNormalRecyclerViewListener = onNormalRecyclerViewListener;

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

    public void onRefresh() {
        mOnNormalRecyclerViewListener.onSwipeRefresh();
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        mOnNormalRecyclerViewListener.onScrollStateChanged(recyclerView, newState);
    }

    public void onScrolled(RecyclerView recyclerView, int dy) {
        mOnNormalRecyclerViewListener.onScrolled(recyclerView, dy);
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public interface OnNormalRecyclerViewListener {

        void onSwipeRefresh();
        void onScrollStateChanged(RecyclerView recyclerView, int newState);
        void onScrolled(RecyclerView recyclerView, int dy);
    }
}
