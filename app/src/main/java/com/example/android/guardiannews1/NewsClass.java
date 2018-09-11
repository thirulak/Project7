package com.example.android.guardiannews1;

/**
 * Created by Meenakshi on 9/11/2018.
 */

public class NewsClass {

    private String mTitle;
    private String mSection;
    private String mUrl;
    private String mDate;
    private String mAuthor;

    public NewsClass(String articleTitle, String section, String articleAuthor, String url, String date) {
        mTitle = articleTitle;
        mSection = section;
        mUrl = url;
        mDate = date;
        mAuthor = articleAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }


    public String getDate() {
        return mDate;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
