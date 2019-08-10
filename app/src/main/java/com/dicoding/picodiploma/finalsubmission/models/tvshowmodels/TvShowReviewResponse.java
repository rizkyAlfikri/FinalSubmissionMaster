package com.dicoding.picodiploma.finalsubmission.models.tvshowmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowReviewResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<TvShowReview> results;

    @SerializedName("total_results")
    private int totalResults;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setResults(List<TvShowReview> results) {
        this.results = results;
    }

    public List<TvShowReview> getResults() {
        return results;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    @Override
    public String toString() {
        return
                "TvShowReviewResponse{" +
                        "id = '" + id + '\'' +
                        ",page = '" + page + '\'' +
                        ",total_pages = '" + totalPages + '\'' +
                        ",results = '" + results + '\'' +
                        ",total_results = '" + totalResults + '\'' +
                        "}";
    }
}