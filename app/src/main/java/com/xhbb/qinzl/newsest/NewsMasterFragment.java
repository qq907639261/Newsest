package com.xhbb.qinzl.newsest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhbb.qinzl.newsest.async.UpdateDataTask;
import com.xhbb.qinzl.newsest.common.RecyclerViewCursorAdapter;
import com.xhbb.qinzl.newsest.data.Contract;
import com.xhbb.qinzl.newsest.databinding.LayoutRecyclerViewBinding;
import com.xhbb.qinzl.newsest.viewmodel.News;
import com.xhbb.qinzl.newsest.viewmodel.RecyclerViewModel;

import org.json.JSONException;

import java.io.IOException;

public class NewsMasterFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_NEWS_TYPE = "ARG_NEWS_TYPE";

    private Activity mActivity;
    private NewsAdapter mNewsAdapter;
    private String mNewsType;
    private RecyclerViewModel mRecyclerViewModel;
    private int mNewsPage;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static NewsMasterFragment newInstance(String newsType) {
        Bundle args = new Bundle();
        args.putString(ARG_NEWS_TYPE, newsType);

        NewsMasterFragment fragment = new NewsMasterFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mNewsAdapter = new NewsAdapter(mActivity, R.layout.item_news_master);
        mNewsType = getArguments().getString(ARG_NEWS_TYPE);
        mNewsPage = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        LayoutRecyclerViewBinding binding = DataBindingUtil.bind(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerViewModel = new RecyclerViewModel(mNewsAdapter, layoutManager, this);

        getLoaderManager().initLoader(0, null, this);
        refreshNewsData(false);
        binding.setRecyclerViewModel(mRecyclerViewModel);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = mRecyclerViewModel.getSwipeRefreshLayout();
    }

    private void refreshNewsData(final boolean swipeRefreshing) {
        new AsyncTask<Void, Void, Integer>() {
            private static final int NETWORK_ERROR = -2;
            private static final int SERVER_ERROR = -1;
            private static final int NEWS_PAGE_OUT_RANGE = 0;
            private static final int REFRESH_SUCCESS = 1;

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    int newsPage = swipeRefreshing ? 1 : mNewsPage;
                    boolean newsPageInRange = UpdateDataTask
                            .updateNewsDataIfThePageInRange(mActivity, mNewsType, newsPage);

                    return newsPageInRange ? REFRESH_SUCCESS : NEWS_PAGE_OUT_RANGE;
                } catch (IOException e) {
                    e.printStackTrace();
                    return NETWORK_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return SERVER_ERROR;
                }
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);

                mRecyclerViewModel.setAutoRefreshing(false);
                if (swipeRefreshing) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                switch (integer) {
                    case NETWORK_ERROR:
                        mRecyclerViewModel.setAbnormalText(mActivity.getString(R.string.network_error_text));
                        break;
                    case SERVER_ERROR:
                        mRecyclerViewModel.setAbnormalText(mActivity.getString(R.string.server_error_text));
                        break;
                    case REFRESH_SUCCESS:
                        mNewsPage = swipeRefreshing ? 2 : mNewsPage + 1;
                        break;
                    default:
                }
            }
        }.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = Contract.NewsEntry._NEWS_TYPE + "=?";
        String[] selectionArgs = {mNewsType};
        return new CursorLoader(mActivity, Contract.NewsEntry.URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mNewsAdapter.swapCursor(cursor);

        if (cursor.getCount() == 0) {
            return;
        }

        mRecyclerViewModel.setAutoRefreshing(false);
        mRecyclerViewModel.setAbnormalText("");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        refreshNewsData(true);
    }

    private class NewsAdapter extends RecyclerViewCursorAdapter
            implements View.OnClickListener {

        private News mNews;

        NewsAdapter(Context context, @LayoutRes int defaultResource) {
            super(context, defaultResource);
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            mCursor.moveToPosition(position);
            mNews = new News(mContext, mCursor, this);

            ViewDataBinding binding = holder.getBinding();
            binding.setVariable(BR.news, mNews);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            NewsDetailActivity.start(mContext, mNews);
        }
    }
}
