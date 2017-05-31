package com.xhbb.qinzl.newsest.data;

import android.content.Context;
import android.preference.PreferenceManager;

import com.xhbb.qinzl.newsest.R;

/**
 * Created by qinzl on 2017/5/27.
 */

public class PreferencesUtils {

    public static void saveHasNewsData(Context context, boolean hasNewsData) {
        String key = context.getString(R.string.key_has_news_data);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, hasNewsData)
                .apply();
    }

    public static boolean hasNewsData(Context context) {
        String key = context.getString(R.string.key_has_news_data);
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(key, false);
    }
}
