package com.xhbb.qinzl.newsest.viewmodel;

import android.view.View;

/**
 * Created by qinzl on 2017/5/27.
 */

public class NewsMaster {

    private String mTitle;
    private String mImageUrl1;
    private String mImageUrl2;
    private String mImageUrl3;

    private int mItemPosition;
    private OnNewsMasterListener mOnNewsMasterListener;

    public NewsMaster(String title, String imageUrl1, String imageUrl2, String imageUrl3,
                      int itemPosition, OnNewsMasterListener onNewsMasterListener) {
        mTitle = title;
        mImageUrl1 = imageUrl1;
        mImageUrl2 = imageUrl2;
        mImageUrl3 = imageUrl3;
        mItemPosition = itemPosition;
        mOnNewsMasterListener = onNewsMasterListener;
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
        mOnNewsMasterListener.onClickItem(mItemPosition, sharedElement);
    }

    public interface OnNewsMasterListener {

        void onClickItem(int itemPosition, View sharedElement);
    }
}
