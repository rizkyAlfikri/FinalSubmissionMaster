package com.dicoding.picodiploma.finalsubmission.utils;

import android.content.ContentValues;

import com.dicoding.picodiploma.finalsubmission.db.DatabaseContract;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;

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

// kelas ini berfungsi untuk mengkonversi movie data dan tv show data menjadi content value
// dikarenakan data yang tersimpan di database harus ber bentuk content value
public class ContentValueHelper {

    // method ini berfungsi untuk mengubah data movie menjadi content value
    public static ContentValues getContentValueMovie(MovieResults movieResults) {
        ContentValues values = new ContentValues();
        values.put(ID, movieResults.getId());
        values.put(TITLE, movieResults.getTitle());
        values.put(OVERVIEW, movieResults.getOverview());
        values.put(LANGUAGE, movieResults.getOriginalLanguage());
        values.put(POSTER, movieResults.getPosterPath());
        values.put(DATE, movieResults.getReleaseDate());
        values.put(POPULAR, movieResults.getPopularity());
        values.put(VOTE_AVERAGE, movieResults.getVoteAverage());
        values.put(VOTE_COUNT, movieResults.getVoteCount());
        values.put(GENRE, movieResults.getGenre());
        return values;
    }

    // method ini berfungsi untuk mengubah data tv show menjadi content value
    public static ContentValues getContentValueTv(TvShowResults tvShowResults) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TvShowColumns.ID, tvShowResults.getId());
        values.put(DatabaseContract.TvShowColumns.TITLE, tvShowResults.getName());
        values.put(DatabaseContract.TvShowColumns.OVERVIEW, tvShowResults.getOverview());
        values.put(DatabaseContract.TvShowColumns.LANGUAGE, tvShowResults.getOriginalLanguage());
        values.put(DatabaseContract.TvShowColumns.POSTER, tvShowResults.getPosterPath());
        values.put(DatabaseContract.TvShowColumns.DATE, tvShowResults.getFirstAirDate());
        values.put(DatabaseContract.TvShowColumns.POPULAR, tvShowResults.getPopularity());
        values.put(DatabaseContract.TvShowColumns.VOTE_AVERAGE, tvShowResults.getVoteAverage());
        values.put(DatabaseContract.TvShowColumns.VOTE_COUNT, tvShowResults.getVoteCount());
        values.put(DatabaseContract.TvShowColumns.GENRE, tvShowResults.getGenre());
        return values;
    }
}
