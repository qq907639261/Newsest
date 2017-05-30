package com.xhbb.qinzl.newsest.async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.server.JsonUtils;
import com.xhbb.qinzl.newsest.server.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by qinzl on 2017/5/29.
 */

public class UpdateDataTask {

    public static boolean downloadNewsDataIntoDatabase(Context context, String newsType, int newsPage)
            throws IOException, JSONException {
        String newsResponse = NetworkUtils.getNewsResponse(context, newsType, newsPage);
        ContentValues[] newsValuesArray = JsonUtils.getNewsValuesArray(newsResponse, newsType);

        if (newsValuesArray == null) {
            return false;
        }

        ContentResolver contentResolver = context.getContentResolver();
        if (newsPage == 1) {
            String where = NewsEntry._NEWS_TYPE + "=?";
            String[] selectionArgs = {newsType};
            contentResolver.delete(NewsEntry.URI, where, selectionArgs);
        }
        contentResolver.bulkInsert(NewsEntry.URI, newsValuesArray);

        return true;
    }
}