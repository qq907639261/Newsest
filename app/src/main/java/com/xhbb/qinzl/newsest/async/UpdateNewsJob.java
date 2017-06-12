package com.xhbb.qinzl.newsest.async;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.xhbb.qinzl.newsest.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by qinzl on 2017/6/12.
 */

public class UpdateNewsJob extends Job {

    static final String TAG = "UpdateNewsJob";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Context context = getContext();
        String[] newsTypeArray = context.getResources().getStringArray(R.array.news_type);

        try {
            for (String newsType : newsTypeArray) {
                UpdateDataTasks.updateNewsData(context, newsType, 1);
            }

            // TODO: 2017/6/12 通知 有序广播
            MainNotifications.notifyNewsUpdated(context);

            return Result.SUCCESS;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return Result.RESCHEDULE;
        }
    }

    public static int scheduleJob() {
        return new JobRequest.Builder(TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(30))
                .setRequiredNetworkType(JobRequest.NetworkType.NOT_ROAMING)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    public static void cancelJob() {
        JobManager.instance().cancelAllForTag(TAG);
    }
}
