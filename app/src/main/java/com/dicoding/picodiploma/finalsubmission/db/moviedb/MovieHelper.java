package com.dicoding.picodiploma.finalsubmission.db.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.picodiploma.finalsubmission.db.DatabaseHelper;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.ID;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.MOVIE_TABLE_NAME;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.TvShowColumns.TV_TABLE_NAME;

public class MovieHelper {
    private static final String DATABASE_TABEL = MOVIE_TABLE_NAME;
    private static final String DATABASE_TABLE_2 = TV_TABLE_NAME;
    private final DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;

    private SQLiteDatabase database;

    private MovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABEL, null
                , ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABEL
                , null
                , null
                , null
                , null
                , null
                , null);
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABEL, null, values);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABEL, ID + " = ?", new String[]{id});
    }
}
