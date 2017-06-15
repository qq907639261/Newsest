package com.xhbb.qinzl.newsest.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.xhbb.qinzl.newsest.R;
import com.xhbb.qinzl.newsest.data.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qinzl on 2017/5/27.
 */

public class NetworkUtils {

    public static final int NEWS_COUNT_OF_EACH_PAGE = 15;

    public static String getNewsResponse(Context context, String newsType, int page)
            throws IOException {
        String appCode = context.getString(R.string.news_app_code);
        String spec = Uri.parse("http://ali-news.showapi.com")
                .buildUpon()
                .appendPath("newsList")
                .appendQueryParameter("maxResult", String.valueOf(NEWS_COUNT_OF_EACH_PAGE))
                .appendQueryParameter("channelName", newsType)
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("needContent", "1")
                .appendQueryParameter("needHtml", "1")
                .appendQueryParameter("needAllList", "0")
                .build().toString();

        URL url = new URL(spec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "APPCODE " + appCode);

        return getResponseFromHttpUrl(context, connection);
    }

    public static File getTheLatestApkFile(Context context) throws IOException {
        if (!isNetworkConnectedOrConnecting(context)) {
            throw new IOException();
        }

        String fileName = context.getString(R.string.apk_file_name);

        URL url = new URL("http://10.0.2.2/" + fileName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        FileOutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            connection.setRequestMethod("GET");

            int contentLength = connection.getContentLength();
            long externalAvailableSize = FileUtils.getExternalAvailableSize();

            if (contentLength > externalAvailableSize) {
                return null;
            }

            File file = FileUtils.getTheLatestApkFile(context);
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(file);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }

            return file;
        } finally {
            connection.disconnect();
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static boolean isAppUpgraded(Context context) {
        try {
            URL url = new URL("http://10.0.2.2");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1000);

            getResponseFromHttpUrl(context, connection);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @NonNull
    private static String getResponseFromHttpUrl(Context context, HttpURLConnection connection)
            throws IOException {
        if (!isNetworkConnectedOrConnecting(context)) {
            throw new IOException();
        }

        BufferedReader reader = null;
        try {
            connection.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String readLine;
            while ((readLine = reader.readLine()) != null) {
                response.append(readLine);
            }

            return response.toString();
        } finally {
            connection.disconnect();
            if (reader != null) {
                reader.close();
            }
        }
    }

    private static boolean isNetworkConnectedOrConnecting(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
