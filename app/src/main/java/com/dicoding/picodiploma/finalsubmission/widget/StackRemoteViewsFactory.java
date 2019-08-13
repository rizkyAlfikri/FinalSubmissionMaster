package com.dicoding.picodiploma.finalsubmission.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.concurrent.ExecutionException;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_MOVIE;
import static com.dicoding.picodiploma.finalsubmission.widget.MovieFavoriteWidget.EXTRA_ITEM;
import static com.dicoding.picodiploma.finalsubmission.widget.MovieFavoriteWidget.EXTRA_MOVIE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private Cursor cursor;

    StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        int mAppWidgetid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    @Override
    public void onCreate() {
        // ketika widget dibuat pertmakali maka data defaultnya yaitu data yang di query / di ambil
        // di database
        cursor = context.getContentResolver().query(CONTENT_URI_MOVIE
                , null
                , null
                , null
                , null);

    }

    @Override
    public void onDataSetChanged() {
        // method ini berguna ketika ada perubahan data di database
        if (cursor != null) {
            cursor.close();
        }

        final long identifyToken = Binder.clearCallingIdentity();

        cursor = context.getContentResolver().query(CONTENT_URI_MOVIE
                , null
                , null
                , null
                , null);

        Binder.restoreCallingIdentity(identifyToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        // statement ini berfungsi untuk menampilkan jumlah data yang sesuai dengan jumlah data yang ada di database
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // statement dibawah ini berfungsi untuk mengatur data yang akan di tampilkan di widget
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.movie_widget_item);
        MovieResults movieResults = getMoviePosition(position);
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + movieResults.getPosterPath();
        CharSequence title = movieResults.getTitle();
        CharSequence rate = String.valueOf(movieResults.getVoteAverage());
        try {
            Bitmap bmp = Glide.with(context)
                    .asBitmap()
                    .load(urlPhoto)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.img_photo, bmp);
            rv.setTextViewText(R.id.txt_title, title);
            rv.setTextViewText(R.id.txt_rate, rate);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ITEM, position);
        bundle.putParcelable(EXTRA_MOVIE, movieResults);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(bundle);

        rv.setOnClickFillInIntent(R.id.img_photo, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private MovieResults getMoviePosition(int position) {
        if (cursor.moveToPosition(position)) {
            return new MovieResults(cursor);
        } else {
            throw new IllegalStateException("The position is invalid");
        }
    }
}
