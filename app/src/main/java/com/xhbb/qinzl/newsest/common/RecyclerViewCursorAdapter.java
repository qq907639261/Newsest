package com.xhbb.qinzl.newsest.common;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qinzl on 2017/5/30.
 */

public abstract class RecyclerViewCursorAdapter extends
        RecyclerView.Adapter<RecyclerViewCursorAdapter.BindingHolder> {

    protected Context mContext;
    protected Cursor mCursor;

    private int mDefaultLayoutRes;

    public RecyclerViewCursorAdapter(Context context, int defaultLayoutRes) {
        mContext = context;
        mDefaultLayoutRes = defaultLayoutRes;
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mDefaultLayoutRes, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    protected static class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mBinding;

        public BindingHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBinding() {
            return mBinding;
        }
    }
}
