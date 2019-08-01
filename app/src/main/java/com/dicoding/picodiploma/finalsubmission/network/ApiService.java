package com.dicoding.picodiploma.finalsubmission.network;

import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieDetail;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenreResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReviewResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieTrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("discover/movie")
    Call<MovieResponse> getMovieFromApi(@Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<MovieGenreResponse> getMovieGenreApi(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> getQueryMovie(@Query("api_key") String apuKey, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetailApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<MovieTrailerResponse> getMovieTrailerApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewResponse> getMovieReviewApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
