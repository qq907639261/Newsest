package com.xhbb.qinzl.newsest.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.xhbb.qinzl.newsest.BR;

/**
 * Created by qinzl on 2017/6/9.
 */

public class NewsDetail extends BaseObservable {

    private String mTitle;
    private String mImageUrl;
    private String mPublishDate;
    private String mSourceWeb;
    private String mNewsContent;
    private boolean mSoftInputShowed;
    private CharSequence mComment;

    private RecyclerView.Adapter mCommentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnNewsDetailListener mOnNewsDetailListener;

    public NewsDetail(String title, String imageUrl, String publishDate, String sourceWeb, String newsContent,
                      RecyclerView.Adapter commentAdapter,
                      RecyclerView.LayoutManager layoutManager,
                      OnNewsDetailListener onNewsDetailListener) {
        mTitle = title;
        mImageUrl = imageUrl;
        mPublishDate = publishDate;
        mSourceWeb = sourceWeb;
        mNewsContent = newsContent;
        mCommentAdapter = commentAdapter;
        mLayoutManager = layoutManager;
        mOnNewsDetailListener = onNewsDetailListener;

        mComment = "";
    }

    @Bindable
    public CharSequence getComment() {
        return mComment;
    }

    public void setComment(CharSequence comment) {
        mComment = comment;
        notifyPropertyChanged(BR.comment);
    }

    @Bindable
    public boolean isSoftInputShowed() {
        return mSoftInputShowed;
    }

    public void setSoftInputShowed(boolean softInputShowed) {
        mSoftInputShowed = softInputShowed;
        notifyPropertyChanged(BR.softInputShowed);
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
        int textLength = charSequence.toString().trim().length();
        if (textLength < 5 && commentButton.isEnabled()) {
            commentButton.setEnabled(false);
        } else if (textLength >= 5 && !commentButton.isEnabled()) {
            commentButton.setEnabled(true);
        }
    }

    public void onTransferView(EditText commentEdit) {
        mOnNewsDetailListener.onTransferView(commentEdit);
    }

    public interface OnNewsDetailListener {

        void onClickCommentButton(EditText commentEdit);
        void onTransferView(EditText commentEdit);
    }
}
