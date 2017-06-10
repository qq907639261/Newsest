package com.xhbb.qinzl.newsest.viewmodel;

import android.content.Context;

import com.xhbb.qinzl.newsest.R;

import java.util.Calendar;

/**
 * Created by qinzl on 2017/6/10.
 */

public class Comment {

    private String mContent;
    private String mDate;
    private Context mContext;

    public Comment(Context context, String content, long date) {
        mContext = context;
        mContent = content;
        setDate(date);
    }

    private void setDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDate = mContext.getString(R.string.format_date, year, month, day);
    }

    public String getContent() {
        return mContent;
    }

    public String getDate() {
        return mDate;
    }
}
