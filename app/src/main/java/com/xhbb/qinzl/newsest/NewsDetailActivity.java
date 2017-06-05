package com.xhbb.qinzl.newsest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.xhbb.qinzl.newsest.viewmodel.News;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String EXTRA_NEWS = "com.xhbb.qinzl.newsest.EXTRA_NEWS";

    public static void start(Context context, News news) {
        Intent starter = new Intent(context, NewsDetailActivity.class);
        starter.putExtra(EXTRA_NEWS, news);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            News news = getIntent().getParcelableExtra(EXTRA_NEWS);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, NewsDetailFragment.newInstance(news))
                    .commit();
        }
    }
}
