package com.xhbb.qinzl.newsest.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.xhbb.qinzl.newsest.BR;

/**
 * Created by qinzl on 2017/5/29.
 */

public class MainModel extends BaseObservable {

    private PagerAdapter mPagerAdapter;
    private OnMainModelListener mOnMainModelListener;
    private boolean mFabShowed;

    public MainModel(PagerAdapter pagerAdapter, OnMainModelListener onMainModelListener) {
        mPagerAdapter = pagerAdapter;
        mOnMainModelListener = onMainModelListener;
    }

    public void setFabShowed(boolean fabShowed) {
        mFabShowed = fabShowed;
        notifyPropertyChanged(BR.fabShowed);
    }

    @Bindable
    public boolean isFabShowed() {
        return mFabShowed;
    }

    public PagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    public void onClick(ViewPager viewPager) {
        mOnMainModelListener.onClickToTopFab(viewPager);
    }

    public interface OnMainModelListener {

        void onClickToTopFab(ViewPager viewPager);
    }
}
