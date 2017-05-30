package com.xhbb.qinzl.newsest.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

import com.xhbb.qinzl.newsest.BR;

/**
 * Created by qinzl on 2017/5/29.
 */

public class NewsMaster extends BaseObservable {

    private Context mContext;
    private boolean mAutoRefreshing;
    private String mNetworkingFailedText;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;

    public NewsMaster(Context context, RecyclerView.Adapter recyclerViewAdapter,
                      RecyclerView.LayoutManager recyclerViewLayoutManager) {
        mContext = context;
        mRecyclerViewAdapter = recyclerViewAdapter;
        mRecyclerViewLayoutManager = recyclerViewLayoutManager;

        mAutoRefreshing = true;
        mNetworkingFailedText = "";
    }

    public void setAutoRefreshing(boolean autoRefreshing) {
        mAutoRefreshing = autoRefreshing;
        notifyPropertyChanged(BR.autoRefreshing);
    }

    @Bindable
    public boolean getAutoRefreshing() {
        return mAutoRefreshing;
    }

    public void setNetworkingFailedText(String networkingFailedText) {
        mNetworkingFailedText = networkingFailedText;
        notifyPropertyChanged(BR.networkingFailedText);
    }

    @Bindable
    public String getNetworkingFailedText() {
        return mNetworkingFailedText;
    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }

    public RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return mRecyclerViewLayoutManager;
    }
}
