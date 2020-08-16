package com.example.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.data.Movie;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

        private List<Movie> mMovieData;
        private Context mContext;

        /* An on-click handler that we've defined to make it easy for an Activity to interface with
         * our RecyclerView
         */

        private  FinCargaListener mFinCargaListener;

        /**
         * The interface that receives onClick messages.
         */
        public interface FinCargaListener {
            void findeCarga( );
        }

        public void setmFinCargaListener(FinCargaListener finCargaListener){
            mFinCargaListener = finCargaListener;
        }

        private final MovieAdapterOnClickHandler mClickHandler;

        /**
         * The interface that receives onClick messages.
         */
        public interface MovieAdapterOnClickHandler {
            void onClick(Movie movieClicked, View view);
        }

        /**
         * Creates a MovieAdapter.
         *
         * @param clickHandler The on-click handler for this adapter. This single handler is called
         *                     when an item is clicked.
         */
        public MovieAdapter(MovieAdapterOnClickHandler clickHandler, Context context) {
            mClickHandler = clickHandler;
            mContext = context;
        }
        /**
         * Cache of the children views for a forecast list item.
         */
        public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final ImageView mImageView;

            public MovieAdapterViewHolder(View view) {
                super(view);
                mImageView = (ImageView) view.findViewById(R.id.iv_item_lista) ;
                mImageView.getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                                ((AppCompatActivity) mContext).supportStartPostponedEnterTransition();
                                return true;
                            }
                        });
                view.setOnClickListener(this);
            }
             /**
             * This gets called by the child views during a click.
             *
             * @param v The View that was clicked
             */
            @Override
            public void onClick(View v) {
                int adapterPosition = getAdapterPosition();
                Movie movieClicked = mMovieData.get(adapterPosition) ;
                mClickHandler.onClick(movieClicked, mImageView);
            }
        }

        /**
         * This gets called when each new ViewHolder is created. This happens when the RecyclerView
         * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
         *
         * @param viewGroup The ViewGroup that these ViewHolders are contained within.
         * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
         *                  can use this viewType integer to provide a different layout. See
         *            ///      link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
         *                  for more details.
         * @return A new MovieAdapterViewHolder that holds the View for each list item
         */
        @Override
        public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.movie_list_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new MovieAdapterViewHolder(view);
        }

        /**
         * OnBindViewHolder is called by the RecyclerView to display the data at the specified
         * position. In this method, we update the contents of the ViewHolder to display the weather
         * details for this particular position, using the "position" argument that is conveniently
         * passed into us.
         *
         * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
         *                                  contents of the item at the given position in the data set.
         * @param position                  The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
            URL posterURL = NetworkUtils.buildUrlPoster(mMovieData.get(position).getMoviePoster());

            Glide.with(mContext).load(String.valueOf(posterURL))
                    .centerCrop()
                    .into(movieAdapterViewHolder.mImageView);
//            Picasso.get().setLoggingEnabled(true);
//            Picasso.get().load(String.valueOf(posterURL))
//                  //  .noFade()
//                 //   .noPlaceholder()
//                    .into(movieAdapterViewHolder.mImageView, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            if(mFinCargaListener!=null){
//                                mFinCargaListener.findeCarga();
//                            }
//                            //startPostponedEnterTransition();
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            if(mFinCargaListener!=null){
//                                mFinCargaListener.findeCarga();
//                            }
//                        }
//                    });

          //  movieAdapterViewHolder.mImageView.setTransitionName("cambio"+movieAdapterViewHolder.getAdapterPosition());
        }

        /**
         * This method simply returns the number of items to display. It is used behind the scenes
         * to help layout our Views and for animations.
         *
         * @return The number of items available in our forecast
         */
        @Override
        public int getItemCount() {
            if (null == mMovieData) return 0;
            return mMovieData.size();
        }

        /**
         * This method is used to set the weather forecast on a MovieAdapter if we've already
         * created one. This is handy when we get new data from the web but don't want to create a
         * new MovieAdapter to display it.
         *
         * @param movieData The new weather data to be displayed.
         */
        public void setMovieData(List<Movie> movieData) {
            mMovieData = movieData;
            if(mMovieData!=null)
                //Log.e("cantidad",String.valueOf(mMovieData.size()));
            notifyDataSetChanged();
        }
    }