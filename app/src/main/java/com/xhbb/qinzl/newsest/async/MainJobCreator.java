package com.xhbb.qinzl.newsest.async;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by qinzl on 2017/6/12.
 */

public class MainJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case UpdateNewsJob.TAG:
                return new UpdateNewsJob();
            default:
                return null;
        }
    }
}
