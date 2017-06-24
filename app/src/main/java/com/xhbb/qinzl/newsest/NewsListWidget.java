package com.xhbb.qinzl.newsest;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.xhbb.qinzl.newsest.async.NewsListWidgetService;
import com.xhbb.qinzl.newsest.async.UpdateNewsJob;

public class NewsListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.news_list_widget);

        Intent intent = new Intent(context, NewsListWidgetService.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(new Intent(context, NewsDetailActivity.class))
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setEmptyView(R.id.list_view, R.id.empty_view);
        views.setPendingIntentTemplate(R.id.list_view, pendingIntent);
        views.setRemoteAdapter(R.id.list_view, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case UpdateNewsJob.ACTION_NEWS_UPDATED:
                notifyDataChanged(context);
                break;
            default:
                super.onReceive(context, intent);
        }
    }

    private void notifyDataChanged(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, getClass());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
    }
}

