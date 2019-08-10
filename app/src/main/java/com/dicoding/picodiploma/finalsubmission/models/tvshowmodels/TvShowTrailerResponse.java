package com.dicoding.picodiploma.finalsubmission.models.tvshowmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowTrailerResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<TvShowTrailer> results;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setResults(List<TvShowTrailer> results) {
        this.results = results;
    }

    public List<TvShowTrailer> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return
                "TvShowTrailerResponse{" +
                        "id = '" + id + '\'' +
                        ",results = '" + results + '\'' +
                        "}";
    }
}