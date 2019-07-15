package com.dicoding.picodiploma.finalsubmission.db.moviedb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.ID;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.MOVIE_TABLE_NAME;

public class MovieHelper {
    private static final String DATABASE_TABEL = MOVIE_TABLE_NAME;
    private final MovieDatabaseHelper movieDatabaseHelper;
    private static MovieHelper INSTANCE;

    private SQLiteDatabase database;

    private MovieHelper(Context context) {
        movieDatabaseHelper = new MovieDatabaseHelper(context);
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
        database = movieDatabaseHelper.getWritableDatabase();
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
