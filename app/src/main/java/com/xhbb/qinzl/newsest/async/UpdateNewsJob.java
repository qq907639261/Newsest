package com.xhbb.qinzl.newsest.async;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.xhbb.qinzl.newsest.R;
import com.xhbb.qinzl.newsest.server.JsonUtils;
import com.xhbb.qinzl.newsest.server.NetworkUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by qinzl on 2017/6/12.
 */

public class UpdateNewsJob extends Job {

    public static final String ACTION_NEWS_UPDATED = "com.xhbb.qinzl.newsest.ACTION_NEWS_UPDATED";
    static final String JOB_TAG = "UpdateNewsJob";

    private static final String PARAM_MAIN_JOB_SCHEDULED = "PARAM_MAIN_JOB_SCHEDULED";

    public static int scheduleMainJob() {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putBoolean(PARAM_MAIN_JOB_SCHEDULED, true);

        return new JobRequest.Builder(JOB_TAG)
                .setExact(TimeUnit.MINUTES.toMillis(15))
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setExtras(extras)
                .build()
                .schedule();
    }

    public static void cancelJob() {
        JobManager.instance().cancelAllForTag(JOB_TAG);
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        boolean mainJobScheduled = params.getExtras().getBoolean(PARAM_MAIN_JOB_SCHEDULED, false);
        if (mainJobScheduled) {
            schedulePeriodicJob();
            return Result.SUCCESS;
        }

        Context context = getContext();

        if (!NetworkUtils.isNetworkAvailable(context)) {
            return Result.RESCHEDULE;
        }

        String[] newsTypes = context.getResources().getStringArray(R.array.news_type);
        for (String newsType : newsTypes) {
            NetworkUtils.addNewsRequestToRequestQueue(context, newsType, 1, getListener(newsType), null);
        }

        Intent intent = new Intent(ACTION_NEWS_UPDATED);
        String receiverPermission = context.getString(R.string.permission_private);
        context.sendOrderedBroadcast(intent, receiverPermission, null, null, Activity.RESULT_OK, null, null);

        return Result.SUCCESS;
    }

    @NonNull
    private Response.Listener<String> getListener(final String newsType) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ContentValues[] newsValueses = JsonUtils.getNewsValueses(response, newsType);
                MainTasks.updateNewsData(getContext(), newsValueses, 1);
            }
        };
    }

    private int schedulePeriodicJob() {
        return new JobRequest.Builder(JOB_TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(1))
                .setRequiredNetworkType(JobRequest.NetworkType.NOT_ROAMING)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}
