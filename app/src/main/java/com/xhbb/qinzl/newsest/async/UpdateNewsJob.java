package com.xhbb.qinzl.newsest.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.xhbb.qinzl.newsest.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by qinzl on 2017/6/12.
 */

public class UpdateNewsJob extends Job {

    public static final String ACTION_NEWS_UPDATED = "com.xhbb.qinzl.newsest.ACTION_NEWS_UPDATED";

    private static final String PARAM_MAIN_JOB_SCHEDULED = "PARAM_MAIN_JOB_SCHEDULED";

    static final String JOB_TAG = "UpdateNewsJob";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        boolean mainJobScheduled = params.getExtras().getBoolean(PARAM_MAIN_JOB_SCHEDULED, false);
        if (mainJobScheduled) {
            schedulePeriodicJob();
            return Result.SUCCESS;
        }

        Context context = getContext();
        String[] newsTypeArray = context.getResources().getStringArray(R.array.news_type);

        try {
            for (String newsType : newsTypeArray) {
                MainTasks.updateNewsData(context, newsType, 1);
            }

            Intent intent = new Intent(ACTION_NEWS_UPDATED);
            String receiverPermission = context.getString(R.string.permission_private);
            context.sendOrderedBroadcast(intent, receiverPermission, null, null, Activity.RESULT_OK, null, null);

            return Result.SUCCESS;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return Result.RESCHEDULE;
        }
    }

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

    private int schedulePeriodicJob() {
        return new JobRequest.Builder(JOB_TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(1))
                .setRequiredNetworkType(JobRequest.NetworkType.NOT_ROAMING)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    public static void cancelJob() {
        JobManager.instance().cancelAllForTag(JOB_TAG);
    }
}
