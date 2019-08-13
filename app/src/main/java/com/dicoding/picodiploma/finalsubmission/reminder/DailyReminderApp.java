package com.dicoding.picodiploma.finalsubmission.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.dicoding.picodiploma.finalsubmission.MainActivity;
import com.dicoding.picodiploma.finalsubmission.R;

import java.util.Calendar;

public class DailyReminderApp extends BroadcastReceiver {
    // variabel ini digunakan untuk channel id notification
    private final int ID_APP = 100;

    public DailyReminderApp() {
    }

    // method ini akan perjalan ketika pending intent di jalankan
    @Override
    public void onReceive(Context context, Intent intent) {
        showReminderNotification(context);
    }

    // method ini berfungsi untuk membuat notifikasi ketika method onReceive berjalan
    private void showReminderNotification(Context context) {
        // inisialisasi channel id, channel name, judul notifikasi, pesan notifikasi, intent, dan id request
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Reminder Channel App";
        String title = context.getString(R.string.reminder_app_title);
        String message = context.getString(R.string.reminder_app_message);
        Intent intent = new Intent(context, MainActivity.class);
        int REQUST_CODE_APP = 110;

        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(REQUST_CODE_APP, PendingIntent.FLAG_UPDATE_CURRENT);

        // statement ini berfungsi untuk membuat notifikasi kita dapat berkerja dengan sistem android
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mengset bunyi notifikasi, disini menggunakan bunyi default notifikasi pada sistem android
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // method ini berfungsi untuk mengatur isi notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp) // set icon kecil notifikasi
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), // set icon besar notifikasi
                        R.drawable.baseline_notification_important_white_48dp))
                .setContentTitle(title) // set judul notifikasi
                .setContentText(message) // set pesan notifikasi
                .setContentIntent(pendingIntent) // set aksi notifikasi
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}) // set pola getar saat notifikasi
                .setSound(alarmSound); // set bunyi notifikasi yang akan digunakan

        // statement ini berfungsi supaya notifikasi yang telah dibuat dapat berjalan di android dengan OS Oreo ke atas
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

        // statement ini berfungsi supaya notifikasi yang telah dibuat dapat berjalan di bawah android dengan OS Oreo ke bawah
        Notification notification = builder.build();
        if (notificationManager != null) {
            notificationManager.notify(ID_APP, notification);
        }
    }

    // method ini berfungsi untuk mengatur waktu kapan notifikasi akan dikeluarkan, dan menyalakan fitur notifikasi yang telah dibuat
    public void setReminderApp(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderApp.class);

        // setting waktu, kapan notifikasi akan di keluarkan
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7); // waktu yang di set adalah pukul 7 pagi
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // pending intent ini  akan menjalankan fungsi onReceive
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_APP, intent, 0);
        if (alarmManager != null) {
            // statement ini berfungsi untuk mengatur jeda waktu notifikasi muncul
            // jeda waktu yang digunakan adalah sehari
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    // method ini berfungsi untuk mematikan fitur notifikasi
    public void cancelReminderApp(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderApp.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_APP, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, context.getString(R.string.cancel_repeating_app), Toast.LENGTH_SHORT).show();
    }
}
