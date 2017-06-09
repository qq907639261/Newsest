package com.xhbb.qinzl.newsest.data;

import android.content.Context;
import android.preference.PreferenceManager;

import com.xhbb.qinzl.newsest.R;

/**
 * Created by qinzl on 2017/6/9.
 */

public class PreferencesUtil {

    public static void saveCommentCacheJson(Context context, String commentCacheJson) {
        String key = context.getString(R.string.key_comment_cache_json);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, commentCacheJson)
                .apply();
    }

    public static String getCommentCacheJson(Context context) {
        String key = context.getString(R.string.key_comment_cache_json);
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, null);
    }
}
