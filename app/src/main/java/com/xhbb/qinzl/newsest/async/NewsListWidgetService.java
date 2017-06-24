package com.xhbb.qinzl.newsest.async;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.volley.Response;
import com.xhbb.qinzl.newsest.NewsDetailActivity;
import com.xhbb.qinzl.newsest.R;
import com.xhbb.qinzl.newsest.common.ValuesUtils;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.server.JsonUtils;
import com.xhbb.qinzl.newsest.server.NetworkUtils;

public class NewsListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewsListRemoteViewsFactory(getApplicationContext());
    }

    class NewsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory,
            Response.Listener<String> {

        private Context mContext;
        private Cursor mCursor;

        public NewsListRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {
            NetworkUtils.addNewsRequestToRequestQueue(mContext, mContext.getString(R.string.technology), 1, this, null);
        }

        @Override
        public void onResponse(String response) {
            ContentValues[] newsValueses = JsonUtils.getNewsValueses(response, mContext.getString(R.string.technology));
            MainTasks.updateNewsData(mContext, newsValueses, 1);
        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) {
                mCursor.close();
            }

            long callingIdentity = Binder.clearCallingIdentity();
            mCursor = mContext.getContentResolver().query(NewsEntry.URI, null, null, null, null);
            Binder.restoreCallingIdentity(callingIdentity);
        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            mCursor.moveToPosition(i);

            ContentValues newsValues = ValuesUtils.getNewsValues(mCursor);
            Intent fillInIntent = NewsDetailActivity.newIntent(mContext, newsValues);
            String title = mCursor.getString(mCursor.getColumnIndex(NewsEntry._TITLE));

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_news);
            views.setTextViewText(R.id.title, title);
            views.setOnClickFillInIntent(R.id.title, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
