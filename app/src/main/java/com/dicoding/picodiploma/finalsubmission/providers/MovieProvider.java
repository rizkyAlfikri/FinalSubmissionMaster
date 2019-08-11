package com.dicoding.picodiploma.finalsubmission.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieHelper;
import com.dicoding.picodiploma.finalsubmission.db.tvshowdb.TvShowHelper;
import com.dicoding.picodiploma.finalsubmission.fragments.moviefragments.FavoriteMovieFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.tvshowfragments.FavoriteTvShowFragment;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.AUTHORITY;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_MOVIE;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_TV;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MOVIE_TABLE_NAME;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.TV_TABLE_NAME;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV_SHOW = 3;
    private static final int TV_SHOW_ID = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieHelper movieHelper;
    private TvShowHelper tvShowHelper;

    static {
        //content://com.dicoding.picodiploma.finalsubmission/tableMovie
        sUriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME, MOVIE);

        //content://com.dicoding.picodiploma.finalsubmission/tableMovie/id
        sUriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME + "/#", MOVIE_ID);

        sUriMatcher.addURI(AUTHORITY, TV_TABLE_NAME, TV_SHOW);

        sUriMatcher.addURI(AUTHORITY, TV_TABLE_NAME + "/#", TV_SHOW_ID);
    }


    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        tvShowHelper = TvShowHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                movieHelper.open();
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                movieHelper.open();
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV_SHOW:
                tvShowHelper.open();
                cursor = tvShowHelper.queryProvider();
                break;
            case TV_SHOW_ID:
                tvShowHelper.open();
                cursor = tvShowHelper.queryByIdProvider(uri.getLastPathSegment());
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
        Uri uri1;
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                movieHelper.open();
                added = movieHelper.insertProvider(values);
                uri1 = Uri.parse(CONTENT_URI_MOVIE + "/" + added);
                getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE,
                        new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));
                break;
            case TV_SHOW:
                tvShowHelper.open();
                added = tvShowHelper.insertProvider(values);
                uri1 = Uri.parse(CONTENT_URI_TV + "/" + added);
                getContext().getContentResolver().notifyChange(CONTENT_URI_TV,
                        new FavoriteTvShowFragment.DataObserver(new Handler(), getContext()));
                break;
            default:
                throw new SQLException("failed to insert row into " + uri);
        }
        return uri1;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                movieHelper.open();
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE,
                        new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));
                break;
            case TV_SHOW_ID:
                tvShowHelper.open();
                deleted = tvShowHelper.deleteProvider(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_URI_TV,
                        new FavoriteTvShowFragment.DataObserver(new Handler(), getContext()));
                break;
            default:
                deleted = 0;
                break;
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
