package com.xhbb.qinzl.newsest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhbb.qinzl.newsest.async.UpdateDataTask;
import com.xhbb.qinzl.newsest.common.RecyclerViewCursorAdapter;
import com.xhbb.qinzl.newsest.data.Contract;
import com.xhbb.qinzl.newsest.data.PreferencesUtils;
import com.xhbb.qinzl.newsest.databinding.LayoutRecyclerViewBinding;
import com.xhbb.qinzl.newsest.server.NetworkUtils;
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
    private LinearLayoutManager mLayoutManager;
    private boolean mRecyclerViewScrollRefreshing;
    private boolean mNewsTotalPageOuted;
    private boolean mHasNewsData;
    private String mErrorText;

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
        mNewsAdapter = new NewsAdapter(mActivity, R.layout.item_news);
        mNewsType = getArguments().getString(ARG_NEWS_TYPE);
        mNewsPage = 1;
        mHasNewsData = PreferencesUtils.hasNewsData(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        LayoutRecyclerViewBinding binding = DataBindingUtil.bind(view);

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerViewModel = new RecyclerViewModel(mNewsAdapter, mLayoutManager,
                getOnRecyclerViewScrollListener(), this);

        getLoaderManager().initLoader(0, null, this);
        refreshNewsData(false);

        binding.setRecyclerViewModel(mRecyclerViewModel);

        return view;
    }

    @NonNull
    private RecyclerView.OnScrollListener getOnRecyclerViewScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mRecyclerViewScrollRefreshing || mNewsTotalPageOuted) {
                    return;
                }

                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                int itemCount = mLayoutManager.getItemCount();

                if (lastVisibleItemPosition >= itemCount - NetworkUtils.NEWS_COUNT_OF_EACH_PAGE) {
                    mRecyclerViewScrollRefreshing = true;
                    refreshNewsData(false);
                }
            }
        };
    }

    private void refreshNewsData(final boolean swipeRefreshing) {
        new AsyncTask<Void, Void, Integer>() {
            private static final int NETWORK_ERROR = -2;
            private static final int SERVER_ERROR = -1;
            private static final int NEWS_PAGE_OUT_RANGE = 0;
            private static final int REFRESH_SUCCESS = 1;

            @Override
            protected Integer doInBackground(Void... params) {
                if (!NetworkUtils.isNetworkConnectedOrConnecting(mActivity)) {
                    return NETWORK_ERROR;
                }

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

                mRecyclerViewScrollRefreshing = false;
                mRecyclerViewModel.setAutoRefreshing(false);
                if (swipeRefreshing) {
                    mRecyclerViewModel.getSwipeRefreshLayout().setRefreshing(false);
                }

                switch (integer) {
                    case NETWORK_ERROR:
                        handleError(getString(R.string.network_error_text));
                        break;
                    case SERVER_ERROR:
                        handleError(getString(R.string.server_error_text));
                        break;
                    case NEWS_PAGE_OUT_RANGE:
                        handleNewsTotalPageOuted();
                        break;
                    default:
                        if (swipeRefreshing) {
                            mNewsTotalPageOuted = false;
                            mNewsPage = 2;
                            mErrorText = null;
                            mNewsAdapter.notifyItemChanged(0);
                        } else {
                            mNewsPage++;
                        }
                }
            }

            private void handleNewsTotalPageOuted() {
                mNewsTotalPageOuted = true;
                mNewsAdapter.notifyItemChanged(mNewsAdapter.getItemCount() - 1);
            }

            private void handleError(String errorText) {
                if (mHasNewsData) {
                    handleNewsTotalPageOuted();
                    mErrorText = errorText;
                    mNewsAdapter.notifyItemChanged(0);
                } else {
                    mRecyclerViewModel.setErrorText(errorText);
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
        mRecyclerViewModel.setErrorText(null);
        mHasNewsData = true;
        PreferencesUtils.saveHasNewsData(mActivity, true);
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

        private static final int VIEW_TYPE_LAST_ITEM = 0;
        private static final int VIEW_TYPE_FIRST_ITEM = 1;
        private static final int VIEW_TYPE_OTHER_ITEM = 2;

        private News mNews;

        NewsAdapter(Context context, @LayoutRes int defaultResource) {
            super(context, defaultResource);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return VIEW_TYPE_LAST_ITEM;
            } else if (position == 0) {
                return VIEW_TYPE_FIRST_ITEM;
            } else {
                return VIEW_TYPE_OTHER_ITEM;
            }
        }

        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_FIRST_ITEM:
                    return getBindingHolder(parent, R.layout.item_news_first);
                case VIEW_TYPE_LAST_ITEM:
                    return getBindingHolder(parent, R.layout.item_news_last);
                default:
                    return super.onCreateViewHolder(parent, viewType);
            }
        }

        @NonNull
        private BindingHolder getBindingHolder(ViewGroup parent, @LayoutRes int itemResource) {
            View view = LayoutInflater.from(mContext).inflate(itemResource, parent, false);
            return new BindingHolder(view);
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            mCursor.moveToPosition(position);
            mNews = new News(mContext, mCursor, this);

            ViewDataBinding binding = holder.getBinding();

            binding.setVariable(BR.news, mNews);

            if (position == getItemCount() - 1) {
                binding.setVariable(BR.newsTotalPageOuted, mNewsTotalPageOuted);
            } else if (position == 0) {
                binding.setVariable(BR.errorText, mErrorText);
            }

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            NewsDetailActivity.start(mContext, mNews);
        }
    }
}
