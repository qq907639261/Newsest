package com.xhbb.qinzl.newsest.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;

import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;

/**
 * Created by qinzl on 2017/5/27.
 */

public class News implements Parcelable {

    private Context mContext;
    private OnNewsListener mOnNewsListener;
    private int mItemPosition;
    private int mLargeNewsImageWidth;

    private String mDescription;

    private String mNewsCode;
    private String mTitle;
    private String mPublishDate;
    private String mSourceWeb;
    private String mNewsContent;
    private String mImageUrl;

    public News(Context context, Cursor cursor, OnNewsListener onNewsListener, int itemPosition) {
        setNews(cursor);
        mContext = context;
        mOnNewsListener = onNewsListener;
        mItemPosition = itemPosition;
    }

    private void setNews(Cursor cursor) {
        mNewsCode = cursor.getString(cursor.getColumnIndex(NewsEntry._NEWS_CODE));
        mTitle = cursor.getString(cursor.getColumnIndex(NewsEntry._TITLE));
        mPublishDate = cursor.getString(cursor.getColumnIndex(NewsEntry._PUBLISH_DATE));
        mSourceWeb = cursor.getString(cursor.getColumnIndex(NewsEntry._SOURCE_WEB));
        mDescription = cursor.getString(cursor.getColumnIndex(NewsEntry._DESCRIPTION));
        mNewsContent = cursor.getString(cursor.getColumnIndex(NewsEntry._NEWS_CONTENT));
        mImageUrl = cursor.getString(cursor.getColumnIndex(NewsEntry._IMAGE_URL));
    }

    private News(Parcel in) {
        mNewsCode = in.readString();
        mTitle = in.readString();
        mPublishDate = in.readString();
        mSourceWeb = in.readString();
        mNewsContent = in.readString();
        mImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNewsCode);
        dest.writeString(mTitle);
        dest.writeString(mPublishDate);
        dest.writeString(mSourceWeb);
        dest.writeString(mNewsContent);
        dest.writeString(mImageUrl);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setupLargeNewsImageWidth() {
        Activity activity = (Activity) mContext;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        mLargeNewsImageWidth = widthPixels < heightPixels ? widthPixels : heightPixels;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void onClickItem(View sharedElement) {
        mOnNewsListener.onClickItem(this, mItemPosition, sharedElement);
    }

    public int getLargeNewsImageWidth() {
        return mLargeNewsImageWidth;
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public interface OnNewsListener {

        void onClickItem(News news, int itemPosition, View sharedElement);
    }
}
