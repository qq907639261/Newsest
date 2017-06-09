package com.xhbb.qinzl.newsest.async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.server.JsonUtil;
import com.xhbb.qinzl.newsest.server.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinzl on 2017/5/29.
 */

public class UpdateDataTask {

    public static boolean updateNewsDataAndGetIsPageEqualsTotalPage(Context context, String newsType, int newsPage)
            throws IOException, JSONException {
        String newsResponse = NetworkUtil.getNewsResponse(context, newsType, newsPage);

        List<ContentValues> newsValuesList = new ArrayList<>();
        int totalPage = JsonUtil.fillNewsValuesAndGetTotalPage(newsResponse, newsValuesList, newsType);

        ContentResolver contentResolver = context.getContentResolver();
        if (newsPage == 1) {
            String where = NewsEntry._NEWS_TYPE + "=?";
            String[] selectionArgs = {newsType};
            contentResolver.delete(NewsEntry.URI, where, selectionArgs);
        }

        ContentValues[] newsValuesArray = newsValuesList.toArray(new ContentValues[newsValuesList.size()]);
        contentResolver.bulkInsert(NewsEntry.URI, newsValuesArray);

        return totalPage == newsPage;
    }
}
