package com.xhbb.qinzl.newsest.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qinzl on 2017/5/27.
 */

public class News implements Parcelable {

    private String mNewsCode;
    private String mTitle;
    private String mPublishDate;
    private String mSourceWeb;
    private String mDescription;
    private String mContent;
    private String mImageUrl;

    protected News(Parcel in) {
        mNewsCode = in.readString();
        mTitle = in.readString();
        mPublishDate = in.readString();
        mSourceWeb = in.readString();
        mDescription = in.readString();
        mContent = in.readString();
        mImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNewsCode);
        dest.writeString(mTitle);
        dest.writeString(mPublishDate);
        dest.writeString(mSourceWeb);
        dest.writeString(mDescription);
        dest.writeString(mContent);
        dest.writeString(mImageUrl);
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
}
