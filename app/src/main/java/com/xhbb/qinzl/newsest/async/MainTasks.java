package com.xhbb.qinzl.newsest.async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.xhbb.qinzl.newsest.BuildConfig;
import com.xhbb.qinzl.newsest.R;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.server.JsonUtils;
import com.xhbb.qinzl.newsest.server.NetworkUtils;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

/**
 * Created by qinzl on 2017/5/29.
 */

public class MainTasks {

    public static void updateNewsData(Context context, String newsType, int newsPage)
            throws IOException, JSONException {
        String newsResponse = NetworkUtils.getNewsResponse(context, newsType, newsPage);
        ContentValues[] newsValuesArray = JsonUtils.getNewsValuesArray(newsResponse, newsType);

        ContentResolver contentResolver = context.getContentResolver();
        if (newsPage == 1) {
            String where = NewsEntry._NEWS_TYPE + "=?";
            String[] selectionArgs = {newsType};
            contentResolver.delete(NewsEntry.URI, where, selectionArgs);
        }
        contentResolver.bulkInsert(NewsEntry.URI, newsValuesArray);
    }

    static void downloadAndSetupApk(Context context) throws IOException {
        File apkFile = NetworkUtils.getTheLatestApkFile(context);

        if (apkFile == null) {
            Toast.makeText(context, R.string.upgrade_failed_because_size_not_enough_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".FileProvider", apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setData(uri);

        context.startActivity(intent);
    }
}
