package com.xhbb.qinzl.newsest.common;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.xhbb.qinzl.newsest.R;

/**
 * Created by qinzl on 2017/6/4.
 */

public class BindingAdapterContainer {

    @BindingAdapter(value = {"android:bindActionBar", "android:displayHomeAsUpEnabled"},
            requireAll = false)
    public static void setActionBar(Toolbar toolbar, boolean bindActionBar, boolean displayHomeAsUpEnabled) {
        if (bindActionBar) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) toolbar.getContext();
            appCompatActivity.setSupportActionBar(toolbar);

            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            assert actionBar != null;

            if (displayHomeAsUpEnabled) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_home_as_up);
            }
        }
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

    @BindingAdapter({"android:onScroll"})
    public static void addOnScrollListener(RecyclerView recyclerView,
                                           RecyclerView.OnScrollListener onRecyclerViewScrollListener) {
        recyclerView.addOnScrollListener(onRecyclerViewScrollListener);
    }

    @BindingAdapter({"android:smoothScrollToTop"})
    public static void smoothScrollToTop(RecyclerView recyclerView, boolean smoothScrollToTop) {
        if (smoothScrollToTop) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @BindingAdapter(value = {"android:imageObj", "android:placeHolderSrc", "android:errorSrc"},
            requireAll = false)
    public static void loadImageIntoImageView(ImageView imageView, Object imageObj,
                                              Drawable placeHolderDrawable, Drawable errorDrawable) {
        Context context = imageView.getContext();
        GlideApp.with(context)
                .load(imageObj)
                .placeholder(placeHolderDrawable)
                .error(errorDrawable)
                .into(imageView);
    }

    @BindingAdapter({"android:heightByWidth"})
    public static void setImageHeightByImageWidth(ImageView imageView, int imageWidth) {
        int imageHeight = imageWidth * 2 / 3;
        imageView.setMaxHeight(imageHeight);
        imageView.setMinimumHeight(imageHeight);
    }
}
