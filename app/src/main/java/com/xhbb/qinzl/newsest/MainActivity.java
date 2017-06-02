package com.xhbb.qinzl.newsest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.xhbb.qinzl.newsest.viewmodel.RecyclerViewModel;

public class MainActivity extends AppCompatActivity implements
        NewsMasterFragment.OnNewsMasterFragmentListener {

    private NewsMasterPagerAdapter mNewsMasterPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mStickTopFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewPager = binding.viewPager;
        mStickTopFab = binding.fab;
        mNewsMasterPagerAdapter = new NewsMasterPagerAdapter(getSupportFragmentManager());

        binding.setPagerAdapter(mNewsMasterPagerAdapter);
        binding.setOnClickFabListener(getOnClickStickTopFabListener());
    }

    @NonNull
    private View.OnClickListener getOnClickStickTopFabListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsMasterFragment newsMasterFragment = (NewsMasterFragment) mNewsMasterPagerAdapter
                        .instantiateItem(mViewPager, mViewPager.getCurrentItem());

                LayoutRecyclerViewBinding binding = DataBindingUtil.getBinding(newsMasterFragment.getView());
                if (binding != null) {
                    RecyclerViewModel recyclerViewModel = binding.getRecyclerViewModel();

                    recyclerViewModel.getRecyclerView().scrollToPosition(0);
                    recyclerViewModel.getSwipeRefreshLayout().setRefreshing(true);
                    newsMasterFragment.onRefresh();
                }
            }
        };
    }

    @Override
    public void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy < 0 && !mStickTopFab.isShown()) {
            mStickTopFab.show();
        } else if (dy > 0 && mStickTopFab.isShown()) {
            mStickTopFab.hide();
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
