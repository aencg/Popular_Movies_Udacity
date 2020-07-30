/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */

    /* The format we want our API to return */
    private static final String format = "json";

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE_MOVIE  = "http://api.themoviedb.org/3/movie";
    private static final String API_KEY_QUERY = "api_key";


    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";

    /****the moviedb api key***/
    private static final String KEY = " ";

    private static final String PATH_RATED = "top_rated";
    private static final String PATH_POPULAR = "popular";

    private static final String PATH_TRAILERS= "videos";
    private static final String PATH_REVIEWS = "reviews";


    //private static final String SIZE = "w185";
    private static final String SIZE = "w500";

    final static int RATINGS_MODE = 1;
    final static int POPULAR_MODE = 2;
    final static int FAVORITE_MODE = 3;


    public static URL buildUrlMovie(int mode) {

        Uri.Builder builder = Uri.parse(BASE_MOVIE).buildUpon();
        if(mode==POPULAR_MODE){
            builder.appendPath(PATH_POPULAR);
        } else{
            builder.appendPath(PATH_RATED);
        }

        Uri builtUri = builder.appendQueryParameter(API_KEY_QUERY, KEY)
                .appendQueryParameter("page", "1")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "Built URI " + url);

        return url;
    }


    public static URL buildUrlYoutube(String video) {

        Uri.Builder builder = Uri.parse(BASE_YOUTUBE_URL).buildUpon();
        builder.appendQueryParameter("v",video);
//https://www.youtube.com/watch?v=jKCj3XuPG8M
        Uri builtUri =  builder  .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static URL buildUrlReviews(String id) {

        Uri.Builder builder = Uri.parse(BASE_MOVIE).buildUpon();

        builder.appendPath(id);
        builder.appendPath(PATH_REVIEWS);

        Uri builtUri = builder.appendQueryParameter(API_KEY_QUERY, KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlTrailers(String id) {

        Uri.Builder builder = Uri.parse(BASE_MOVIE).buildUpon();

        builder.appendPath(id);
        builder.appendPath(PATH_TRAILERS);

        Uri builtUri = builder.appendQueryParameter(API_KEY_QUERY, KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static URL buildUrlPoster(String posterPath) {

        Uri.Builder builder = Uri.parse(BASE_IMAGE_URL).buildUpon();


        Uri builtUri = builder.appendPath(SIZE)
                .appendEncodedPath(posterPath)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}