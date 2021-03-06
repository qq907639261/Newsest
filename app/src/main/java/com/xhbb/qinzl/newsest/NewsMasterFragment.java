package com.xhbb.qinzl.newsest;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xhbb.qinzl.newsest.async.MainTasks;
import com.xhbb.qinzl.newsest.common.MainEnums.RefreshState;
import com.xhbb.qinzl.newsest.common.RecyclerViewCursorAdapter;
import com.xhbb.qinzl.newsest.common.ValuesUtils;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.databinding.FragmentNormalRecyclerViewBinding;
import com.xhbb.qinzl.newsest.server.JsonUtils;
import com.xhbb.qinzl.newsest.server.NetworkUtils;
import com.xhbb.qinzl.newsest.viewmodel.NewsMaster;
import com.xhbb.qinzl.newsest.viewmodel.NormalRecyclerView;

public class NewsMasterFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        NormalRecyclerView.OnNormalRecyclerViewListener,
        Response.Listener<String>,
        Response.ErrorListener {

    private static final String ARG_NEWS_TYPE = "ARG_NEWS_TYPE";
    private static final String SAVE_ITEM_POSITION = "SAVE_ITEM_POSITION";
    private static final String SAVE_NEWS_PAGE = "SAVE_NEWS_PAGE";
    private static final String SAVE_NEWS_PAGE_EQUALS_TOTAL_PAGE = "SAVE_NEWS_PAGE_EQUALS_TOTAL_PAGE";

    private boolean mHasNewsData;
    private int mItemPosition;
    private int mNewsPage;
    private boolean mNewsPageEqualsTotalPage;
    private String mNewsType;
    private int mRefreshState;
    private Context mContext;
    private NewsAdapter mNewsAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private NormalRecyclerView mNormalRecyclerView;
    private OnNewsMasterFragmentListener mOnNewsMasterFragmentListener;
    private boolean mViewRecreating;
    private boolean mTwoPane;
    private boolean mFirstCreating;

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

        mRefreshState = RefreshState.NO_REFRESHING;

        if (savedInstanceState != null) {
            mItemPosition = savedInstanceState.getInt(SAVE_ITEM_POSITION);
            mNewsPage = savedInstanceState.getInt(SAVE_NEWS_PAGE);
            mNewsPageEqualsTotalPage = savedInstanceState.getBoolean(SAVE_NEWS_PAGE_EQUALS_TOTAL_PAGE);
        } else {
            mFirstCreating = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentNormalRecyclerViewBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_normal_recycler_view, container, false);
        Bundle args = getArguments();

        mNewsType = args.getString(ARG_NEWS_TYPE);
        mContext = getContext();
        mNewsAdapter = new NewsAdapter(mContext, R.layout.item_news);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mNormalRecyclerView = new NormalRecyclerView(mNewsAdapter, mLinearLayoutManager, this);
        mTwoPane = getActivity().findViewById(R.id.detail_fragment_container) != null;

        getLoaderManager().initLoader(0, null, this);
        binding.setNormalRecyclerView(mNormalRecyclerView);

        if (savedInstanceState == null) {
            refreshNewsData(RefreshState.AUTO_REFRESHING);
        } else {
            mViewRecreating = true;
        }

        return binding.getRoot();
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
    public void onSwipeRefresh() {
        refreshNewsData(RefreshState.SWIPE_REFRESHING);
    }

    public void refreshNewsData(int refreshState) {
        mRefreshState = refreshState;

        boolean scrollRefreshing = mRefreshState == RefreshState.SCROLL_REFRESHING;
        int newsPage = scrollRefreshing ? mNewsPage : 1;
        NetworkUtils.addNewsRequestToRequestQueue(mContext, mNewsType, newsPage, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (mHasNewsData) {
            mNewsPageEqualsTotalPage = true;
            mNewsAdapter.notifyItemChanged(mNewsAdapter.getItemCount() - 1);
        } else {
            if (isAdded()) {
                mNormalRecyclerView.setErrorText(getString(R.string.network_error_text));
            }
        }
        finishRefresh();
    }

    @Override
    public void onResponse(String response) {
        if (mRefreshState != RefreshState.SCROLL_REFRESHING) {
            mNewsPage = 1;
        }

        ContentValues[] newsValueses = JsonUtils.getNewsValueses(response, mNewsType);
        MainTasks.updateNewsData(mContext, newsValueses, mNewsPage);

        mNewsPage++;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SAVE_ITEM_POSITION, mItemPosition);
        outState.putInt(SAVE_NEWS_PAGE, mNewsPage);
        outState.putBoolean(SAVE_NEWS_PAGE_EQUALS_TOTAL_PAGE, mNewsPageEqualsTotalPage);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = NewsEntry._NEWS_TYPE + "=?";
        String[] selectionArgs = {mNewsType};
        return new CursorLoader(mContext, NewsEntry.URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            int totalPage = cursor.getInt(cursor.getColumnIndex(NewsEntry._TOTAL_PAGE_BY_TYPE));
            mNewsPageEqualsTotalPage = mNewsPage == totalPage;

            if (mViewRecreating) {
                mLinearLayoutManager.scrollToPosition(mItemPosition);
            } else if (mFirstCreating && mTwoPane) {
                replaceDetailFragment(cursor);
            }

            mHasNewsData = true;
            mNormalRecyclerView.setErrorText(null);
            finishRefresh();
        } else if (mViewRecreating) {
            refreshNewsData(RefreshState.AUTO_REFRESHING);
        }
        mViewRecreating = false;
        mNewsAdapter.swapCursor(cursor);
    }

    private void replaceDetailFragment(final Cursor cursor) {
        View rootView = getView();
        if (isAdded() && rootView != null) {
            rootView.post(new Runnable() {
                @Override
                public void run() {
                    ContentValues newsValues = ValuesUtils.getNewsValues(cursor);
                    NewsDetailFragment fragment = NewsDetailFragment.newInstance(newsValues);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.detail_fragment_container, fragment)
                            .commit();
                }
            });
        }
    }

    private void finishRefresh() {
        mFirstCreating = false;
        mRefreshState = RefreshState.NO_REFRESHING;
        mNormalRecyclerView.setSwipeRefreshing(false);
        mNormalRecyclerView.setAutoRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            mItemPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            scrollRefresh();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dy) {
        if (mOnNewsMasterFragmentListener != null) {
            mOnNewsMasterFragmentListener.onRecyclerViewScrolled(mLinearLayoutManager, dy);
        }
    }

    private class NewsAdapter extends RecyclerViewCursorAdapter
            implements NewsMaster.OnNewsMasterListener {

        private static final int VIEW_TYPE_LAST_ITEM = 0;
        private static final int VIEW_TYPE_OTHER_ITEM = 1;

        NewsAdapter(Context context, int defaultLayoutRes) {
            super(context, defaultLayoutRes);
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
        private BindingHolder getBindingHolder(ViewGroup parent, int layoutRes) {
            View view = LayoutInflater.from(mContext).inflate(layoutRes, parent, false);
            return new BindingHolder(view);
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            ViewDataBinding binding = holder.getBinding();

            binding.setVariable(BR.newsMaster, getNewsMaster(position));
            if (position == getItemCount() - 1) {
                binding.setVariable(BR.alreadyToBottom, mNewsPageEqualsTotalPage);
            }

            binding.executePendingBindings();
        }

        @NonNull
        private NewsMaster getNewsMaster(int position) {
            mCursor.moveToPosition(position);

            String title = mCursor.getString(mCursor.getColumnIndex(NewsEntry._TITLE));
            String imageUrl1 = mCursor.getString(mCursor.getColumnIndex(NewsEntry._IMAGE_URL_1));
            String imageUrl2 = mCursor.getString(mCursor.getColumnIndex(NewsEntry._IMAGE_URL_2));
            String imageUrl3 = mCursor.getString(mCursor.getColumnIndex(NewsEntry._IMAGE_URL_3));

            return new NewsMaster(title, imageUrl1, imageUrl2, imageUrl3, position, this);
        }

        @Override
        public void onClickItem(int itemPosition, View sharedElement) {
            mCursor.moveToPosition(itemPosition);
            mItemPosition = itemPosition;

            ContentValues newsValues = ValuesUtils.getNewsValues(mCursor);
            if (mTwoPane) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, NewsDetailFragment.newInstance(newsValues))
                        .addSharedElement(sharedElement, ViewCompat.getTransitionName(sharedElement))
                        .commit();
            } else {
                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) mContext,
                        sharedElement,
                        ViewCompat.getTransitionName(sharedElement)).toBundle();

                NewsDetailActivity.start(mContext, newsValues, options);
            }
        }
    }

    interface OnNewsMasterFragmentListener {

        void onRecyclerViewScrolled(LinearLayoutManager linearLayoutManager, int dy);
    }
}
