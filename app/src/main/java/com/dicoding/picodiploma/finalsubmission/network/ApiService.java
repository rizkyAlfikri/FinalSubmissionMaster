package com.dicoding.picodiploma.finalsubmission.network;

import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieDetail;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenreResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReviewResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieTrailerResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowDetail;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenreResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowReviewResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowTrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // get data movie dari web service movieDB
    @GET("discover/movie")
    Call<MovieResponse> getMovieFromApi(@Query("api_key") String apiKey);

    // get data movie genre dari web service movieDB
    @GET("genre/movie/list")
    Call<MovieGenreResponse> getMovieGenreApi(@Query("api_key") String apiKey);

    // get data hasil pencarian movie dari web service movieDB
    @GET("search/movie")
    Call<MovieResponse> getQueryMovie(@Query("api_key") String apuKey, @Query("query") String query);

    // get data movie detail dari web service movieDB
    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetailApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    // get data movie video dari web service movieDB
    @GET("movie/{movie_id}/videos")
    Call<MovieTrailerResponse> getMovieTrailerApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    // get data movie review dari web service movieDB
    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewResponse> getMovieReviewApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    // get data tv show dari web service movieDB
    @GET("discover/tv")
    Call<TvShowResponse> getTvFromApi(@Query("api_key") String apiKey);

    // get data tv show genre dari web service movieDB
    @GET("genre/tv/list")
    Call<TvShowGenreResponse> getTvGenreApi(@Query("api_key") String apiKey);

    // get data hasil pencarian tv show dari web service movieDB
    @GET("search/tv")
    Call<TvShowResponse> getQueryTv(@Query("api_key") String apiKey, @Query("query") String query);

    // get data tv show detail dari web service movieDB
    @GET("tv/{tv_id}")
    Call<TvShowDetail> getTvDetailApi(@Path("tv_id") int tvId, @Query("api_key") String apikey);

    // get data tv show video dari web service movieDB
    @GET("tv/{tv_id}/videos")
    Call<TvShowTrailerResponse> getTvTrailerApi(@Path("tv_id") int tvId, @Query("api_key") String apiKey);

    // get data tv show review dari web service movieDB
    @GET("tv/{tv_id}/reviews")
    Call<TvShowReviewResponse> getTvReviewApi(@Path("tv_id") int tvId, @Query("api_key") String apiKey);

}
