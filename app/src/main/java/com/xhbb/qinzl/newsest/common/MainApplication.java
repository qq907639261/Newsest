package com.xhbb.qinzl.newsest.common;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.xhbb.qinzl.newsest.async.MainJobCreator;

/**
 * Created by qinzl on 2017/6/12.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new MainJobCreator());
    }
}
