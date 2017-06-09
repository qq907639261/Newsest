package com.xhbb.qinzl.newsest.custom;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

/**
 * Created by qinzl on 2017/6/6.
 */

public class ViewListeners {

    @BindingAdapter(value = {"android:onScrollListener", "android:removeOnScrollListener"},
            requireAll = false)
    public static void addOrRemoveOnScrollListener(RecyclerView recyclerView,
                                                   RecyclerView.OnScrollListener onScrollListener,
                                                   boolean removeOnScrollListener) {
        if (removeOnScrollListener) {
            recyclerView.removeOnScrollListener(onScrollListener);
        } else {
            recyclerView.addOnScrollListener(onScrollListener);
        }
    }

    @BindingAdapter({"android:onRefresh"})
    public static void setOnSwipeRefreshListener(SwipeRefreshLayout swipeRefreshLayout,
                                                 final OnSwipeRefreshListener onSwipeRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefreshListener.onSwipeRefresh();
            }
        });
    }

    public interface OnSwipeRefreshListener {

        void onSwipeRefresh();
    }
}
