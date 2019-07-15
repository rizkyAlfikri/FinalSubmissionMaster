package com.dicoding.picodiploma.finalsubmission.models.moviemodels;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieGenreResponse {

    @SerializedName("genres")
    @Expose
    private List<MovieGenres> genres;

    public List<MovieGenres> getGenres() {
        return genres;
    }

    public void setGenres(List<MovieGenres> genres) {
        this.genres = genres;
    }
}