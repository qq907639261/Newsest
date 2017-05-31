package com.xhbb.qinzl.newsest.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by qinzl on 2017/5/29.
 */

public class MainModel {

    private Context mContext;
    private PagerAdapter mPagerAdapter;

    public MainModel(Context context, PagerAdapter pagerAdapter) {
        mContext = context;
        mPagerAdapter = pagerAdapter;
    }

    public PagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    @BindingAdapter({"android:bindActionBar"})
    public static void setActionBar(Toolbar toolbar, MainModel mainModel) {
        ((AppCompatActivity) mainModel.mContext).setSupportActionBar(toolbar);
    }

    @BindingAdapter({"android:bindViewPager"})
    public static void setupWithViewPager(TabLayout tabLayout, ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }
}
