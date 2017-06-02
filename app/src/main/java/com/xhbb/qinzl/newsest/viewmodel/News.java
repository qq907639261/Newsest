package com.xhbb.qinzl.newsest.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import com.xhbb.qinzl.newsest.common.GlideApp;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;

/**
 * Created by qinzl on 2017/5/27.
 */

public class News implements Parcelable {

    private Context mContext;
    private View.OnClickListener mOnClickItemListener;

    private String mNewsCode;
    private String mTitle;
    private String mPublishDate;
    private String mSourceWeb;
    private String mDescription;
    private String mNewsContent;
    private String mImageUrl;

    public News(Context context, Cursor cursor) {
        mContext = context;
        setNews(cursor);
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

    public void setContext(Context context) {
        mContext = context;
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

    public void setOnClickItemListener(View.OnClickListener onClickItemListener) {
        mOnClickItemListener = onClickItemListener;
    }

    public String getTitle() {
        return mTitle;
    }

    @BindingAdapter({"android:setNewsImage"})
    public static void setNewsImageFromServer(ImageView imageView, News news) {
        GlideApp.with(news.mContext)
                .load(news.mImageUrl)
                .into(imageView);
    }

    public void onItemClick(View view) {
        if (mOnClickItemListener != null) {
            mOnClickItemListener.onClick(view);
        }
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
