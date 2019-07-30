package com.dicoding.picodiploma.finalsubmission.utils;

import android.database.Cursor;

import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;

import java.util.ArrayList;

import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.DATE;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.GENRE;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.ID;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.LANGUAGE;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.OVERVIEW;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.POPULAR;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.POSTER;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.TITLE;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.VOTE_AVERAGE;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.MovieColumns.VOTE_COUNT;

public class MappingHelper {
    public static ArrayList<MovieResults> mapCursorToArrayList(Cursor movieCursor) {
        ArrayList<MovieResults> movieList = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            String overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(OVERVIEW));
            String originalLanguage = movieCursor.getString(movieCursor.getColumnIndexOrThrow(LANGUAGE));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TITLE));
            String posterPath = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POSTER));
            String releaseDate = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DATE));
            double voteAverage = movieCursor.getDouble(movieCursor.getColumnIndexOrThrow(VOTE_AVERAGE));
            double popularity = movieCursor.getDouble(movieCursor.getColumnIndexOrThrow(POPULAR));
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(ID));
            int voteCount = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(VOTE_COUNT));
            String genre = movieCursor.getString(movieCursor.getColumnIndexOrThrow(GENRE));
            movieList.add(new MovieResults(overview, originalLanguage, title, posterPath,
                    releaseDate, voteAverage, popularity, id, voteCount, genre));
        }
        return movieList;
    }
}
