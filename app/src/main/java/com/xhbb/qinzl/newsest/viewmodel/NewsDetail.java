package com.xhbb.qinzl.newsest.viewmodel;

import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by qinzl on 2017/6/9.
 */

public class NewsDetail {

    private OnNewsDetailListener mOnNewsDetailListener;

    private String mTitle;
    private String mImageUrl;
    private String mPublishDate;
    private String mSourceWeb;
    private String mNewsContent;

    public NewsDetail(String title, String imageUrl, String publishDate, String sourceWeb,
                      String newsContent, OnNewsDetailListener onNewsDetailListener) {
        mTitle = title;
        mImageUrl = imageUrl;
        mPublishDate = publishDate;
        mSourceWeb = sourceWeb;
        mNewsContent = newsContent;
        mOnNewsDetailListener = onNewsDetailListener;
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public String getSourceWeb() {
        return mSourceWeb;
    }

    public String getNewsContent() {
        return mNewsContent;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void onClickButton(EditText commentEdit) {
        mOnNewsDetailListener.onClickCommentButton(commentEdit);
    }

    public void onTextChanged(CharSequence charSequence, Button commentButton) {
        if (charSequence.toString().trim().length() < 5) {
            commentButton.setEnabled(false);
        } else {
            commentButton.setEnabled(true);
        }
    }

    public interface OnNewsDetailListener {

        void onClickCommentButton(EditText commentEdit);
    }
}
