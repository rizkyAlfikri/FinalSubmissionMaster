package com.dicoding.picodiploma.finalsubmission.db.moviedb;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDatabaseContract {
    public static final String AUTHORITY = "com.dicoding.picodiploma.finalsubmission";
    public static final String MOVIE_TABLE_NAME = "tableMovie";
    private static final String SCHEME = "content";

    private MovieDatabaseContract(){}

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(MOVIE_TABLE_NAME)
            .build();

    static final class MovieColumns implements BaseColumns {
        static final String MOVIE_TABLE_NAME = "tableMovie";
        static final String ID = "id";
        static final String TITLE = "title";
        static final String OVERVIEW = "overview";
        static final String LANGUAGE = "language";
        static final String GENRE = "genre";
        static final String POSTER = "poster";
        static final String DATE = "date";
        static final String POPULAR = "popular";
        static final String VOTE_AVERAGE = "vote_average";
        static final String VOTE_COUNT = "vote_count";
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }
}
