package com.xhbb.qinzl.newsest;

import android.content.ContentValues;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.xhbb.qinzl.newsest.data.Contract.CommentEntry;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.data.PreferencesUtil;
import com.xhbb.qinzl.newsest.databinding.FragmentNewsDetailBinding;
import com.xhbb.qinzl.newsest.server.JsonUtil;
import com.xhbb.qinzl.newsest.viewmodel.NewsDetail;

public class NewsDetailFragment extends Fragment implements
        NewsDetail.OnNewsDetailListener {

    private static final String ARG_NEWS_DETAIL_VALUES = "ARG_NEWS_DETAIL_VALUES";

    private ContentValues mNewsDetailValues;
    private FragmentActivity mActivity;
    private String mNewsCode;
    private EditText mCommentEdit;
    private String mCommentCacheJson;

    public static NewsDetailFragment newInstance(ContentValues newsDetailValues) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEWS_DETAIL_VALUES, newsDetailValues);

        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsDetailValues = getArguments().getParcelable(ARG_NEWS_DETAIL_VALUES);
        assert mNewsDetailValues != null;
        mNewsCode = mNewsDetailValues.getAsString(NewsEntry._NEWS_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewsDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_news_detail, container, false);
        setHasOptionsMenu(true);

        mCommentEdit = binding.commentEdit;
        mActivity = getActivity();
        mCommentCacheJson = PreferencesUtil.getCommentCacheJson(mActivity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(mActivity).inflateTransition(R.transition.explode);
            mActivity.getWindow().setExitTransition(transition);
        }

        getCommentIntoEditIfExists();

        binding.setNewsDetail(getNewsDetail());
        return binding.getRoot();
    }

    private void getCommentIntoEditIfExists() {
        if (mCommentCacheJson == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues commentValues = JsonUtil.getCommentValues(mCommentCacheJson);
                String newsCode = commentValues.getAsString(CommentEntry._NEWS_CODE);
                if (newsCode.equals(mNewsCode)) {
                    final String comment = commentValues.getAsString(CommentEntry._COMMENT_CONTENT);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCommentEdit.setText(comment);
                        }
                    });
                }
            }
        }).start();
    }

    @NonNull
    private NewsDetail getNewsDetail() {
        String title = mNewsDetailValues.getAsString(NewsEntry._TITLE);
        String imageUrl = mNewsDetailValues.getAsString(NewsEntry._IMAGE_URL_1);
        String publishDate = mNewsDetailValues.getAsString(NewsEntry._PUBLISH_DATE);
        String sourceWeb = mNewsDetailValues.getAsString(NewsEntry._SOURCE_WEB);
        String newsContent = mNewsDetailValues.getAsString(NewsEntry._NEWS_CONTENT);

        return new NewsDetail(title, imageUrl, publishDate, sourceWeb, newsContent, this);
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
                Toast.makeText(mActivity, "", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_settings:
                SettingsActivity.start(mActivity);
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
            String commentCacheJson = getString(R.string.comment_cache_json_format, currentDate, comment, mNewsCode);
            PreferencesUtil.saveCommentCacheJson(mActivity, commentCacheJson);
        }
    }

    @Override
    public void onClickCommentButton(EditText commentEdit) {
        String comment = commentEdit.getText().toString().trim();
    }
}
