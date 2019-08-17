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

    // method ini akan di jalankan ketika pendingIntent.getBroadcast di panggil
    @Override
    public void onReceive(Context context, Intent intent) {
        // method ini berfungsi untuk mendapatkan data date release movie
        getReleaseDateMovie(context);
    }

    private void getReleaseDateMovie(Context context) {
        // inisialisasi dan menentukan format date untuk tanggal bulan dan tahun hari ini
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateToday = sdf.format(date);

        // method ini berfungsi untuk mengrequest data release date movie ke web service
        ApiService apiService = RetrofitService.createService(ApiService.class);
        apiService.getMovieUpcoming(Config.API_KEY, 1).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResults() != null) {
                            tmpMovie = response.body().getResults();
                            // looping ini berfungsi adakah move yang waktu release date nya sama waktu sekarang
                            // jika ada tambahkan movie dengan release date yang sesuai ke listMovie
                            for (MovieResults movieResults : tmpMovie) {
                                String movieDate = movieResults.getReleaseDate();
                                if (movieDate.equals(dateToday)) {
                                    listMovie.add(movieResults);
                                }
                            }
                            // method ini berfungsi untuk memunculkan notifikasi
                            showReminderNotifcation(context);
                        }
                    }
                }
            }

            // error ini akan berjalan ketika aplikasi gagal mengrequest data ke web service movie db
            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }

    // method ini berfungsi untuk memunculkan notifikasi
    private void showReminderNotifcation(Context context) {
        // inisialisasi channel id, nama channel, judul notifikasi, pesan notifikasi, dan intent
        int REQUST_CODE_MOVIE = 111;
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Reminder Movie Date Channel";
        String title = context.getString(R.string.reminder_movie_title);
        String message;
        Intent intent;
        PendingIntent pendingIntent;

        // statement ini berfungsi supaya notifikasi kita dapat berjalan di sistem android
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        // statement ini berfungsi supaya notifikasi yang telah dibuat dapat berjalan di android
        // dengan OS Oreo ke atas
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
        // mengset bunyi notifikasi, disini menggunakan bunyi default notifikasi pada sistem android
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // set bitmap untuk icon besar di notifikasi
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.baseline_notification_important_white_48dp);

        // statement ini berfungsi untuk  menentukan jumlah notifikasi yang akan dimunculkan
        // jumlah notifikasi akan sesuai dengan jumlah data yang ada di listMovie. isi dari setiap
        // notifikasi akan sesuai dengan data movie yang ada di listMovie
        // jika listMovie = 0, notifikasi akan tetap di munculkan tetapi dengan data yang berbeda
        int numMovie;
        if (listMovie.size() > 0) {
            numMovie = listMovie.size();
        } else {
            numMovie = 0;
        }

        if (numMovie == 0) {
            // statement dibawah ini akan berjalan jika data dari listMovie = 0
            intent = new Intent(context, MainActivity.class);
            pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntent(intent)
                    .getPendingIntent(REQUST_CODE_MOVIE, PendingIntent.FLAG_UPDATE_CURRENT);
            message = context.getString(R.string.no_release_today);
            builder.setSmallIcon(R.drawable.ic_notifications_black_24dp) // set small icon untuk notifikasi
                    .setLargeIcon(bmp) // set large icon untuk notifikasi
                    .setContentTitle(title) // set judul untuk notifikasi
                    .setContentText(message) // set pesan untuk notifikasi
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}) // set pola getar ketika
                    // notifikasi muncul
                    .setSound(alarmSound) // set bunyi ketika notifikasi muncul
                    .setContentIntent(pendingIntent) // set pending intent untuk notifikasi
                    .setAutoCancel(true);

            // statement ini berfungsi supaya notifikasi yang telah dibuat dapat berjalan di bawah
            // android dengan OS Oreo ke bawah
            if (notificationManager != null) {
                notificationManager.notify(0, builder.build());
            }
        } else {
            // statement dibawah ini akan berfungsi ketika ada data di dalam listMovie
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

    // method ini berfungsi untuk mengaktifkan fitur notifikasi release date movie pada aplikasi
    public void setReminderMovie(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderMovie.class);

        // method ini berfungsi untuk mengatur waktu kapan notifikasi akan dimunculkan
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8); // waktu yang di set adalah pukul 8 pagi
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // pending intent ini  akan menjalankan fungsi onReceive
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_MOVIE, intent, 0);

        // statement ini berfungsi untuk mengatur jeda waktu notifikasi muncul
        // jeda waktu yang digunakan adalah sehari
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    // method ini berfungsi untuk mengcancel / mematikan fitur notifikasi pada app
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
