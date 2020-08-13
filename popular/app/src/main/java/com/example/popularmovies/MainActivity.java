package com.example.popularmovies;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.example.popularmovies.data.AppDatabase;
import com.example.popularmovies.data.Movie;
import com.example.popularmovies.databinding.ActivityMainBinding;
import com.example.popularmovies.utilities.JSONMovies;
import com.example.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>> {
    ActivityMainBinding mBinding;

    List<Movie> mMovies;
    List<Movie> mMoviesFav;
    MovieAdapter mMovieAdapter;

    private int mMode;

    private static final int MOVIE_LIST_LOADER = 11;

    final static int RATINGS_MODE = 1;
    final static int POPULAR_MODE = 2;
    final static int FAVORITE_MODE = 3;

    final static String KEY_MODE ="key_mode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // supportPostponeEnterTransition();
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_main );

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(getString(R.string.order_by_key_prefs))){
            mMode = sharedPreferences.getInt(getString(R.string.order_by_key_prefs),RATINGS_MODE);
        }   else{
            mMode = RATINGS_MODE;
        }

        int spanCount;
        Configuration config = getResources().getConfiguration();
        switch(config.orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                spanCount = 4;
                break;
                default:
                    spanCount = 2;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this,spanCount,GridLayoutManager.VERTICAL,false);
        mBinding.recyclerview.setLayoutManager(layoutManager);
       // mBinding.recyclerview.setItemAnimator(new DefaultItemAnimator());
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mBinding.recyclerview.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);
        mMovieAdapter.setmFinCargaListener(new MovieAdapter.FinCargaListener() {
            @Override
            public void findeCarga() {
            //    startPostponedEnterTransition();
            }
        });

        mBinding.recyclerview.setAdapter(mMovieAdapter);
        setupViewModel();
        loadMovies(mMode);

      //  startPostponedEnterTransition();
    }


    private void setupViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d("tag", "Updating list of movies from LiveData in ViewModel");
                mMoviesFav = movies;
                if(mMode==FAVORITE_MODE){
                    loadUIinFavMode();
                }
            }
        });
    }



    void loadUIinFavMode(){
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        mMovies = mMoviesFav;
        if (mMoviesFav!=null && mMoviesFav.size()!=0 ) {
            mBinding.recyclerview.setVisibility(View.VISIBLE);
            mBinding.tvMainFeedback.setVisibility(View.INVISIBLE);
            mMovies = mMoviesFav;
            mMovieAdapter.setMovieData(mMovies);
        } else {
            if (mMoviesFav==null ) Log.e("change","moviesFav = null");
                    else   if ( mMoviesFav.size()==0 )Log.e("change","moviesFav size = 0");
            mBinding.tvMainFeedback.setVisibility(View.VISIBLE);
            mBinding.tvMainFeedback.setText(getText(R.string.no_favs));
        }
//        if(mMovies!=null)
//            Log.e("Favs",mMovies.toString());
        mMovieAdapter.setMovieData(mMovies);


    //    supportStartPostponedEnterTransition();
    }

    public void saveMode(int mode){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.order_by_key_prefs), mode);
        editor.commit();
        mMode = mode;
    }

    public void loadMovies(int mode){
        saveMode(mode);

        if(mMode == FAVORITE_MODE){
            loadUIinFavMode();
        }   else {

            Bundle queryBundle = new Bundle();
            queryBundle.putInt(KEY_MODE, mMode);

            LoaderManager.getInstance(this).initLoader( MOVIE_LIST_LOADER, queryBundle, this);
        }
    }

    @Override
    public void onClick(Movie movieClicked, View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie",movieClicked);
        intent.putExtra("position",mMovies.indexOf(movieClicked));

        Log.e("transition name", view.getTransitionName());

      /*  funciona con explosion
      startActivity(intent,
                makeSceneTransitionAnimation(this).toBundle()); //*/


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = view.getWidth() / displayMetrics.density;
        float dpWidth = view.getHeight() / displayMetrics.density;
//Log.e("tamaño",(view.getWidth()*view.getScaleY())+"w "+(view.getHeight()*view.getScaleY())+"h");
        Log.e("tamaño",dpWidth+"w "+dpHeight+"h");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,   view, view.getTransitionName());
        startActivity(intent, options.toBundle());      //*/

//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_up, R.anim.slide_down);
       // startActivity(intent, options.toBundle());
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> mMovieList;

            @Override
            protected void onStartLoading() {
                mBinding.tvMainFeedback.setVisibility(View.INVISIBLE);
                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (args == null) {
                    return;
                }

                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                if (mMovieList != null) {
                    deliverResult(mMovieList);
                } else {
                    /*
                     * When we initially begin loading in the background, we want to display the
                     * loading indicator to the user
                     */
                    mBinding.loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {

                int mode = args.getInt(KEY_MODE);
                if (mode==0) {
                    return null;
                }
                List<Movie> movies = null;
                URL moviesRequestUrl = NetworkUtils.buildUrlMovie(mode);
                //Log.e("hola",moviesRequestUrl.toString());
                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                    movies = JSONMovies.getMoviesFromJson(MainActivity.this, jsonMovieResponse);
                } catch (Exception e) {
                    Log.e("e", e.toString());
                    e.printStackTrace();
                    return null;
                }
                 return movies;
            }
            @Override
            public void deliverResult(List<Movie> results) {
                mMovieList = results;
                super.deliverResult(mMovieList);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if(mMode!=FAVORITE_MODE){
            mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
            if (data != null) {
                mBinding.recyclerview.setVisibility(View.VISIBLE);
                mMovies = data;
                mMovieAdapter.setMovieData(data);

               // supportStartPostponedEnterTransition();
            } else {
                mBinding.tvMainFeedback.setVisibility(View.VISIBLE);
                mBinding.tvMainFeedback.setText(getText(R.string.error_message));
                mBinding.recyclerview.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        /*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        boolean known = false;
        switch (itemId) {
            case R.id.action_menu_fav:
                saveMode(FAVORITE_MODE);
                loadUIinFavMode();
                return true;
            case R.id.action_menu_rated:
                saveMode(RATINGS_MODE);
                known = true;
                break;
            case R.id.action_menu_popular:
                saveMode(POPULAR_MODE);
                known = true;
                break;
        }
        if(known){
            Bundle queryBundle = new Bundle();
            queryBundle.putInt(KEY_MODE, mMode);
            LoaderManager.getInstance(this).restartLoader( MOVIE_LIST_LOADER, queryBundle, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
