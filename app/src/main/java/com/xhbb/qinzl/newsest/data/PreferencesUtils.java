package com.xhbb.qinzl.newsest.data;

import android.content.Context;
import android.preference.PreferenceManager;

import com.xhbb.qinzl.newsest.R;

/**
 * Created by qinzl on 2017/6/9.
 */

public class PreferencesUtils {

    public static void saveCommentJson(Context context, String commentJson) {
        String key = context.getString(R.string.key_comment_json);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, commentJson)
                .apply();
    }

    public static String getCommentJson(Context context) {
        String key = context.getString(R.string.key_comment_json);
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, null);
    }

    public static void removeCommentJson(Context context) {
        String key = context.getString(R.string.key_comment_json);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(key)
                .apply();
    }
}
