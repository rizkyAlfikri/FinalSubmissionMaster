package com.dicoding.picodiploma.finalsubmission.db.moviedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.picodiploma.finalsubmission.utils.Config;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    private static String DATABSE_NAME = Config.DB_MOVIE_NAME;
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            MovieDatabaseContract.MOVIE_TABLE_NAME,
            MovieDatabaseContract.MovieColumns.ID,
            MovieDatabaseContract.MovieColumns.TITLE,
            MovieDatabaseContract.MovieColumns.OVERVIEW,
            MovieDatabaseContract.MovieColumns.LANGUAGE,
            MovieDatabaseContract.MovieColumns.GENRE,
            MovieDatabaseContract.MovieColumns.POSTER,
            MovieDatabaseContract.MovieColumns.DATE,
            MovieDatabaseContract.MovieColumns.POPULAR,
            MovieDatabaseContract.MovieColumns.VOTE_AVERAGE,
            MovieDatabaseContract.MovieColumns.VOTE_COUNT
    );

    MovieDatabaseHelper(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDatabaseContract.MOVIE_TABLE_NAME);
        onCreate(db);
    }
}
