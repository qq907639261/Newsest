package com.xhbb.qinzl.newsest.common;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

/**
 * Created by qinzl on 2017/6/4.
 */

public class BindingAdapterContainer {

    @BindingAdapter({"android:bindActionBar"})
    public static void setSupportActionBar(Toolbar toolbar, boolean bindActionBar) {
        if (bindActionBar) {
            Context context = toolbar.getContext();
            ((AppCompatActivity) context).setSupportActionBar(toolbar);
        }
    }

    @BindingAdapter({"android:onScroll"})
    public static void addOnScrollListener(RecyclerView recyclerView,
                                           RecyclerView.OnScrollListener onRecyclerViewScrollListener) {
        recyclerView.addOnScrollListener(onRecyclerViewScrollListener);
    }

    @BindingAdapter({"android:onRefresh"})
    public static void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout,
                                            SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    @BindingAdapter({"android:shown"})
    public static void showOrHideFab(FloatingActionButton fab, boolean showFab) {
        if (!fab.isShown() && showFab) {
            fab.show();
        } else if (fab.isShown() && !showFab) {
            fab.hide();
        }
    }

    @BindingAdapter({"android:smoothScrollToTop"})
    public static void smoothScrollToTop(RecyclerView recyclerView, boolean smoothScrollToTop) {
        if (smoothScrollToTop) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @BindingAdapter({"android:imageObj"})
    public static void loadImageIntoImageView(ImageView imageView, Object imageObj) {
        Context context = imageView.getContext();
        GlideApp.with(context)
                .load(imageObj)
                .into(imageView);
    }
}
