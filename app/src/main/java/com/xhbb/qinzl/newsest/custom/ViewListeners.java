package com.xhbb.qinzl.newsest.custom;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

/**
 * Created by qinzl on 2017/6/6.
 */

public class ViewListeners {

    @BindingAdapter({"android:onScroll"})
    public static void addOnScrollListener(RecyclerView recyclerView,
                                           final OnScrollListener onScrollListener) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                onScrollListener.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onScrollListener.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @BindingAdapter({"android:onRefresh"})
    public static void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout,
                                                 final OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshListener.onSwipeRefresh();
            }
        });
    }

    public interface OnScrollListener {

        void onScrollStateChanged(RecyclerView recyclerView, int newState);
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    public interface OnRefreshListener {

        void onSwipeRefresh();
    }
}
