package com.xhbb.qinzl.newsest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;

import com.xhbb.qinzl.newsest.common.RecyclerViewCursorAdapter;
import com.xhbb.qinzl.newsest.data.Contract.CommentEntry;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.data.PreferencesUtils;
import com.xhbb.qinzl.newsest.databinding.FragmentNewsDetailBinding;
import com.xhbb.qinzl.newsest.server.JsonUtils;
import com.xhbb.qinzl.newsest.viewmodel.Comment;
import com.xhbb.qinzl.newsest.viewmodel.NewsDetail;

public class NewsDetailFragment extends Fragment implements
        NewsDetail.OnNewsDetailListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_NEWS_DETAIL_VALUES = "ARG_NEWS_DETAIL_VALUES";

    private ContentValues mNewsDetailValues;
    private Context mContext;
    private String mNewsCode;
    private String mCommentJson;
    private CommentAdapter mCommentAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private NewsDetail mNewsDetail;
    private EditText mCommentEdit;

    public static NewsDetailFragment newInstance(ContentValues newsDetailValues) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEWS_DETAIL_VALUES, newsDetailValues);

        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewsDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_news_detail, container, false);

        mContext = getContext();
        mNewsDetailValues = getArguments().getParcelable(ARG_NEWS_DETAIL_VALUES);
        mCommentJson = PreferencesUtils.getCommentJson(mContext);
        mNewsCode = mNewsDetailValues.getAsString(NewsEntry._NEWS_CODE);
        mCommentAdapter = new CommentAdapter(mContext, R.layout.item_comment);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mCommentEdit = binding.commentEdit;

        initNewsDetail();
        setCommentFromJsonIfExists();
        getLoaderManager().initLoader(0, null, this);

        binding.setNewsDetail(mNewsDetail);
        return binding.getRoot();
    }

    private void setCommentFromJsonIfExists() {
        if (mCommentJson == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ContentValues commentValues = JsonUtils.getCommentValues(mCommentJson);
                String newsCodeFromJson = commentValues.getAsString(CommentEntry._NEWS_CODE);

                if (!newsCodeFromJson.equals(mNewsCode)) {
                    return;
                }

                final FragmentActivity activity = getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String comment = commentValues.getAsString(CommentEntry._COMMENT_CONTENT);
                        mNewsDetail.setComment(comment);

                        mNewsDetail.setSoftInputShowed(true);
                        activity.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                });
            }
        }).start();
    }

    private void initNewsDetail() {
        String title = mNewsDetailValues.getAsString(NewsEntry._TITLE);
        String imageUrl = mNewsDetailValues.getAsString(NewsEntry._IMAGE_URL_1);
        String publishDate = mNewsDetailValues.getAsString(NewsEntry._PUBLISH_DATE);
        String sourceWeb = mNewsDetailValues.getAsString(NewsEntry._SOURCE_WEB);
        String newsContent = mNewsDetailValues.getAsString(NewsEntry._NEWS_CONTENT);

        mNewsDetail = new NewsDetail(title, imageUrl, publishDate, sourceWeb, newsContent,
                mCommentAdapter,
                mLinearLayoutManager,
                this);
    }

    @Override
    public void onStop() {
        super.onStop();

        String comment = mCommentEdit.getText().toString().trim();
        if (comment.length() > 0) {
            long currentDate = System.currentTimeMillis();
            String commentJson = mContext.getString(R.string.comment_cache_json_format,
                    currentDate, comment, mNewsCode);

            PreferencesUtils.saveCommentJson(mContext, commentJson);
        } else {
            PreferencesUtils.removeCommentJson(mContext);
        }
    }

    @Override
    public void onClickCommentButton(EditText commentEdit) {
        String comment = commentEdit.getText().toString().trim();

        ContentValues commentValues = new ContentValues();
        commentValues.put(CommentEntry._NEWS_CODE, mNewsCode);
        commentValues.put(CommentEntry._COMMENT_CONTENT, comment);
        commentValues.put(CommentEntry._COMMENT_DATE, System.currentTimeMillis());

        getActivity().getContentResolver().insert(CommentEntry.URI, commentValues);
        commentEdit.setText("");
        mNewsDetail.setSoftInputShowed(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = CommentEntry._NEWS_CODE + "=?";
        String[] selectionArgs = {mNewsCode};
        return new CursorLoader(mContext, CommentEntry.URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCommentAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCommentAdapter.swapCursor(null);
    }

    private class CommentAdapter extends RecyclerViewCursorAdapter {

        CommentAdapter(Context context, int defaultLayoutRes) {
            super(context, defaultLayoutRes);
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            ViewDataBinding binding = holder.getBinding();

            binding.setVariable(BR.comment, getComment(position));
            binding.executePendingBindings();
        }

        private Comment getComment(int position) {
            mCursor.moveToPosition(position);

            String content = mCursor.getString(mCursor.getColumnIndex(CommentEntry._COMMENT_CONTENT));
            long date = mCursor.getLong(mCursor.getColumnIndex(CommentEntry._COMMENT_DATE));

            return new Comment(mContext, content, date);
        }
    }
}
