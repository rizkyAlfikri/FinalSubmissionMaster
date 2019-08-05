package com.dicoding.picodiploma.finalsubmission.utils;

import android.content.ContentValues;

import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;

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

public class ContentValueHelper {
    public static ContentValues getContentValue(MovieResults movieResults) {
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
}
