package com.xhbb.qinzl.newsest.common;

import android.content.ContentValues;
import android.database.Cursor;

import com.xhbb.qinzl.newsest.data.Contract;

/**
 * Created by qinzl on 2017/6/24.
 */

public class ValuesUtils {

    public static ContentValues getNewsValues(Cursor cursor) {
        String newsCode = cursor.getString(cursor.getColumnIndex(Contract.NewsEntry._NEWS_CODE));
        String publishDate = cursor.getString(cursor.getColumnIndex(Contract.NewsEntry._PUBLISH_DATE));
        String sourceWeb = cursor.getString(cursor.getColumnIndex(Contract.NewsEntry._SOURCE_WEB));
        String newsContent = cursor.getString(cursor.getColumnIndex(Contract.NewsEntry._NEWS_CONTENT));
        String title = cursor.getString(cursor.getColumnIndex(Contract.NewsEntry._TITLE));
        String imageUrl1 = cursor.getString(cursor.getColumnIndex(Contract.NewsEntry._IMAGE_URL_1));
        String imageUrl2 = cursor.getString(cursor.getColumnIndex(Contract.NewsEntry._IMAGE_URL_2));
        String imageUrl3 = cursor.getString(cursor.getColumnIndex(Contract.NewsEntry._IMAGE_URL_3));

        ContentValues newsValues = new ContentValues();

        newsValues.put(Contract.NewsEntry._NEWS_CODE, newsCode);
        newsValues.put(Contract.NewsEntry._TITLE, title);
        newsValues.put(Contract.NewsEntry._PUBLISH_DATE, publishDate);
        newsValues.put(Contract.NewsEntry._SOURCE_WEB, sourceWeb);
        newsValues.put(Contract.NewsEntry._NEWS_CONTENT, newsContent);
        newsValues.put(Contract.NewsEntry._IMAGE_URL_1, imageUrl1);
        newsValues.put(Contract.NewsEntry._IMAGE_URL_2, imageUrl2);
        newsValues.put(Contract.NewsEntry._IMAGE_URL_3, imageUrl3);

        return newsValues;
    }
}
