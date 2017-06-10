package com.xhbb.qinzl.newsest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.xhbb.qinzl.newsest.common.RecyclerViewCursorAdapter;
import com.xhbb.qinzl.newsest.data.Contract.CommentEntry;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.data.PreferencesUtils;
import com.xhbb.qinzl.newsest.databinding.FragmentNewsDetailBinding;
import com.xhbb.qinzl.newsest.server.JsonUtils;
import com.xhbb.qinzl.newsest.viewmodel.Comment;
import com.xhbb.qinzl.newsest.viewmodel.NewsDetail;

public class NewsDetailFragment extends Fragment implements
        NewsDetail.OnNewsDetailListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_NEWS_DETAIL_VALUES = "ARG_NEWS_DETAIL_VALUES";

    private ContentValues mNewsDetailValues;
    private Context mContext;
    private String mNewsCode;
    private EditText mCommentEdit;
    private String mCommentJson;
    private CommentAdapter mCommentAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private NewsDetail mNewsDetail;

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
        setHasOptionsMenu(true);

        mCommentEdit = binding.commentEdit;
        mContext = getContext();
        mNewsDetailValues = getArguments().getParcelable(ARG_NEWS_DETAIL_VALUES);
        mCommentJson = PreferencesUtils.getCommentJson(mContext);
        mNewsCode = mNewsDetailValues.getAsString(NewsEntry._NEWS_CODE);
        mCommentAdapter = new CommentAdapter(mContext, R.layout.item_comment);
        mLinearLayoutManager = new LinearLayoutManager(mContext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(mContext).inflateTransition(R.transition.explode);
            getActivity().getWindow().setExitTransition(transition);
        }

        initNewsDetail();
        getCommentIntoEditIfExists();
        getLoaderManager().initLoader(0, null, this);

        binding.setNewsDetail(mNewsDetail);
        return binding.getRoot();
    }

    private void getCommentIntoEditIfExists() {
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String comment = commentValues.getAsString(CommentEntry._COMMENT_CONTENT);
                        mCommentEdit.append(comment);
                        mNewsDetail.setSoftInputShowed(true);
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

        mNewsDetail = new NewsDetail(title, imageUrl, publishDate, sourceWeb, newsContent, this,
                mCommentAdapter, mLinearLayoutManager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_settings:
                SettingsActivity.start(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        String comment = mCommentEdit.getText().toString().trim();
        if (comment.length() > 0) {
            long currentDate = System.currentTimeMillis();
            String commentJson = getString(R.string.comment_cache_json_format, currentDate, comment, mNewsCode);
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

        CommentAdapter(Context context, @LayoutRes int defaultResource) {
            super(context, defaultResource);
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
