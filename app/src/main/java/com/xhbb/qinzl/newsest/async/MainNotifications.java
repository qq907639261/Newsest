package com.xhbb.qinzl.newsest.async;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.xhbb.qinzl.newsest.MainActivity;
import com.xhbb.qinzl.newsest.R;

public class MainNotifications {

    private static final String NOTIFICATION_TAG = "MainNotifications";

    static void notifyNewsUpdated(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_news_updated)
                .setContentTitle("更新提醒")
                .setContentText("新闻内容已更新，请点击查看。")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setTicker("新闻内容已更新，请点击查看。")
                .setContentIntent(
                        TaskStackBuilder.create(context)
                                .addNextIntent(MainActivity.newIntent(context))
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    private static void notify(Context context, Notification notification) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TAG, 0, notification);
    }

    public static void cancel(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, 0);
    }
}
