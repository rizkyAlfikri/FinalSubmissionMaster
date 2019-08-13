package com.dicoding.picodiploma.finalsubmission.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;


public class MovieFavoriteWidget extends AppWidgetProvider {
    private static final String TOAST_ACTION = "com.dicoding.picodiploma.finalsubmission.TOAST_ACTION";
    private static final String TOAST_UPDATE = "com.dicoding.picodiploma.finalsubmission.TOAST_UPDATE";
    public static final String EXTRA_ITEM = "com.dicoding.picodiploma.finalsubmission.EXTRA_ITEM";
    public static final String EXTRA_MOVIE = "com.dicoding.picodiploma.finalsubmission.EXTRA_MOVIE";

    // method ini berfungsi untuk mengupdate data widget
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // statement ini berfungsi untuk menampikan data, menentukan layout pada widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movie_favorite_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, MovieFavoriteWidget.class);
        toastIntent.setAction(MovieFavoriteWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(
                context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
        updateWidgetFavorite(context);
        Intent widgetUpdateIntent = new Intent(context, StackWidgetService.class);
        context.startService(widgetUpdateIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // method ini berfungsi untuk mengupdate banyak widget besamaan
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    // widget ini akan dijalankan ketika pending intent aktif
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
                MovieResults movieResults = intent.getParcelableExtra(EXTRA_MOVIE);
                Toast.makeText(context, "Touched View" + movieResults.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // method ini berfungsi untuk mengupdate widget ketika data yang ditampilkan ada perubahan
    static void updateWidgetFavorite(Context context) {
        Intent updateIntent = new Intent(context, MovieFavoriteWidget.class);
        updateIntent.setAction(TOAST_UPDATE);
        context.sendBroadcast(updateIntent);
    }
}

