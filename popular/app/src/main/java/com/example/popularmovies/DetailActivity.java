package com.example.popularmovies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.example.popularmovies.data.AppDatabase;
import com.example.popularmovies.data.Movie;
import com.example.popularmovies.data.Review;
import com.example.popularmovies.data.Trailer;
import com.example.popularmovies.databinding.ActivityDetailBinding;
import com.example.popularmovies.utilities.JSONMovies;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class DetailActivity extends AppCompatActivity  implements TrailerAdapter.TrailerAdapterOnClickHandler {

    static final String KEY_MOVIE = "key_movie";
    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewsAdapter;

    boolean mIsFavorite = false;

    Movie movie;
    private AppDatabase mDb;
    ActivityDetailBinding mDetailBinding;



    private static final int REVIEW_LOADER_ID = 500;
    private static final int TRAILER_LOADER_ID = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


     //   supportPostponeEnterTransition();


        mDetailBinding= DataBindingUtil.setContentView(this,R.layout.activity_detail);
        getWindow().setAllowEnterTransitionOverlap(true);
        LinearLayoutManager linearLayoutManagerTrailers = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDetailBinding.recyclerviewTrailers.setLayoutManager(linearLayoutManagerTrailers);
        mDetailBinding.recyclerviewTrailers.setHasFixedSize(true);

        mDb = AppDatabase.getInstance(getApplicationContext());

        mDetailBinding.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFavorite = !mIsFavorite;
                if (!mIsFavorite) {
                    mDetailBinding.favoriteButton.setText(getText(R.string.mark_favorites));
                    mDetailBinding.favoriteButton.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                } else {
                    mDetailBinding.favoriteButton.setText(getText(R.string.unmark_favorites));
                    mDetailBinding.favoriteButton.setBackgroundColor(ContextCompat.getColor(v.getContext(), android.R.color.background_light));
                }
                onFavButtonClicked();
            }
        });

        mTrailerAdapter = new TrailerAdapter(this, this);
        mDetailBinding.recyclerviewTrailers.setAdapter(mTrailerAdapter);
        DividerItemDecoration decorationT = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mDetailBinding.recyclerviewTrailers.addItemDecoration(decorationT);

        LinearLayoutManager linearLayoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDetailBinding.recyclerviewReviews.setLayoutManager(linearLayoutManagerReviews);
        mDetailBinding.recyclerviewReviews.setHasFixedSize(true);

        mReviewsAdapter = new ReviewAdapter(this);
        mDetailBinding.recyclerviewReviews.setAdapter(mReviewsAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mDetailBinding.recyclerviewReviews.addItemDecoration(decoration);

        Intent intent = getIntent();
        if (intent.hasExtra("movie")) {
            movie = (Movie) intent.getSerializableExtra("movie");

            if(intent.hasExtra("position")){
                Log.e("position", String.valueOf(intent.getIntExtra("position",-1)));
            //    mDetailBinding.ivDetail.setTransitionName("cambio"+String.valueOf(intent.getIntExtra("position",-1)));
            }

            mDetailBinding.tvTitle.setText(movie.getTitle());
            mDetailBinding.tvVoteAverage.setText(movie.getVoteAverage() + "/10");
            mDetailBinding.tvReleaseDate.setText(movie.getReleaseDate().substring(0, 4));
            mDetailBinding.tvSynopsis.setText(movie.getSynopsis());




            supportPostponeEnterTransition();
            mDetailBinding.ivDetail.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            mDetailBinding.ivDetail.getViewTreeObserver().removeOnPreDrawListener(this);
                            supportStartPostponedEnterTransition();
                            return true;
                        }
                    }
            );

            if(intent.hasExtra("w") && intent.hasExtra("h")){
                int width = intent.getIntExtra("w",320);
                int height = intent.getIntExtra("h",320);
                mDetailBinding.ivDetail.setLayoutParams(new LayoutParams(width, height));
            }
            mDetailBinding.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click2","width:"+v.getWidth()+" height:"+v.getHeight());
                }
            });



            URL posterURL = NetworkUtils.buildUrlPoster(movie.getMoviePoster());
            Glide.with(this).load(String.valueOf(posterURL))
                    .centerCrop()
                    .into(mDetailBinding.ivDetail);
