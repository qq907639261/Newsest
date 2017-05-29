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

    public static boolean updateNewsFromServer(Context context, String newsType, int page)
            throws IOException, JSONException {
        String newsResponse = NetworkUtils.getNewsResponse(context, newsType, page);
        ContentValues[] newsValuesArray = JsonUtils.getNewsValuesArray(newsResponse);

        if (newsValuesArray == null) {
            return false;
        }

        ContentResolver contentResolver = context.getContentResolver();
        if (page == 1) {
            contentResolver.delete(NewsEntry.URI, null, null);
        }
        contentResolver.bulkInsert(NewsEntry.URI, newsValuesArray);

        return true;
    }
}
