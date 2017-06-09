package com.xhbb.qinzl.newsest;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.xhbb.qinzl.newsest.common.MainEnum.RefreshState;
import com.xhbb.qinzl.newsest.databinding.ActivityMainBinding;
import com.xhbb.qinzl.newsest.databinding.LayoutNormalRecyclerViewBinding;
import com.xhbb.qinzl.newsest.viewmodel.MainModel;
import com.xhbb.qinzl.newsest.viewmodel.NormalRecyclerView;

public class MainActivity extends AppCompatActivity implements
        NewsMasterFragment.OnNewsMasterFragmentListener,
        MainModel.OnMainModelListener {

    private NewsMasterPagerAdapter mNewsMasterPagerAdapter;
    private MainModel mMainModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mNewsMasterPagerAdapter = new NewsMasterPagerAdapter(getSupportFragmentManager());
        mMainModel = new MainModel(mNewsMasterPagerAdapter, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_start);
            getWindow().setExitTransition(transition);
        }

        binding.setMainModel(mMainModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_item_share).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                SettingsActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager linearLayoutManager, int dy) {
        if (dy < 0 && linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
            mMainModel.setShowFab(true);
        } else {
            mMainModel.setShowFab(false);
        }
    }

    @Override
    public void onClickToTopFab(ViewPager viewPager) {
        NewsMasterFragment newsMasterFragment = (NewsMasterFragment) mNewsMasterPagerAdapter
                .instantiateItem(viewPager, viewPager.getCurrentItem());
        LayoutNormalRecyclerViewBinding binding = DataBindingUtil.getBinding(newsMasterFragment.getView());

        if (binding != null) {
            NormalRecyclerView normalRecyclerView = binding.getNormalRecyclerView();
            normalRecyclerView.setSmoothScrollToTop(true);
            normalRecyclerView.setSwipeRefreshing(true);
            newsMasterFragment.refreshNewsData(RefreshState.SWIPE_REFRESHING);
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
