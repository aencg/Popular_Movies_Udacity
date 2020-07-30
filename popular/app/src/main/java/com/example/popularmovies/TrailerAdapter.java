package com.example.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.popularmovies.data.Trailer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

        private List<Trailer> mTrailers;
        private Context mContext;

        /* An on-click handler that we've defined to make it easy for an Activity to interface with
         * our RecyclerView
         */
        private final TrailerAdapterOnClickHandler mClickHandler;

        /**
         * The interface that receives onClick messages.
         */
        public interface TrailerAdapterOnClickHandler {
            void onClick(Trailer trailerClicked);
        }

        /**
         * Creates a MovieAdapter.
         *
         * @param clickHandler The on-click handler for this adapter. This single handler is called
         *                     when an item is clicked.
         */
        public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler, Context context) {
            mClickHandler = clickHandler;
            mContext = context;
        }
        /**
         * Cache of the children views for a forecast list item.
         */
        public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final Button mButton;
            public final TextView mTextView;

            public TrailerAdapterViewHolder(View view) {
                super(view);
                mButton = (Button) view.findViewById(R.id.trailer_button) ;
                mTextView = (TextView) view.findViewById(R.id.trailer_textview);
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
                int adapterPosition = getAdapterPosition();
                mClickHandler.onClick(mTrailers.get(adapterPosition));
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
        public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.trailer_view;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new TrailerAdapterViewHolder(view);
        }

        /**
         * OnBindViewHolder is called by the RecyclerView to display the data at the specified
         * position. In this method, we update the contents of the ViewHolder to display the weather
         * details for this particular position, using the "position" argument that is conveniently
         * passed into us.
         *
         * @param TrailerAdapterViewHolder The ViewHolder which should be updated to represent the
         *                                  contents of the item at the given position in the data set.
         * @param position                  The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(TrailerAdapterViewHolder TrailerAdapterViewHolder, int position) {
            TrailerAdapterViewHolder.mTextView.setText(mContext.getString(R.string.trailer_label)+(position+1));
        }

        /**
         * This method simply returns the number of items to display. It is used behind the scenes
         * to help layout our Views and for animations.
         *
         * @return The number of items available in our forecast
         */
        @Override
        public int getItemCount() {
            if (null == mTrailers) return 0;
            return mTrailers.size();
        }

        /**
         * This method is used to set the weather forecast on a MovieAdapter if we've already
         * created one. This is handy when we get new data from the web but don't want to create a
         * new MovieAdapter to display it.
         *
         * @param trailers The new weather data to be displayed.
         */
        public void setTrailerData(List<Trailer> trailers) {
            mTrailers = trailers;
            //Log.e("cantidad",String.valueOf(mTrailers.size()));
            notifyDataSetChanged();
        }
    }