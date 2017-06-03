package com.xhbb.qinzl.newsest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhbb.qinzl.newsest.common.MainEnum.RefreshState;
import com.xhbb.qinzl.newsest.databinding.ActivityMainBinding;
import com.xhbb.qinzl.newsest.databinding.LayoutRecyclerViewBinding;

public class MainActivity extends AppCompatActivity implements
        NewsMasterFragment.OnNewsMasterFragmentListener {

    private NewsMasterPagerAdapter mNewsMasterPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mToTopFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewPager = binding.viewPager;
        mToTopFab = binding.fab;
        mNewsMasterPagerAdapter = new NewsMasterPagerAdapter(getSupportFragmentManager());

        binding.setPagerAdapter(mNewsMasterPagerAdapter);
        binding.setOnClickFabListener(getOnClickToTopFabListener());
    }

    @NonNull
    private View.OnClickListener getOnClickToTopFabListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsMasterFragment newsMasterFragment = (NewsMasterFragment) mNewsMasterPagerAdapter
                        .instantiateItem(mViewPager, mViewPager.getCurrentItem());
                LayoutRecyclerViewBinding binding = DataBindingUtil.getBinding(newsMasterFragment.getView());

                if (binding != null) {
                    binding.recyclerView.smoothScrollToPosition(0);
                    binding.swipeRefreshLayout.setRefreshing(true);
                    newsMasterFragment.refreshNewsData(RefreshState.SWIPE_REFRESHING);
                }
            }
        };
    }

    @Override
    public void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy < 0 && !mToTopFab.isShown()) {
            mToTopFab.show();
        } else if (dy > 0 && mToTopFab.isShown()) {
            mToTopFab.hide();
        }
    }

    private class NewsMasterPagerAdapter extends FragmentStatePagerAdapter {

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
