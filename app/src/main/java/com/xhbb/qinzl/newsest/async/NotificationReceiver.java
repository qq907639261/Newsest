package com.xhbb.qinzl.newsest.async;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (getResultCode() == Activity.RESULT_CANCELED) {
            return;
        }

        switch (intent.getAction()) {
            case UpdateNewsJob.ACTION_NEWS_UPDATED:
                MainNotifications.notifyNewsUpdated(context);
                break;
            default:
        }
    }
}
