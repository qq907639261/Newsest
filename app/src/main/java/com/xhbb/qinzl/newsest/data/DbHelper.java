package com.xhbb.qinzl.newsest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xhbb.qinzl.newsest.data.Contract.CommentEntry;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;

/**
 * Created by qinzl on 2017/5/29.
 */

class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "newsest.db";
    private static final int DB_VERSION = 2;

    private static final String CREATE_TABLE_NEWS =
            "CREATE TABLE " + NewsEntry.TABLE_NAME + " ( " +
                    NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NewsEntry._NEWS_CONTENT + " TEXT, " +
                    NewsEntry._IMAGE_URL_1 + " TEXT, " +
                    NewsEntry._IMAGE_URL_2 + " TEXT, " +
                    NewsEntry._IMAGE_URL_3 + " TEXT, " +
                    NewsEntry._PUBLISH_DATE + " TEXT, " +
                    NewsEntry._SOURCE_WEB + " TEXT, " +
                    NewsEntry._TITLE + " TEXT, " +
                    NewsEntry._NEWS_CODE + " TEXT, " +
                    NewsEntry._NEWS_TYPE + " TEXT, " +
                    NewsEntry._TOTAL_PAGE_BY_TYPE + " INTEGER " +
                    ")";

    private static final String CREATE_TABLE_COMMENT =
            "CREATE TABLE " + CommentEntry.TABLE_NAME + " ( " +
                    CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CommentEntry._COMMENT_DATE + " LONG, " +
                    CommentEntry._COMMENT_CONTENT + " TEXT, " +
                    CommentEntry._NEWS_CODE + " TEXT " +
                    ")";

    DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(CREATE_TABLE_NEWS);
            db.execSQL(CREATE_TABLE_COMMENT);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tableName = NewsEntry.TABLE_NAME;
        String tempTableName = tableName + "_temp";

        db.beginTransaction();
        try {
            db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tempTableName);
            db.execSQL(CREATE_TABLE_NEWS);
            db.execSQL("INSERT INTO " + tableName +
                    " SELECT *,'国际' FROM " + tempTableName);
            db.execSQL("DROP TABLE " + tempTableName);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
