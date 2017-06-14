package com.xhbb.qinzl.newsest.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.xhbb.qinzl.newsest.R;

import java.io.BufferedReader;
import java.io.IOException;
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

        return getResponseFromHttpUrl(spec, appCode);
    }

    public static boolean isAppUpdated() {
        try {
            URL url = new URL("http://192.168.31.35");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1000);

            getResponseFromHttpUrl(connection);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isNetworkConnectedOrConnecting(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @NonNull
    private static String getResponseFromHttpUrl(String spec, String appCode) throws IOException {
        URL url = new URL(spec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "APPCODE " + appCode);

        return getResponseFromHttpUrl(connection);
    }

    @NonNull
    private static String getResponseFromHttpUrl(HttpURLConnection connection) throws IOException {
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
}
