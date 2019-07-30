package com.dicoding.picodiploma.finalsubmission.reminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.dicoding.picodiploma.finalsubmission.MainActivity;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.activity.DetailMovieActivity;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.network.ApiService;
import com.dicoding.picodiploma.finalsubmission.network.RetrofitService;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyReminderMovie extends BroadcastReceiver {
    private final int ID_MOVIE = 101;
    private ArrayList<MovieResults> listMovie = new ArrayList<>();
    private List<MovieResults> tmpMovie = new ArrayList<>();

    public DailyReminderMovie() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getReleaseDateMovie(context);
    }

    private void getReleaseDateMovie(Context context) {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateToday = sdf.format(date);

        ApiService apiService = RetrofitService.createService(ApiService.class);

        apiService.getMovieFromApi(Config.API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResults() != null) {
                            tmpMovie = response.body().getResults();
                            for (MovieResults movieResults : tmpMovie) {
                                String movieDate = movieResults.getReleaseDate();
                                if (movieDate.equals(dateToday)) {
                                    listMovie.add(movieResults);
                                }
                            }
                            showReminderNotifcation(context);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }

    private void showReminderNotifcation(Context context) {
        int REQUST_CODE_MOVIE = 111;
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Reminder Movie Date Channel";
        String title = context.getString(R.string.reminder_movie_title);
        String message;
        Intent intent;
        PendingIntent pendingIntent;

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.baseline_notification_important_white_48dp);

        int numMovie;
        if (listMovie.size() > 0) {
            numMovie = listMovie.size();
        } else {
            numMovie = 0;
        }

        if (numMovie == 0) {
            intent = new Intent(context, MainActivity.class);
            pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntent(intent)
                    .getPendingIntent(REQUST_CODE_MOVIE, PendingIntent.FLAG_UPDATE_CURRENT);
            message = "There is no movie in release today";
            builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setLargeIcon(bmp)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(alarmSound)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            if (notificationManager != null) {
                notificationManager.notify(0, builder.build());
            }
        } else {
            intent = new Intent(context, DetailMovieActivity.class);
            for (int i = 0; i < listMovie.size(); i++) {
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, listMovie.get(i));
                pendingIntent = TaskStackBuilder.create(context)
                        .addNextIntent(intent)
                        .getPendingIntent(i, PendingIntent.FLAG_UPDATE_CURRENT);
                message = listMovie.get(i).getTitle() + " " + context.getString(R.string.release_today);
                builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setLargeIcon(bmp)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(alarmSound)
                        .setAutoCancel(true);
                if (notificationManager != null) {
                    notificationManager.notify(i, builder.build());
                }
            }
        }
    }


    public void setReminderMovie(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderMovie.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_MOVIE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, context.getString(R.string.repeating_movie), Toast.LENGTH_SHORT).show();
    }

    public void cancelReminderMovie(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderMovie.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_MOVIE, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, context.getString(R.string.cancel_repeating_movie), Toast.LENGTH_SHORT).show();
    }
}
