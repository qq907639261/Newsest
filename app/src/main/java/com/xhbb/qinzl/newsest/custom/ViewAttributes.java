package com.xhbb.qinzl.newsest.custom;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.xhbb.qinzl.newsest.R;
import com.xhbb.qinzl.newsest.common.GlideApp;

/**
 * Created by qinzl on 2017/6/4.
 */

public class ViewAttributes {

    @BindingAdapter(value = {"android:bindActionBar", "android:displayHomeAsUpEnabled"},
            requireAll = false)
    public static void setActionBar(Toolbar toolbar, boolean bindActionBar, boolean displayHomeAsUpEnabled) {
        if (bindActionBar) {
            AppCompatActivity activity = (AppCompatActivity) toolbar.getContext();

            activity.setSupportActionBar(toolbar);

            ActionBar actionBar = activity.getSupportActionBar();
            assert actionBar != null;

            if (displayHomeAsUpEnabled) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_home_as_up);
            }
        }
    }

    @BindingAdapter({"android:shown"})
    public static void showOrHideFab(FloatingActionButton fab, boolean showFab) {
        if (showFab) {
            fab.show();
        } else {
            fab.hide();
        }
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
}
