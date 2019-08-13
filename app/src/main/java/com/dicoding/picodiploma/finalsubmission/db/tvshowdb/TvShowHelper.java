package com.dicoding.picodiploma.finalsubmission.db.tvshowdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.picodiploma.finalsubmission.db.DatabaseHelper;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.TvShowColumns.ID;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.TvShowColumns.TV_TABLE_NAME;

public class TvShowHelper {
    // inisialisasi nama table, database helper, SQLiteDatabase dan tv show helper
    private static final String DATABASE_TABLE = TV_TABLE_NAME;
    private final DatabaseHelper databaseHelper;
    private static TvShowHelper INSTANCE;

    private SQLiteDatabase database;

    // contruct movie helper dibuat private supaya menjadi singleton
    private TvShowHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    // method ini berfungsi untuk membatu movie helper melakukan inisialisasi
    public static TvShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvShowHelper(context);
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
        return database.query(DATABASE_TABLE, null
                , ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    // method ini nantinya digunakan untuk meng-query data
    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , null);
    }

    // method ini berfungsi untuk melakukan insert data ke table
    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    // method ini berfungsi untuk melakukan delete data berdasarkan id data
    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
    }
}
