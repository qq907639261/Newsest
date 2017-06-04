package com.xhbb.qinzl.newsest.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MainProvider extends ContentProvider {

    private DbHelper mDbHelper;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new DbHelper(mContext);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String tableName = uri.getLastPathSegment();

        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, sortOrder, null, null);
        cursor.setNotificationUri(getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tableName = uri.getLastPathSegment();

        db.beginTransaction();
        try {
            for (ContentValues values : valuesArray) {
                db.insert(tableName, null, values);
            }
            db.setTransactionSuccessful();

            getContentResolver().notifyChange(uri, null);
            return valuesArray.length;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        String tableName = uri.getLastPathSegment();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long insertedId = db.insert(tableName, null, values);

        getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(insertedId));
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tableName = uri.getLastPathSegment();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int affectedRows = db.update(tableName, values, selection, selectionArgs);

        getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String tableName = uri.getLastPathSegment();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int affectedRows = db.delete(tableName, selection, selectionArgs);

        getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    private ContentResolver getContentResolver() {
        return mContext.getContentResolver();
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
