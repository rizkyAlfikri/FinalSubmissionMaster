package com.dicoding.picodiploma.finalsubmission.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dicoding.picodiploma.finalsubmission.db.tvshowdb.TvShowHelper;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.AUTHORITY;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_TV;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.TV_TABLE_NAME;

public class TvShowProvider extends ContentProvider {
    private static final int TV = 1;
    private static final int TV_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private TvShowHelper tvShowHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TV_TABLE_NAME, TV);

        sUriMatcher.addURI(AUTHORITY, TV_TABLE_NAME + "/#", TV_ID);
    }

    @Override
    public boolean onCreate() {
        tvShowHelper = TvShowHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        tvShowHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case TV:
                cursor = tvShowHelper.queryProvider();
                break;
            case TV_ID:
                cursor = tvShowHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        tvShowHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case TV:
                added = tvShowHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI_TV, new FavoriteTvShowFragment().DataObserver(new Handler(), getContext()));
        return Uri.parse(CONTENT_URI_TV + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        tvShowHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case TV_ID:
                deleted = tvShowHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI_TV, new FavoriteTvShowFragment().DataObserver(new Handler(), getContext()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
