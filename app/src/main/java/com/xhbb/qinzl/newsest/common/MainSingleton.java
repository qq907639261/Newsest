package com.xhbb.qinzl.newsest.common;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.evernote.android.job.JobManager;
import com.xhbb.qinzl.newsest.async.MainJobCreator;

/**
 * Created by qinzl on 2017/6/13.
 */

public class MainSingleton {

    private static MainSingleton sMainSingleton;

    private RequestQueue mRequestQueue;

    public static synchronized MainSingleton getInstance(Context context) {
        if (sMainSingleton == null) {
            sMainSingleton = new MainSingleton(context.getApplicationContext());
        }
        return sMainSingleton;
    }

    private MainSingleton(Context context) {
        JobManager.create(context).addJobCreator(new MainJobCreator());
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        mRequestQueue.add(request);
    }
}
