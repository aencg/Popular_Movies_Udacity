package com.example.popularmovies.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.example.popularmovies.DetailActivity;
import com.example.popularmovies.R;
import com.example.popularmovies.data.Movie;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class NotificationUtilities {
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 20;
    private static final int MOVIE_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int MOVIE_PENDING_INTENT_ID = 3417;
    /**
     * This notification channel id is used to link notifications to this channel
     */
    private static final String MOVIE_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    public static void createNotificationMovie(Context context, Movie movie) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    MOVIE_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MOVIE_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.movies)
                .setLargeIcon(getBitmapFromURL(NetworkUtils.buildUrlPoster(movie.getMoviePoster()).toString()))
                .setContentTitle(context.getText(R.string.mostPopular)+" "+movie.getTitle())
                .setContentText(movie.getSynopsis().substring(0,32))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        movie.getSynopsis()))
                .setContentIntent(contentIntent(context, movie))
//                .addAction(dismissAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(MOVIE_NOTIFICATION_ID, notificationBuilder.build());
    }


    private static PendingIntent contentIntent(Context context, Movie movie) {
        Intent startActivityIntent = new Intent(context, DetailActivity.class);

        startActivityIntent.putExtra("movie", movie);
        return PendingIntent.getActivity(
                context,
                MOVIE_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            //make the bitmap square
            myBitmap = Bitmap.createBitmap(myBitmap, 0,0, myBitmap.getWidth(), myBitmap.getWidth());
            return myBitmap;
        } catch (Exception e) {
            Log.e("Bitmap","error getting bitmap poster");
            // Log exception
            return null;
        }
    }
}
