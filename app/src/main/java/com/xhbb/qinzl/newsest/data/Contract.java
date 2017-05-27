package com.xhbb.qinzl.newsest.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by qinzl on 2017/5/27.
 */

public interface Contract {

    interface NewsEntry extends BaseColumns {

        String TABLE_NAME = "news";
        Uri URI = Uri.parse("content://com.xhbb.qinzl.newsest/" + TABLE_NAME);

        String _ALL_PAGES = "all_pages";
        String _TITLE = "title";
        String _PUBLISH_DATE = "publish_date";
        String _SOURCE_WEB = "source_web";
        String _DESCRIPTION = "description";
        String _CONTENT = "content";
        String _IMAGE_URL = "image_url";
    }
}
