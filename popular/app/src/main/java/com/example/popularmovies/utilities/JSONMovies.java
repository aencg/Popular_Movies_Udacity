package com.example.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import com.example.popularmovies.data.Movie;
import com.example.popularmovies.data.Review;
import com.example.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class JSONMovies {

    public static List<Movie> getMoviesFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVERAGE = "vote_average";
        final String OVERVIEW = "overview";
        final String ID = "id";

        ArrayList<Movie> movieList = new ArrayList<Movie>();

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        final String HTTP_STATUS_CODE ="status_code";

        /* Is there an error? */
        if (forecastJson.has(HTTP_STATUS_CODE)) {
            int errorCode = forecastJson.getInt(HTTP_STATUS_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }


        JSONArray resultsJson = forecastJson.getJSONArray(RESULTS);
        for(int i = 0; i<resultsJson.length(); i++){
            Movie movie = new Movie();
            String id = resultsJson.getJSONObject(i).getString(ID);
            movie.setId(id);
            String title = resultsJson.getJSONObject(i).getString(TITLE);
            movie.setTitle(title);
            String releaseDate = resultsJson.getJSONObject(i).getString(RELEASE_DATE);
            movie.setReleaseDate(releaseDate);
            String moviePoster = resultsJson.getJSONObject(i).getString(POSTER_PATH);
            movie.setMoviePoster(moviePoster);
            String voteAverage = resultsJson.getJSONObject(i).getString(VOTE_AVERAGE);
            movie.setVoteAverage(voteAverage);
            String synopsis = resultsJson.getJSONObject(i).getString(OVERVIEW);
            movie.setSynopsis(synopsis);
            movieList.add(movie);
           // Log.e("m",movie.toString());
        }
        return movieList;
    }


    public static List<Review> getReviewsFromJson(Context context, String jsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        ArrayList<Review> reviewList = new ArrayList<Review>();

        JSONObject forecastJson = new JSONObject(jsonStr);

        final String HTTP_STATUS_CODE ="status_code";

        /* Is there an error? */
        if (forecastJson.has(HTTP_STATUS_CODE)) {
            int errorCode = forecastJson.getInt(HTTP_STATUS_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }


        String idMovie = forecastJson.getString("id");
        JSONArray resultsJson = forecastJson.getJSONArray(RESULTS);
        for(int i = 0; i<resultsJson.length(); i++){
            Review review = new Review();

            //Log.e("item",resultsJson.getJSONObject(i).toString());
            String author = resultsJson.getJSONObject(i).getString(AUTHOR);
            review.setAuthor(author);

          //  Log.e("itemreview",author);

            String content = resultsJson.getJSONObject(i).getString(CONTENT);
           // movie.setSynopsis(synopsis);
           // JSONArray contentArray = forecastJson.getJSONArray(CONTENT);

          //  Log.e("content",content);
            review.setIdMovie(idMovie);
           review.setContent(content);
            reviewList.add(review);
        }
        return reviewList;
    }

    public static List<Trailer> getTrailersFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String KEY_VIDEO = "key";

        ArrayList<Trailer> trailerList = new ArrayList<Trailer>();

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        final String HTTP_STATUS_CODE ="status_code";

        /* Is there an error? */
        if (forecastJson.has(HTTP_STATUS_CODE)) {
            int errorCode = forecastJson.getInt(HTTP_STATUS_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        String idMovie = forecastJson.getString("id");

        JSONArray resultsJson = forecastJson.getJSONArray(RESULTS);
        for(int i = 0; i<resultsJson.length(); i++){
            Trailer trailer = new Trailer();
            String keyVideo = resultsJson.getJSONObject(i).getString(KEY_VIDEO);
            trailer.setIdMovie(idMovie);
            trailer.setContent(keyVideo);
            trailerList.add(trailer);
            // Log.e("m",movie.toString());
        }
        return trailerList;
    }
}
