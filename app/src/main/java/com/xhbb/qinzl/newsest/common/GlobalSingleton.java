package com.xhbb.qinzl.newsest.common;

import android.content.Context;

import com.evernote.android.job.JobManager;
import com.xhbb.qinzl.newsest.async.MainJobCreator;

/**
 * Created by qinzl on 2017/6/13.
 */

public class GlobalSingleton {

    private static GlobalSingleton sGlobalSingleton;

    public static GlobalSingleton getInstance(Context context) {
        if (sGlobalSingleton == null) {
            sGlobalSingleton = new GlobalSingleton(context);
        }
        return sGlobalSingleton;
    }

    private GlobalSingleton(Context context) {
        JobManager.create(context).addJobCreator(new MainJobCreator());
    }
}
