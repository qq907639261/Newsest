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
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhbb.qinzl.newsest.async.UpdateDataTask;
import com.xhbb.qinzl.newsest.common.RecyclerViewCursorAdapter;
import com.xhbb.qinzl.newsest.data.Contract.NewsEntry;
import com.xhbb.qinzl.newsest.databinding.FragmentNewsMasterBinding;
import com.xhbb.qinzl.newsest.viewmodel.News;
import com.xhbb.qinzl.newsest.viewmodel.NewsMaster;

import org.json.JSONException;

import java.io.IOException;

public class NewsMasterFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_NEWS_TYPE = "ARG_NEWS_TYPE";

    private Activity mActivity;
    private NewsAdapter mNewsAdapter;
    private NewsMaster mNewsMaster;
    private String mNewsType;
    private int mNewsPage;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_master, container, false);
        FragmentNewsMasterBinding binding = DataBindingUtil.bind(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mNewsMaster = new NewsMaster(mActivity, mNewsAdapter, layoutManager);

        binding.setNewsMaster(mNewsMaster);

        getLoaderManager().initLoader(0, null, this);
        refreshNewsData();

        return view;
    }

    private void refreshNewsData() {
        new AsyncTask<Void, Void, Integer>() {
            private static final int NETWORK_ERROR = -1;
            private static final int SERVER_ERROR = 0;
            private static final int SUCCESS = 1;

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    UpdateDataTask.downloadNewsDataIntoDatabase(mActivity, mNewsType, mNewsPage);
                    return SUCCESS;
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
                mNewsMaster.setAutoRefreshing(false);
                switch (integer) {
                    case NETWORK_ERROR:
                        mNewsMaster.setNetworkingFailedText(getString(R.string.network_error_text));
                        break;
                    case SERVER_ERROR:
                        mNewsMaster.setNetworkingFailedText(getString(R.string.server_error_text));
                        break;
                    default:
                        mNewsPage++;
                }
            }
        }.execute();
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

        if (cursor.getCount() == 0) {
            return;
        }

        mNewsMaster.setAutoRefreshing(false);
        mNewsMaster.setNetworkingFailedText("");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

    private class NewsAdapter extends RecyclerViewCursorAdapter {

        NewsAdapter(Context context, @LayoutRes int defaultResource) {
            super(context, defaultResource);
        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            mCursor.moveToPosition(position);
            News news = new News(mContext, mCursor);

            ViewDataBinding binding = holder.getBinding();
            binding.setVariable(BR.news, news);
            binding.executePendingBindings();
        }
    }
}
