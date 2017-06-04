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
import android.support.annotation.StringRes;
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
import com.xhbb.qinzl.newsest.common.MainEnum.RefreshState;
import com.xhbb.qinzl.newsest.common.RecyclerViewCursorAdapter;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.databinding.LayoutNormalRecyclerViewBinding;
import com.xhbb.qinzl.newsest.server.NetworkUtils;
import com.xhbb.qinzl.newsest.viewmodel.News;
import com.xhbb.qinzl.newsest.viewmodel.NormalRecyclerView;

import org.json.JSONException;

import java.io.IOException;

public class NewsMasterFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_NEWS_TYPE = "ARG_NEWS_TYPE";

    private static final String SAVE_ITEM_POSITION = "SAVE_ITEM_POSITION";
    private static final String SAVE_NEWS_PAGE = "SAVE_NEWS_PAGE";
    private static final String SAVE_NEWS_TOTAL_PAGE_OUTED = "SAVE_NEWS_TOTAL_PAGE_OUTED";

    private OnNewsMasterFragmentListener mOnNewsMasterFragmentListener;
    private boolean mHasNewsData;

    private int mRefreshState;
    private Activity mActivity;
    private NewsAdapter mNewsAdapter;
    private String mNewsType;
    private LoaderManager mLoaderManager;
    private LinearLayoutManager mLinearLayoutManager;
    private NormalRecyclerView mNormalRecyclerView;

    private int mItemPosition;
    private int mNewsPage;
    private boolean mNewsPageEqualsTotalPage;

    private boolean mViewRecreating;

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

        if (savedInstanceState != null) {
            mItemPosition = savedInstanceState.getInt(SAVE_ITEM_POSITION);
            mNewsPage = savedInstanceState.getInt(SAVE_NEWS_PAGE);
            mNewsPageEqualsTotalPage = savedInstanceState.getBoolean(SAVE_NEWS_TOTAL_PAGE_OUTED);
        }

        mRefreshState = RefreshState.NO_REFRESHING;
        mActivity = getActivity();
        mLoaderManager = getLoaderManager();
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mNewsType = getArguments().getString(ARG_NEWS_TYPE);
        mNewsAdapter = new NewsAdapter(mActivity, R.layout.item_news);
        mNormalRecyclerView = new NormalRecyclerView(mNewsAdapter, mLinearLayoutManager,
                getOnRecyclerViewScrollListener(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LayoutNormalRecyclerViewBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.layout_normal_recycler_view, container, false);

        if (savedInstanceState == null) {
            refreshNewsData(RefreshState.AUTO_REFRESHING);
        } else {
            mViewRecreating = true;
        }

        mLoaderManager.initLoader(0, null, this);
        binding.setNormalRecyclerView(mNormalRecyclerView);

        return binding.getRoot();
    }

    @Override
    public void onRefresh() {
        refreshNewsData(RefreshState.SWIPE_REFRESHING);
    }

    public void refreshNewsData(int refreshState) {
        mRefreshState = refreshState;

        new AsyncTask<Void, Void, Integer>() {

            private static final int NETWORK_ERROR = -1;
            private static final int SERVER_ERROR = 0;
            private static final int REFRESH_SUCCESS = 1;

            @Override
            protected Integer doInBackground(Void... params) {
                if (!NetworkUtils.isNetworkConnectedOrConnecting(mActivity)) {
                    return NETWORK_ERROR;
                }

                try {
                    boolean scrollRefreshing = mRefreshState == RefreshState.SCROLL_REFRESHING;
                    int newsPage = scrollRefreshing ? mNewsPage : 1;
                    mNewsPageEqualsTotalPage = UpdateDataTask
                            .updateNewsDataAndGetIsPageEqualsTotalPage(mActivity, mNewsType, newsPage);

                    return REFRESH_SUCCESS;
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
                switch (integer) {
                    case NETWORK_ERROR:
                        handleError(R.string.network_error_text);
                        break;
                    case SERVER_ERROR:
                        handleError(R.string.server_error_text);
                        break;
                    default:
                        if (mNewsPageEqualsTotalPage) {
                            break;
                        }

                        if (mRefreshState == RefreshState.SCROLL_REFRESHING) {
                            mNewsPage++;
                        } else {
                            mNewsPage = 2;
                        }
                }

                mNormalRecyclerView.setAutoRefreshing(false);
                mNormalRecyclerView.setSwipeRefreshing(false);
                mRefreshState = RefreshState.NO_REFRESHING;
            }

            private void handleError(@StringRes int errorTextRes) {
                if (mHasNewsData) {
                    mNewsPageEqualsTotalPage = true;
                    mNewsAdapter.notifyItemChanged(mNewsAdapter.getItemCount() - 1);
                } else {
                    mNormalRecyclerView.setErrorText(getString(errorTextRes));
                }
            }
        }.execute();
    }

    @NonNull
    private RecyclerView.OnScrollListener getOnRecyclerViewScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mItemPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    scrollRefresh();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnNewsMasterFragmentListener != null) {
                    mOnNewsMasterFragmentListener.onRecyclerViewScrolled(mLinearLayoutManager, dy);
                }
            }

            private void scrollRefresh() {
                if (mRefreshState != RefreshState.NO_REFRESHING || mNewsPageEqualsTotalPage) {
                    return;
                }

                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                int itemCount = mNewsAdapter.getItemCount();

                if (lastVisibleItemPosition >= itemCount - NetworkUtils.NEWS_COUNT_OF_EACH_PAGE) {
                    refreshNewsData(RefreshState.SCROLL_REFRESHING);
                }
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewsMasterFragmentListener) {
            mOnNewsMasterFragmentListener = (OnNewsMasterFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnNewsMasterFragmentListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_ITEM_POSITION, mItemPosition);
        outState.putInt(SAVE_NEWS_PAGE, mNewsPage);
        outState.putBoolean(SAVE_NEWS_TOTAL_PAGE_OUTED, mNewsPageEqualsTotalPage);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = NewsEntry._NEWS_TYPE + "=?";
        String[] selectionArgs = {mNewsType};
        return new CursorLoader(mActivity, NewsEntry.URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mNewsAdapter.swapCursor(cursor);
        if (cursor.getCount() > 0) {
            mHasNewsData = true;
            mNormalRecyclerView.setAutoRefreshing(false);
            mNormalRecyclerView.setErrorText(null);

            if (mViewRecreating) {
                mLinearLayoutManager.scrollToPosition(mItemPosition);
                mViewRecreating = false;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

    private class NewsAdapter extends RecyclerViewCursorAdapter
            implements News.OnNewsListener {

        private static final int VIEW_TYPE_LAST_ITEM = 0;
        private static final int VIEW_TYPE_OTHER_ITEM = 1;

        NewsAdapter(Context context, @LayoutRes int defaultResource) {
            super(context, defaultResource);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return VIEW_TYPE_LAST_ITEM;
            } else {
                return VIEW_TYPE_OTHER_ITEM;
            }
        }

        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
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
            News news = new News(mCursor, this, position);
            ViewDataBinding binding = holder.getBinding();

            binding.setVariable(BR.news, news);
            if (position == getItemCount() - 1) {
                binding.setVariable(BR.newsPageEqualsTotalPage, mNewsPageEqualsTotalPage);
            }

            binding.executePendingBindings();
        }

        @Override
        public void onClickItem(News news, int itemPosition) {
            mItemPosition = itemPosition;
            NewsDetailActivity.start(mContext, news);
        }
    }

    interface OnNewsMasterFragmentListener {

        void onRecyclerViewScrolled(LinearLayoutManager linearLayoutManager, int dy);
    }
}
