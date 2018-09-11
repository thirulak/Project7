package com.example.android.guardiannews1;

/**
 * Created by Meenakshi on 9/11/2018.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Meenakshi on 8/30/2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsClass>> {
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsLoader.class.getName();
    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsClass> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        List<NewsClass> news = Query.fetchNewsData(mUrl);
        return news;
    }
}
