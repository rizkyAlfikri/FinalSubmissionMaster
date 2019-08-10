package com.dicoding.picodiploma.finalsubmission.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.dicoding.picodiploma.finalsubmission";
    public static final String AUTHORITY_2 = "com.dicoding.picodiploma.provider.tv";
    private static final String SCHEME = "content";
    public static final String MOVIE_TABLE_NAME = "tableMovie";
    public static final String TV_TABLE_NAME = "tableTv";

    private DatabaseContract(){}

    public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(MOVIE_TABLE_NAME)
            .build();

    public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TV_TABLE_NAME)
            .build();

    public static final class MovieColumns implements BaseColumns {
        public static final String MOVIE_TABLE_NAME = "tableMovie";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String LANGUAGE = "language";
        public static final String GENRE = "genre";
        public static final String POSTER = "poster";
        public static final String DATE = "date";
        public static final String POPULAR = "popular";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String VOTE_COUNT = "vote_count";
    }

    public static final class TvShowColumns implements BaseColumns {
        public static final String TV_TABLE_NAME = "tableTv";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String LANGUAGE = "language";
        public static final String GENRE = "genre";
        public static final String POSTER = "poster";
        public static final String DATE = "date";
        public static final String POPULAR = "popular";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String VOTE_COUNT = "vote_count";

    }


    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }
}
