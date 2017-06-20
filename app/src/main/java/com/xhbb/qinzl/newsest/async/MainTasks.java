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
import com.xhbb.qinzl.newsest.server.NetworkUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by qinzl on 2017/5/29.
 */

public class MainTasks {

    public static void updateNewsData(final Context context, final ContentValues[] newsValueses,
                                      final int newsPage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = context.getContentResolver();
                if (newsPage == 1) {
                    String newsType = newsValueses[0].getAsString(NewsEntry._NEWS_TYPE);

                    String where = NewsEntry._NEWS_TYPE + "=?";
                    String[] selectionArgs = {newsType};
                    contentResolver.delete(NewsEntry.URI, where, selectionArgs);
                }
                contentResolver.bulkInsert(NewsEntry.URI, newsValueses);
            }
        }).start();
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
