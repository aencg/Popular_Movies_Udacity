package com.example.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.data.Review;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

        private List<Review> mReviews;
        private Context mContext;

        /**
         * Creates a MovieAdapter.
         *
         */
        public ReviewAdapter( Context context) {
            mContext = context;
        }
        /**
         * Cache of the children views for a forecast list item.
         */
        public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final TextView mAuthorTextView;
            public final TextView mContentTextView;

            public ReviewAdapterViewHolder(View view) {
                super(view);


                mAuthorTextView = (TextView) view.findViewById(R.id.author_textview) ;
                mContentTextView = (TextView) view.findViewById(R.id.content_textview) ;
                view.setOnClickListener(this);
            }
             /**
             * This gets called by the child views during a click.
             *
             * @param v The View that was clicked
             */
            @Override
            public void onClick(View v) {

                //intent
                //int adapterPosition = getAdapterPosition();
                //String reviewClicked = mReviews.get(adapterPosition) ;
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
        public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.review_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new ReviewAdapterViewHolder(view);
        }

        /**
         * OnBindViewHolder is called by the RecyclerView to display the data at the specified
         * position. In this method, we update the contents of the ViewHolder to display the weather
         * details for this particular position, using the "position" argument that is conveniently
         * passed into us.
         *
         * @param ReviewAdapterViewHolder The ViewHolder which should be updated to represent the
         *                                  contents of the item at the given position in the data set.
         * @param position                  The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(ReviewAdapterViewHolder ReviewAdapterViewHolder, int position) {

            ReviewAdapterViewHolder.mAuthorTextView.setText(mReviews.get(position).getAuthor());
            ReviewAdapterViewHolder.mContentTextView.setText(mReviews.get(position).getContent());

        }

        /**
         * This method simply returns the number of items to display. It is used behind the scenes
         * to help layout our Views and for animations.
         *
         * @return The number of items available in our forecast
         */
        @Override
        public int getItemCount() {
            if (null == mReviews) return 0;
            return mReviews.size();
        }

        /**
         * This method is used to set the weather forecast on a MovieAdapter if we've already
         * created one. This is handy when we get new data from the web but don't want to create a
         * new MovieAdapter to display it.
         *
         * @param reviews The new weather data to be displayed.
         */
        public void setReviewsData(List<Review> reviews) {
            mReviews = reviews;
            //Log.e("cantidad",String.valueOf(mReviews.size()));
            notifyDataSetChanged();
        }
    }