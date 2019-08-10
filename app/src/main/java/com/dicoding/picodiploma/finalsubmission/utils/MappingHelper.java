package com.dicoding.picodiploma.finalsubmission.utils;

import android.database.Cursor;

import com.dicoding.picodiploma.finalsubmission.db.DatabaseContract;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;

import java.util.ArrayList;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.DATE;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.GENRE;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.ID;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.LANGUAGE;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.POPULAR;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.POSTER;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.TITLE;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.VOTE_AVERAGE;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.MovieColumns.VOTE_COUNT;

public class MappingHelper {

    public static ArrayList<MovieResults> mapCursorToArrayListMovie(Cursor movieCursor) {
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

    public static ArrayList<TvShowResults> mapCursorToArrayListTv(Cursor tvCursor) {
        ArrayList<TvShowResults> tvList = new ArrayList<>();

        while (tvCursor.moveToNext()) {
            String firstAirDate = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.DATE));
            String overview = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.OVERVIEW));
            String originalLanguage = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.LANGUAGE));
            double popularity = tvCursor.getDouble(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.POPULAR));
            double voteAverage = tvCursor.getDouble(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.VOTE_AVERAGE));
            String name = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.TITLE));
            int id = tvCursor.getInt(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.ID));
            int voteCount = tvCursor.getInt(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.VOTE_COUNT));
            String posterPath = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.POSTER));
            String genre = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.GENRE));

            tvList.add(new TvShowResults(firstAirDate, overview, originalLanguage, popularity,
                    voteAverage, name, id, voteCount, posterPath, genre));
        }
        return tvList;
    }
}
