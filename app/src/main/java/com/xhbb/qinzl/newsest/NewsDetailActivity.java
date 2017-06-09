package com.xhbb.qinzl.newsest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String EXTRA_NEWS_DETAIL_VALUES = "com.xhbb.qinzl.newsest.EXTRA_NEWS_DETAIL_VALUES";

    public static void start(Context context, ContentValues newsDetailValues, @Nullable Bundle options) {
        Intent starter = new Intent(context, NewsDetailActivity.class);
        starter.putExtra(EXTRA_NEWS_DETAIL_VALUES, newsDetailValues);
        ActivityCompat.startActivity(context, starter, options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            ContentValues newsDetailValues = getIntent().getParcelableExtra(EXTRA_NEWS_DETAIL_VALUES);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, NewsDetailFragment.newInstance(newsDetailValues))
                    .commit();
        }
    }
}
