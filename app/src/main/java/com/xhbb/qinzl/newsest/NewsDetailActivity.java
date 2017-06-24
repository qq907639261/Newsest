package com.xhbb.qinzl.newsest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String EXTRA_NEWS_DETAIL_VALUES = "com.xhbb.qinzl.newsest.EXTRA_NEWS_DETAIL_VALUES";

    public static Intent newIntent(Context context, ContentValues newsDetailValues) {
        return new Intent(context, NewsDetailActivity.class)
                .putExtra(EXTRA_NEWS_DETAIL_VALUES, newsDetailValues);
    }

    public static void start(Context context, ContentValues newsDetailValues, @Nullable Bundle options) {
        Intent starter = newIntent(context, newsDetailValues);
        ActivityCompat.startActivity(context, starter, options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_fragment);

        if (savedInstanceState == null) {
            ContentValues newsDetailValues = getIntent().getParcelableExtra(EXTRA_NEWS_DETAIL_VALUES);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, NewsDetailFragment.newInstance(newsDetailValues))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_check_app_upgrade).setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_settings:
                SettingsActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
