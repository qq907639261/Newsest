package com.xhbb.qinzl.newsest;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.xhbb.qinzl.newsest.async.MainNotifications;
import com.xhbb.qinzl.newsest.async.UpdateNewsJob;
import com.xhbb.qinzl.newsest.common.GlobalSingleton;
import com.xhbb.qinzl.newsest.common.MainEnums.RefreshState;
import com.xhbb.qinzl.newsest.databinding.ActivityMainBinding;
import com.xhbb.qinzl.newsest.databinding.FragmentNormalRecyclerViewBinding;
import com.xhbb.qinzl.newsest.viewmodel.MainModel;
import com.xhbb.qinzl.newsest.viewmodel.NormalRecyclerView;

public class MainActivity extends AppCompatActivity implements
        NewsMasterFragment.OnNewsMasterFragmentListener,
        MainModel.OnMainModelListener,
        Application.ActivityLifecycleCallbacks {

    private NewsMasterPagerAdapter mNewsMasterPagerAdapter;
    private MainModel mMainModel;
    private Activity mStartedActivity;
    private LocalNotificationReceiver mLocalNotificationReceiver;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        GlobalSingleton.getInstance(getApplicationContext());

        mNewsMasterPagerAdapter = new NewsMasterPagerAdapter(getSupportFragmentManager());
        mMainModel = new MainModel(mNewsMasterPagerAdapter, this);
        mLocalNotificationReceiver = new LocalNotificationReceiver();
        mStartedActivity = this;

        getApplication().registerActivityLifecycleCallbacks(this);
        binding.setMainModel(mMainModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplication().unregisterActivityLifecycleCallbacks(this);
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
            mMainModel.setFabShowed(true);
        } else {
            mMainModel.setFabShowed(false);
        }
    }

    @Override
    public void onClickToTopFab(ViewPager viewPager) {
        NewsMasterFragment newsMasterFragment = (NewsMasterFragment) mNewsMasterPagerAdapter
                .instantiateItem(viewPager, viewPager.getCurrentItem());
        FragmentNormalRecyclerViewBinding binding = DataBindingUtil.getBinding(newsMasterFragment.getView());

        if (binding != null) {
            NormalRecyclerView normalRecyclerView = binding.getNormalRecyclerView();
            normalRecyclerView.setSmoothScrollToTop(true);
            normalRecyclerView.setSwipeRefreshing(true);
            newsMasterFragment.refreshNewsData(RefreshState.SWIPE_REFRESHING);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (bundle != null) {
            mStartedActivity = activity;
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mStartedActivity != activity) {
            mStartedActivity = activity;
            return;
        }

        MainNotifications.cancel(this);
        UpdateNewsJob.cancelJob();

        IntentFilter filter = new IntentFilter(UpdateNewsJob.ACTION_NEWS_UPDATED);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        String permission = getString(R.string.permission_private);
        registerReceiver(mLocalNotificationReceiver, filter, permission, null);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity != mStartedActivity) {
            return;
        }

        UpdateNewsJob.scheduleMainJob();
        unregisterReceiver(mLocalNotificationReceiver);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

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

    private class LocalNotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setResultCode(Activity.RESULT_CANCELED);
        }
    }
}
