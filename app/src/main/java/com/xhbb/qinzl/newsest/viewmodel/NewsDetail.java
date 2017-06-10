package com.xhbb.qinzl.newsest.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by qinzl on 2017/6/9.
 */

public class NewsDetail {

    private OnNewsDetailListener mOnNewsDetailListener;
    private RecyclerView.Adapter mCommentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String mTitle;
    private String mImageUrl;
    private String mPublishDate;
    private String mSourceWeb;
    private String mNewsContent;

    public NewsDetail(String title, String imageUrl, String publishDate, String sourceWeb,
                      String newsContent, OnNewsDetailListener onNewsDetailListener,
                      RecyclerView.Adapter commentAdapter, RecyclerView.LayoutManager layoutManager) {
        mTitle = title;
        mImageUrl = imageUrl;
        mPublishDate = publishDate;
        mSourceWeb = sourceWeb;
        mNewsContent = newsContent;
        mOnNewsDetailListener = onNewsDetailListener;
        mCommentAdapter = commentAdapter;
        mLayoutManager = layoutManager;
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

    public RecyclerView.Adapter getCommentAdapter() {
        return mCommentAdapter;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
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
