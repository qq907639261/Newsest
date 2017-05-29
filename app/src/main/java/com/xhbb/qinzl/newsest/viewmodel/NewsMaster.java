package com.xhbb.qinzl.newsest.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.xhbb.qinzl.newsest.BR;

/**
 * Created by qinzl on 2017/5/29.
 */

public class NewsMaster extends BaseObservable {

    private Context mContext;
    private boolean mAutoRefreshing;
    private String mNetworkingFailedText;

    public NewsMaster(Context context) {
        mContext = context;
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
}
