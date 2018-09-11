package com.example.android.guardiannews1;

/**
 * Created by Meenakshi on 9/11/2018.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Meenakshi on 8/30/2018.
 */

public class NewsAdapter extends ArrayAdapter<NewsClass> {

    public NewsAdapter(Activity context, ArrayList<NewsClass> NewsActivity) {
        super(context, 0, NewsActivity);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        NewsClass news = getItem(position);


        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);


        }
        TextView articleTitle = listItemView.findViewById(R.id.title);
        assert news != null;
        articleTitle.setText(news.getTitle());

        TextView section = listItemView.findViewById(R.id.Section);
        section.setText(news.getSection());

        TextView date = listItemView.findViewById(R.id.date);
        date.setText(news.getDate());

        TextView articleAuthor = listItemView.findViewById(R.id.articleAuthor);
        articleAuthor.setText(news.getAuthor());

        return listItemView;

    }
}