package com.xhbb.qinzl.newsest.server;

import android.content.Context;
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

    public static String getNewsResponse(Context context, String newsType, int page)
            throws IOException {
        String appCode = context.getString(R.string.news_app_code);

        String spec = Uri.parse("http://ali-news.showapi.com")
                .buildUpon()
                .appendPath("newsList")
                .appendQueryParameter("channelName", newsType)
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("needContent", "1")
                .appendQueryParameter("needAllList", "0")
                .appendQueryParameter("maxResult", "15")
                .build().toString();

        return getResponseFromHttpUrl(spec, appCode);
    }

    @NonNull
    private static String getResponseFromHttpUrl(String spec, String appCode) throws IOException {
        URL url = new URL(spec);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = null;
        try {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "APPCODE " + appCode);

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
