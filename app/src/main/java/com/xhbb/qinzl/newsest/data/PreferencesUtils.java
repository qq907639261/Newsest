package com.xhbb.qinzl.newsest.data;

import android.content.Context;
import android.preference.PreferenceManager;

import com.xhbb.qinzl.newsest.R;

/**
 * Created by qinzl on 2017/5/27.
 */

public class PreferencesUtils {

    public static void saveNewsTotalPages(Context context, int totalPages) {
        String key = context.getString(R.string.key_news_total_pages);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(key, totalPages)
                .apply();
    }

    public static int getNewsTotalPages(Context context) {
        String key = context.getString(R.string.key_news_total_pages);
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(key, 1);
    }
}