//            Picasso.get().setLoggingEnabled(true);
//            Picasso.get().load(String.valueOf(posterURL))
//                    .noFade()
//                    .noPlaceholder()
//                    .into(mDetailBinding.ivDetail, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                       //     scheduleStartPostponedTransition(mDetailBinding.ivDetail);
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                           // supportStartPostponedEnterTransition();
//                        }
//                    });
        }   else{
         //   supportStartPostponedEnterTransition();
        }


        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_MOVIE, movie.getId());
        LoaderManager.getInstance(this).initLoader(REVIEW_LOADER_ID, bundle, reviewLoaderListener);
        LoaderManager.getInstance(this).initLoader(TRAILER_LOADER_ID , bundle, trailerLoaderListener);
      //  supportStartPostponedEnterTransition();
        retrieveFavs();
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }



    private void retrieveFavs() {
        Log.d("tag", "Actively retrieving the tasks from the DataBase");
        LiveData<Movie> movieInDb = mDb.movieDao().loadMovieById(movie.getId());
        movieInDb.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                Log.d("tag", "Receiving database update from LiveData");
                if(movie!=null){
                    mIsFavorite=true;
                }   else{
                    mIsFavorite=false;
                }
                uiButtonFav();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Trailer trailerClicked) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailerClicked.getContent()));
        startActivity(intent);
    }

    private void uiButtonFav(){
        if (!mIsFavorite) {
            mDetailBinding.favoriteButton.setText(getText(R.string.mark_favorites));
            mDetailBinding.favoriteButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            mDetailBinding.favoriteButton.setText(getText(R.string.unmark_favorites));
            mDetailBinding.favoriteButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.background_light));
        }
    }


    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoaderListener
            = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Review>>(getApplicationContext()) {
                List<Review> mReviewList;

                @Override
                protected void onStartLoading() {

                    /* If no arguments were passed, we don't have a query to perform. Simply return. */
                    if (args == null) {
                        return;
                    }

                    /*
                     * If we already have cached results, just deliver them now. If we don't have any
                     * cached results, force a load.
                     */
                    if (mReviewList != null) {
                        deliverResult(mReviewList);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Review> loadInBackground() {

                    String idMovie = args.getString(KEY_MOVIE);
                    if (idMovie==null) {
                        return null;
                    }
                    List<Review> reviews = null;


                    URL moviesRequestUrl = NetworkUtils.buildUrlReviews(movie.getId());
                    Log.e("review desde internet","desde internet");
                    //Log.e("hola",moviesRequestUrl.toString());
                    try {
                        String jsonReviewResponse =  NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                        //Log.e("jsonReview",jsonReviewResponse);
                        reviews = JSONMovies.getReviewsFromJson(getApplicationContext(), jsonReviewResponse);
                    } catch (Exception e) {
                        Log.e("e",e.toString());
                        e.printStackTrace();
                        return null;
                    }
                    return reviews;
                }
                @Override
                public void deliverResult(List<Review> results) {
                    mReviewList = results;
                    super.deliverResult(mReviewList);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            mReviewsAdapter.setReviewsData(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {
            /*
             * We aren't using this method in our example application, but we are required to Override
             * it to implement the LoaderCallbacks<String> interface
             */
        }
    };

    private LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoaderListener
            = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<Trailer>>(getApplicationContext()) {
                List<Trailer> mTrailerList;

                @Override
                protected void onStartLoading() {

                    /* If no arguments were passed, we don't have a query to perform. Simply return. */
                    if (args == null) {
                        return;
                    }

                    /*
                     * If we already have cached results, just deliver them now. If we don't have any
                     * cached results, force a load.
                     */
                    if (mTrailerList != null) {
                        deliverResult(mTrailerList);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Trailer> loadInBackground() {

                    String idMovie = args.getString(KEY_MOVIE);
                    if (idMovie==null) {
                        return null;
                    }
                    List<Trailer> trailers = null;


                    URL moviesRequestUrl = NetworkUtils.buildUrlTrailers(movie.getId());
                    Log.e("review desde internet","desde internet");
                    //Log.e("hola",moviesRequestUrl.toString());
                    try {
                        String jsonReviewResponse =  NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                        //Log.e("jsonReview",jsonReviewResponse);
                        trailers = JSONMovies.getTrailersFromJson(getApplicationContext(), jsonReviewResponse);
                    } catch (Exception e) {
                        Log.e("e",e.toString());
                        e.printStackTrace();
                        return null;
                    }
                    return trailers;
                }
                @Override
                public void deliverResult(List<Trailer> results) {
                    mTrailerList = results;
                    super.deliverResult(mTrailerList);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            mTrailerAdapter.setTrailerData(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            /*
             * We aren't using this method in our example application, but we are required to Override
             * it to implement the LoaderCallbacks<String> interface
             */
        }
    };




    public void onFavButtonClicked() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mIsFavorite) {
                    mDb.movieDao().insertMovie(movie);
                    Log.e("insert fav",movie.toString());
                } else {
                    mDb.movieDao().deleteMovie(movie);
                    Log.e("delete fav",movie.toString());
                }
            }
        });
    }
}
