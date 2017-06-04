package com.xhbb.qinzl.newsest.viewmodel;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;

/**
 * Created by qinzl on 2017/5/27.
 */

public class News implements Parcelable {

    private OnNewsListener mOnNewsListener;
    private int mItemPosition;

    private String mNewsCode;
    private String mTitle;
    private String mPublishDate;
    private String mSourceWeb;
    private String mDescription;
    private String mNewsContent;
    private String mImageUrl;

    public News(Cursor cursor, OnNewsListener onNewsListener, int itemPosition) {
        setNews(cursor);
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
        mDescription = in.readString();
        mNewsContent = in.readString();
        mImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNewsCode);
        dest.writeString(mTitle);
        dest.writeString(mPublishDate);
        dest.writeString(mSourceWeb);
        dest.writeString(mDescription);
        dest.writeString(mNewsContent);
        dest.writeString(mImageUrl);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void onClickItem() {
        mOnNewsListener.onClickItem(this, mItemPosition);
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

        void onClickItem(News news, int itemPosition);
    }
}
