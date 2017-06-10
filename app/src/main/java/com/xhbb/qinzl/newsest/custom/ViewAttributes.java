package com.xhbb.qinzl.newsest.custom;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    @BindingAdapter({"android:showed"})
    public static void showOrHideFab(FloatingActionButton fab, boolean fabShowed) {
        if (fabShowed) {
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

    @BindingAdapter({"android:requestFocus"})
    public static void requestFocus(View view, boolean requestFocus) {
        if (requestFocus) {
            view.requestFocus();
        }
    }

    @BindingAdapter({"android:softInputShowed"})
    public static void showOrHideSoftInput(View view, boolean softInputShowed) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (softInputShowed) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
