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
    Call<MovieResponse> getMovieDiscovery(@Query("api_key") String apiKey, @Query("page") int page);

    // get data movie populer
    @GET("movie/popular")
    Call<MovieResponse> getMoviePopular(@Query("api_key") String apiKey, @Query("page") int page);

    // get data top rated movie
    @GET("movie/top_rated")
    Call<MovieResponse> getMovieTopRated(@Query("api_key") String apiKey, @Query("page") int page);

    // get data upcoming movie
    @GET("movie/upcoming")
    Call<MovieResponse> getMovieUpcoming(@Query("api_key") String apiKey, @Query("page") int page);

    // get data movie genre
    @GET("genre/movie/list")
    Call<MovieGenreResponse> getMovieGenreApi(@Query("api_key") String apiKey);

    // get data hasil search movie
    @GET("search/movie")
    Call<MovieResponse> getQueryMovie(@Query("api_key") String apuKey, @Query("query") String query,
                                      @Query("page") int page);

    // get data movie detail
    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetailApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    // get data movie video
    @GET("movie/{movie_id}/videos")
    Call<MovieTrailerResponse> getMovieTrailerApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    // get data movie review
    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewResponse> getMovieReviewApi(@Path("movie_id") int movieId, @Query("api_key") String apiKey);



//  ================================================================================================================



    // get data tv show dari web service movieDB
    @GET("discover/tv")
    Call<TvShowResponse> getTvFromApi(@Query("api_key") String apiKey, @Query("page") int page);

    // get data popular tv show
    @GET("tv/popular")
    Call<TvShowResponse> getTvPopular(@Query("api_key") String apiKey, @Query("page") int page);

    // get data top rated tv show
    @GET("tv/top_rated")
    Call<TvShowResponse> getTvTopRated(@Query("api_key") String apiKey, @Query("page") int page);

    // get data on the air tv
    @GET("tv/on_the_air")
    Call<TvShowResponse> getTvOnAir(@Query("api_key") String apiKey, @Query("page") int page);

    // get data tv show genre
    @GET("genre/tv/list")
    Call<TvShowGenreResponse> getTvGenreApi(@Query("api_key") String apiKey);

    // get data hasil search tv show
    @GET("search/tv")
    Call<TvShowResponse> getQueryTv(@Query("api_key") String apiKey, @Query("query") String query,
                                    @Query("page") int page);

    // get data tv show detail
    @GET("tv/{tv_id}")
    Call<TvShowDetail> getTvDetailApi(@Path("tv_id") int tvId, @Query("api_key") String apikey);

    // get data tv show video
    @GET("tv/{tv_id}/videos")
    Call<TvShowTrailerResponse> getTvTrailerApi(@Path("tv_id") int tvId, @Query("api_key") String apiKey);

    // get data tv show review
    @GET("tv/{tv_id}/reviews")
    Call<TvShowReviewResponse> getTvReviewApi(@Path("tv_id") int tvId, @Query("api_key") String apiKey);

}
