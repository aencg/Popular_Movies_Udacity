package com.example.popularmovies.utilities;




import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class ScheduleUtilities {
    /*
     * Interval at which to remind the user to drink water. Use TimeUnit for convenience, rather
     * than writing out a bunch of multiplication ourselves and risk making a silly mistake.
     */
    private static final int INTERVAL_MINUTES = 1;
    private static final int INTERVAL_SECONDS = 15;
//    private static final int INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = INTERVAL_SECONDS;

    private static final String JOB_TAG = "movie_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {

        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        /* Create the Job to periodically create reminders to drink water */
        Job constraintJob = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences */
                .setService(MoviesFirebaseJobService.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
                .setTag(JOB_TAG)
                /*
                 * Network constraints on which this Job should run. In this app, we're using the
                 * device charging constraint so that the job only executes if the device is
                 * charging.
                 *
                 * In a normal app, it might be a good idea to include a preference for this,
                 * as different users may have different preferences on when you should be
                 * syncing your application's data.
                 */
//                .setConstraints(Constraint.DEVICE_CHARGING)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        INTERVAL_SECONDS,
                        INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(constraintJob);

        /* The job has been initialized */
        sInitialized = true;
    }


    synchronized public static void cancelService(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        int cancel = dispatcher.cancel(JOB_TAG);

        switch (cancel){
            case         FirebaseJobDispatcher.CANCEL_RESULT_NO_DRIVER_AVAILABLE:
                Log.e("cancel service", "CANCEL_RESULT_NO_DRIVER_AVAILABLE");
                break;

            case         FirebaseJobDispatcher.CANCEL_RESULT_SUCCESS:
                Log.e("cancel service", "CANCEL_RESULT_SUCCESS");
                break;
            case         FirebaseJobDispatcher.CANCEL_RESULT_UNKNOWN_ERROR:
                Log.e("cancel service", "CANCEL_RESULT_UNKNOWN_ERROR");
                break;
        }

    }


}

