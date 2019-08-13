package com.dicoding.picodiploma.finalsubmission.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.picodiploma.finalsubmission.utils.Config;

public class DatabaseHelper extends SQLiteOpenHelper {
    // inisialisasi nama dan versi dari database yang akan digunakan
    private static String DATABSE_NAME = Config.DB_MOVIE_NAME;
    private static final int DATABASE_VERSION = 1;

    // construct untuk kelas database helper, yang isinya untuk menentukan nama dan versi database manakah yang akan digunakan
    public DatabaseHelper(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }

    // statement ini berfungsi untuk inisialisasi table movie dengan bantuan MovieColumn dari DatabaseContract
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
            DatabaseContract.MOVIE_TABLE_NAME,
            DatabaseContract.MovieColumns.ID,
            DatabaseContract.MovieColumns.TITLE,
            DatabaseContract.MovieColumns.OVERVIEW,
            DatabaseContract.MovieColumns.LANGUAGE,
            DatabaseContract.MovieColumns.GENRE,
            DatabaseContract.MovieColumns.POSTER,
            DatabaseContract.MovieColumns.DATE,
            DatabaseContract.MovieColumns.POPULAR,
            DatabaseContract.MovieColumns.VOTE_AVERAGE,
            DatabaseContract.MovieColumns.VOTE_COUNT
    );

    // statement ini berfungsi untuk inisialisasi table tv show dengan bantuan TvShowColumn dari DatabaseContract
    private static final String SQL_CREATE_TABLE_TV = String.format("CREATE TABLE %s"
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
            DatabaseContract.TV_TABLE_NAME,
            DatabaseContract.TvShowColumns.ID,
            DatabaseContract.TvShowColumns.TITLE,
            DatabaseContract.TvShowColumns.OVERVIEW,
            DatabaseContract.TvShowColumns.LANGUAGE,
            DatabaseContract.TvShowColumns.GENRE,
            DatabaseContract.TvShowColumns.POSTER,
            DatabaseContract.TvShowColumns.DATE,
            DatabaseContract.TvShowColumns.POPULAR,
            DatabaseContract.TvShowColumns.VOTE_AVERAGE,
            DatabaseContract.TvShowColumns.VOTE_COUNT
    );


    @Override
    public void onCreate(SQLiteDatabase db) {
        // mengcreate table movie dan table tv show
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // method ini berfungsi untuk melakukan migrasi table ketika table ada perubahan struktur
        // ketika ada perubahan struktur pada tabel, maka tabel yang lama akan di hapus dan dibuat lagi table yang baru
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MOVIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TV_TABLE_NAME);
        onCreate(db);
    }
}
