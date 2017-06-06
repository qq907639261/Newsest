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

        String _NEWS_CODE = "news_code";
        String _TITLE = "title";
        String _PUBLISH_DATE = "publish_date";
        String _SOURCE_WEB = "source_web";
        String _NEWS_CONTENT = "news_content";
        String _IMAGE_URL_1 = "image_url_1";
        String _IMAGE_URL_2 = "image_url_2";
        String _IMAGE_URL_3 = "image_url_3";
        String _NEWS_TYPE = "news_type";
    }
}
