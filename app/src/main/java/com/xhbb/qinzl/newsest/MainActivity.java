package com.xhbb.qinzl.newsest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.xhbb.qinzl.newsest.databinding.ActivityMainBinding;
import com.xhbb.qinzl.newsest.viewmodel.MainModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        NewsMasterPagerAdapter newsMasterPagerAdapter = new NewsMasterPagerAdapter(getSupportFragmentManager());
        MainModel mainModel = new MainModel(this, newsMasterPagerAdapter);

        binding.setMainModel(mainModel);
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
