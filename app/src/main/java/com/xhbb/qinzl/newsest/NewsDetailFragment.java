package com.xhbb.qinzl.newsest;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.xhbb.qinzl.newsest.databinding.FragmentNewsDetailBinding;
import com.xhbb.qinzl.newsest.viewmodel.Comment;
import com.xhbb.qinzl.newsest.viewmodel.News;

public class NewsDetailFragment extends Fragment implements Comment.OnCommentListener {

    private static final String ARG_NEWS = "ARG_NEWS";

    private News mNews;
    private Comment mComment;
    private FragmentActivity mActivity;

    public static NewsDetailFragment newInstance(News news) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEWS, news);

        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNews = getArguments().getParcelable(ARG_NEWS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewsDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail, container, false);
        setHasOptionsMenu(true);

        mActivity = getActivity();

        binding.setNews(mNews);
        return binding.getRoot();
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Transition transition = TransitionInflater.from(mActivity).inflateTransition(R.transition.explode);
                    mActivity.getWindow().setExitTransition(transition);
                }
                SettingsActivity.start(mActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickCommitCommentButton() {

    }
}
