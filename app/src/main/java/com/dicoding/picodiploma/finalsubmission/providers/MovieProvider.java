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
    // inisialisasi ID movie dan tvshow yang nantinya akan digunakan sebagai action crud pada masing masing table
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV_SHOW = 3;
    private static final int TV_SHOW_ID = 4;

    // UriMatcher berguna untuk membandingkan uri, yang keluarannya berupa nilai integer
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // inisialisasi movie dan tvshow helper. yang didalamnnya ada method crud data ke database
    private MovieHelper movieHelper;
    private TvShowHelper tvShowHelper;

    // statement dibawah ini merupakan perbandingan uri, dimana uri tersebut dikirim dari activity / fragment
    // outputnya berupa nomor ID dari variabel yang telah di inisialisasi di atas
    // pada akhirnya nilai integer digunakan untuk menentukan table manakah yang akan di proses
    static {
        // pengalamatan untuk mengakses table movie secara keseluruhan
        //content://com.dicoding.picodiploma.finalsubmission/tableMovie
        sUriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME, MOVIE);

        // pengalamatan untuk mengakses table movie berdasarkan id movie
        //content://com.dicoding.picodiploma.finalsubmission/tableMovie/id
        sUriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME + "/#", MOVIE_ID);

        // pengalamatan untuk mengakses table tv show secara keseluruhan
        //content://com.dicoding.picodiploma.finalsubmission/tableTvShow
        sUriMatcher.addURI(AUTHORITY, TV_TABLE_NAME, TV_SHOW);

        // pengalamatan untuk mengakses table tv show berdasarkan id tv show
        //content://com.dicoding.picodiploma.finalsubmission/tableTvShow/id
        sUriMatcher.addURI(AUTHORITY, TV_TABLE_NAME + "/#", TV_SHOW_ID);
    }


    @Override
    public boolean onCreate() {
        // instansiasi movieHelper dan tvShowHelper
        movieHelper = MovieHelper.getInstance(getContext());
        tvShowHelper = TvShowHelper.getInstance(getContext());
        return true;
    }


    // method ini berguna untuk mengquery table movie dan tv show dari database
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                // query table movie
                movieHelper.open();
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                // query table movie berdasarkan id movie
                movieHelper.open();
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV_SHOW:
                //query table tv show
                tvShowHelper.open();
                cursor = tvShowHelper.queryProvider();
                break;
            case TV_SHOW_ID:
                // query table tv show berdasarkan id tv show
                tvShowHelper.open();
                cursor = tvShowHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                // jika tidak ada uri maka cursor akan null dan tidak akan mengakses ke table manapun
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    // method ini berfungsi untuk insert data ke table movie atau tv show
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri uri1;
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                // insert data ke table movie
                movieHelper.open();
                added = movieHelper.insertProvider(values);
                uri1 = Uri.parse(CONTENT_URI_MOVIE + "/" + added);
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE,
                            new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));
                }
                break;
            case TV_SHOW:
                // insert data ke table tv show
                tvShowHelper.open();
                added = tvShowHelper.insertProvider(values);
                uri1 = Uri.parse(CONTENT_URI_TV + "/" + added);
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(CONTENT_URI_TV,
                            new FavoriteTvShowFragment.DataObserver(new Handler(), getContext()));
                }
                break;
            default:
                // jika uri tidak sesuai maka akan keluar SQLException
                throw new SQLException("failed to insert row into " + uri);
        }
        return uri1;
    }

    // method ini berfungsi untuk menghapus data dari table movie atau table tv show
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                // delete data dari table movie berdasarkan id movie
                movieHelper.open();
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE,
                            new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));
                }
                break;
            case TV_SHOW_ID:
                // delete data dari table tv show berdasarkan id tv show
                tvShowHelper.open();
                deleted = tvShowHelper.deleteProvider(uri.getLastPathSegment());
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(CONTENT_URI_TV,
                            new FavoriteTvShowFragment.DataObserver(new Handler(), getContext()));
                }
                break;
            default:
                // jika uri tidak ada yang sesuai, maka tidak ada data yang di delete
                deleted = 0;
                break;
        }
        return deleted;
    }

    // method ini berfungsi untuk mengupdate data, tapi tidak digunakan
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
