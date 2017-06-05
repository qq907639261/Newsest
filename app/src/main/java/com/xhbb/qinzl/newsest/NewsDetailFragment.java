package com.xhbb.qinzl.newsest;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhbb.qinzl.newsest.databinding.FragmentNewsDetailBinding;
import com.xhbb.qinzl.newsest.viewmodel.News;

public class NewsDetailFragment extends Fragment {

    private static final String ARG_NEWS = "ARG_NEWS";

    private News mNews;
    private Activity mActivity;

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
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewsDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_news_detail, container, false);

        mNews.setContext(mActivity);
        mNews.setupLargeNewsImageWidth();
        binding.setNews(mNews);

        return binding.getRoot();
    }
}
