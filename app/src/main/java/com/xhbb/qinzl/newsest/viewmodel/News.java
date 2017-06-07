package com.xhbb.qinzl.newsest.viewmodel;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

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
    private String mNewsContent;
    private String mImageUrl1;
    private String mImageUrl2;
    private String mImageUrl3;

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
        mNewsContent = cursor.getString(cursor.getColumnIndex(NewsEntry._NEWS_CONTENT));
        mImageUrl1 = cursor.getString(cursor.getColumnIndex(NewsEntry._IMAGE_URL_1));
        mImageUrl2 = cursor.getString(cursor.getColumnIndex(NewsEntry._IMAGE_URL_2));
        mImageUrl3 = cursor.getString(cursor.getColumnIndex(NewsEntry._IMAGE_URL_3));
    }

    private News(Parcel in) {
        mNewsCode = in.readString();
        mTitle = in.readString();
        mPublishDate = in.readString();
        mSourceWeb = in.readString();
        mNewsContent = in.readString();
        mImageUrl1 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNewsCode);
        dest.writeString(mTitle);
        dest.writeString(mPublishDate);
        dest.writeString(mSourceWeb);
        dest.writeString(mNewsContent);
        dest.writeString(mImageUrl1);
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public String getSourceWeb() {
        return mSourceWeb;
    }

    public String getNewsCode() {
        return mNewsCode;
    }

    public String getNewsContent() {
        return mNewsContent;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl1() {
        return mImageUrl1;
    }

    public String getImageUrl2() {
        return mImageUrl2;
    }

    public String getImageUrl3() {
        return mImageUrl3;
    }

    public void onClickItem(View sharedElement) {
        mOnNewsListener.onClickItem(this, mItemPosition, sharedElement);
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
