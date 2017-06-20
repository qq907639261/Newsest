package com.xhbb.qinzl.newsest.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.xhbb.qinzl.newsest.R;
import com.xhbb.qinzl.newsest.common.MainSingleton;
import com.xhbb.qinzl.newsest.data.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinzl on 2017/5/27.
 */

public class NetworkUtils {

    public static final int NEWS_COUNT_OF_EACH_PAGE = 15;

    public static void addNewsRequestToRequestQueue(final Context context, String newsType, int page,
                                                    Response.Listener<String> listener,
                                                    Response.ErrorListener errorListener) {
        String url = Uri.parse("http://ali-news.showapi.com")
                .buildUpon()
                .appendPath("newsList")
                .appendQueryParameter("maxResult", String.valueOf(NEWS_COUNT_OF_EACH_PAGE))
                .appendQueryParameter("channelName", newsType)
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("needContent", "1")
                .appendQueryParameter("needHtml", "1")
                .appendQueryParameter("needAllList", "0")
                .build()
                .toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String appCode = context.getString(R.string.news_app_code);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "APPCODE " + appCode);

                return headers;
            }
        };

        MainSingleton.getInstance(context).addToRequestQueue(request);
    }

    public static File getTheLatestApkFile(Context context) throws IOException {
        if (!isNetworkAvailable(context)) {
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
        return isNetworkAvailable(context);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }
}
