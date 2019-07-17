package com.dicoding.picodiploma.finalsubmission.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieHelper;
import com.dicoding.picodiploma.finalsubmission.fragments.FavoriteMovieFragment;


import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.AUTHORITY;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.CONTENT_URI;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MOVIE_TABLE_NAME;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieHelper movieHelper;

    static {
        //content://com.dicoding.picodiploma.finalsubmission/tableMovie
        sUriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME, MOVIE);

        //content://com.dicoding.picodiploma.finalsubmission/tableMovie/id
        sUriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME + "/#", MOVIE_ID);
    }


    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        movieHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        movieHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI,
                new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        movieHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
