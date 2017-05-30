package com.xhbb.qinzl.newsest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NewsDetailsActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, NewsDetailsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
    }
}
