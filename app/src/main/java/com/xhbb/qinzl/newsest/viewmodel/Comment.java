package com.xhbb.qinzl.newsest.viewmodel;

/**
 * Created by qinzl on 2017/6/7.
 */

public class Comment {

    private String mUserComment1;
    private String mUserComment2;
    private String mUserComment3;
    private OnTopCommentsListener mOnTopCommentsListener;

    public Comment(String userComment1, String userComment2, String userComment3,
                   OnTopCommentsListener onTopCommentsListener) {
        mUserComment1 = userComment1;
        mUserComment2 = userComment2;
        mUserComment3 = userComment3;
        mOnTopCommentsListener = onTopCommentsListener;
    }

    public String getUserComment1() {
        return mUserComment1;
    }

    public String getUserComment2() {
        return mUserComment2;
    }

    public String getUserComment3() {
        return mUserComment3;
    }

    public void onClickMoreCommentsText() {
        mOnTopCommentsListener.onClickMoreCommentsText();
    }

    public interface OnTopCommentsListener {

        void onClickMoreCommentsText();
    }
}
