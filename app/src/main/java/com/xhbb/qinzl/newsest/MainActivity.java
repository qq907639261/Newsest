package com.xhbb.qinzl.newsest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhbb.qinzl.newsest.databinding.ActivityMainBinding;
import com.xhbb.qinzl.newsest.databinding.LayoutRecyclerViewBinding;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        NewsMasterFragment.OnNewsMasterFragmentListener {

    private NewsMasterPagerAdapter mNewsMasterPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewPager = binding.viewPager;
        mFab = binding.fab;
        mNewsMasterPagerAdapter = new NewsMasterPagerAdapter(getSupportFragmentManager());

        binding.setPagerAdapter(mNewsMasterPagerAdapter);
        binding.setOnClickFabListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment newsMasterFragment = (Fragment) mNewsMasterPagerAdapter
                .instantiateItem(mViewPager, mViewPager.getCurrentItem());
        LayoutRecyclerViewBinding binding = DataBindingUtil.getBinding(newsMasterFragment.getView());

        if (binding != null) {
            binding.getRecyclerViewModel().getRecyclerView().smoothScrollToPosition(0);
        }
    }

    @Override
    public void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy < 0 && !mFab.isShown()) {
            mFab.show();
        } else if (dy > 0 && mFab.isShown()) {
            mFab.hide();
        }
    }

    private class NewsMasterPagerAdapter extends FragmentPagerAdapter {

        private String[] mNewsTypeArray;

        NewsMasterPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mNewsTypeArray = getResources().getStringArray(R.array.news_type);
        }

        @Override
        public Fragment getItem(int position) {
            return NewsMasterFragment.newInstance(mNewsTypeArray[position]);
        }

        @Override
        public int getCount() {
            return mNewsTypeArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTypeArray[position];
        }
    }
}
