package com.xhbb.qinzl.newsest.custom;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

/**
 * Created by qinzl on 2017/6/6.
 */

public class ViewListeners {

    @BindingAdapter({"android:onRefresh"})
    public static void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout,
                                            SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    @BindingAdapter(value = {"android:onScrollStateChanged", "android:onScrolled"}, requireAll = false)
    public static void addOnScrollListener(RecyclerView recyclerView,
                                           final OnScrollStateChangedListener onScrollStateChangedListener,
                                           final OnScrolledListener onScrolledListener) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onScrollStateChangedListener != null) {
                    onScrollStateChangedListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrolledListener != null) {
                    onScrolledListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    public interface OnScrollStateChangedListener {

        void onScrollStateChanged(RecyclerView recyclerView, int newState);
    }

    public interface OnScrolledListener {

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }
}
