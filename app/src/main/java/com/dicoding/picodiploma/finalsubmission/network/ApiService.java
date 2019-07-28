package com.dicoding.picodiploma.finalsubmission.network;

import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenreResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie")
    Call<MovieResponse> getMovieFromApi(@Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<MovieGenreResponse> getMovieGenreApi(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> getQueryMovie(@Query("api_key") String apuKey, @Query("query") String query);

}
