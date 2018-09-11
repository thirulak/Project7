package com.example.android.guardiannews1;

/**
 * Created by Meenakshi on 9/11/2018.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Meenakshi on 8/30/2018.
 */

public final class Query {
    private static final String LOG_TAG = Query.class.getSimpleName();

    private Query() {

    }


    public static List<NewsClass> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {

            Log.e(LOG_TAG, "It was not possible to connect to the server", e);
        }
        List<NewsClass> newsList = extractFeatureFromJson(jsonResponse);
        return newsList;

    }


    private static List<NewsClass> extractFeatureFromJson(String newJSON) {
        if (TextUtils.isEmpty(newJSON)) {
            return null;
        }

        List<NewsClass> newsList = new ArrayList<>();

        try {
            JSONObject baseJSONresponse = new JSONObject(newJSON);
            JSONObject responseObject = baseJSONresponse.getJSONObject("response");
            JSONArray newsArray = responseObject.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNews = newsArray.getJSONObject(i);

                String articleTitle = currentNews.getString("webTitle");
                if (articleTitle.contains("|")) {
                    String[] arrayString = articleTitle.split("\\|");
                    articleTitle = arrayString[0].trim(); //
                }
                String section = currentNews.getString("sectionName");
                String url = currentNews.getString("id");
                String date = currentNews.getString("webPublicationDate");
                date = formatDate(date);
                JSONArray tags = currentNews.getJSONArray("tags");
                String articleAuthor;
                if (tags.length() != 0) {
                    JSONObject tagsObject = tags.getJSONObject(0);
                    articleAuthor = tagsObject.getString("webTitle");
                } else articleAuthor = "No author, this is just a news";

                NewsClass newEntry = new NewsClass(articleTitle, section, articleAuthor, url, date);
                newsList.add(newEntry);
            }
        } catch (JSONException e) {
            Log.e("Query", "Problem to parse results", e);
        }
        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    // Taken from https://github.com/laramartin/android_newsfeed/blob/master/app/src/main/java/eu/laramartin/newsfeed/QueryUtils.java

    private static String formatDate(String rawDate) {
        String jsonDatePattern = "yyyy-MM-dd";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern);
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "yyyy-MM-dd";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("QueryUtils", "Error parsing JSON date: ", e);
            return "";
        }
    }
}