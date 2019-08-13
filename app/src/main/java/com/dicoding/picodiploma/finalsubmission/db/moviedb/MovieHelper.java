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

public class MovieHelper {
    // inisialisasi nama table, database helper, SQLiteDatabase dan movie helper
    private static final String DATABASE_TABEL = MOVIE_TABLE_NAME;
    private final DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;

    private SQLiteDatabase database;

    // contruct movie helper dibuat private supaya menjadi singleton
    private MovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    // method ini berfungsi untuk membatu movie helper melakukan inisialisasi
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

    // method ini berfungsi untuk membuka koneksi ke database
    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    // method ini berfungsi untuk menutup koneksi ke database
    public void close() {
        database.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    // method ini nantinya digunakan untuk meng-query data berdasarkan id data
    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABEL, null
                , ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    // method ini nantinya digunakan untuk meng-query data
    public Cursor queryProvider() {
        return database.query(DATABASE_TABEL
                , null
                , null
                , null
                , null
                , null
                , null);
    }

    // method ini berfungsi untuk melakukan insert data ke table
    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABEL, null, values);
    }

    // method ini berfungsi untuk melakukan delete data berdasarkan id data
    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABEL, ID + " = ?", new String[]{id});
    }
}
