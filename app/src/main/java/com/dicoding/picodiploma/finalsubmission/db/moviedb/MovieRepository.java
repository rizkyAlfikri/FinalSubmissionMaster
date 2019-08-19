package com.dicoding.picodiploma.finalsubmission.db.moviedb;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieDetail;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenreResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReview;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReviewResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieTrailer;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieTrailerResponse;
import com.dicoding.picodiploma.finalsubmission.network.ApiService;
import com.dicoding.picodiploma.finalsubmission.network.RetrofitService;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    // inisialisasi ApiService, dan membuat construct movie repository ke dalam
    // visibility private karena akan menggunakan pola singleton
    private ApiService apiService;
    private String apiKey = Config.API_KEY;
    private MutableLiveData<List<MovieResults>> listMovie;
    private MutableLiveData<List<MovieResults>> listTopMovie;
    private MutableLiveData<List<MovieResults>> listUpMovie;
    private MutableLiveData<List<MovieGenres>> listGenreMovie;
    private MutableLiveData<MovieDetail> listDetailMovie;
    private MutableLiveData<List<MovieTrailer>> listTrailerMovie;
    private MutableLiveData<List<MovieReview>> listReviewMovie;
    private MutableLiveData<List<MovieResults>> listQueryMovie;


    // method constract ini menginisialisasi apiService
    public MovieRepository() {
        this.apiService = RetrofitService.createService(ApiService.class);
    }

    // method ini berfungsi untuk mengrequest data popular movie ke web service movieDB
    public MutableLiveData<List<MovieResults>> getMoviePopular(int page) {
        if (listMovie == null) {
            listMovie = new MutableLiveData<>();
            apiService.getMoviePopular(apiKey, page).enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listMovie.setValue(response.body().getResults());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                    Log.e("Failure GetPopularMovie", t.getMessage());
                }
            });
        }
        return listMovie;

    }

    // method ini berfungsi untuk mengrequest data top rated movie ke web service movieDB
    public MutableLiveData<List<MovieResults>> getMovieTopRated(int page) {
        if (listTopMovie == null) {
            listTopMovie = new MutableLiveData<>();
            apiService.getMovieTopRated(apiKey, page).enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listTopMovie.setValue(response.body().getResults());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                    Log.e("Failure Get Top Movie", t.getMessage());
                }
            });
        }
        return listTopMovie;

    }

    // method ini berfungsi untuk mengrequest data upcoming movie ke web service movieDB
    public MutableLiveData<List<MovieResults>> getMovieUpcoming(int page) {
        if (listUpMovie == null) {
            listUpMovie = new MutableLiveData<>();
            apiService.getMovieUpcoming(apiKey, page).enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listUpMovie.setValue(response.body().getResults());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                    Log.e("Failure Get Up Movie", t.getMessage());
                }
            });
        }
        return listUpMovie;
    }


    // method ini berfungsi untuk mengrequest data movie genre ke web service movieDB
    public MutableLiveData<List<MovieGenres>> getMovieGenre() {
        if (listGenreMovie == null) {
            listGenreMovie = new MutableLiveData<>();
            apiService.getMovieGenreApi(apiKey).enqueue(new Callback<MovieGenreResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieGenreResponse> call,
                                       @NonNull Response<MovieGenreResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listGenreMovie.setValue(response.body().getGenres());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieGenreResponse> call, @NonNull Throwable t) {
                    Log.e("Failure Genre Movie", t.getMessage());
                }
            });
        }
        return listGenreMovie;

    }

    // method ini berfungsi untuk mengrequest data movie detail ke web service movieDB
    public MutableLiveData<MovieDetail> getMovieDetail(int movieId) {
        if (listDetailMovie == null) {
            listDetailMovie = new MutableLiveData<>();
            apiService.getMovieDetailApi(movieId, apiKey).enqueue(new Callback<MovieDetail>() {
                @Override
                public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listDetailMovie.setValue(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
                    Log.e("Failure Detail Movie 2", t.getMessage());
                }
            });
        }
        return listDetailMovie;

    }

    // method ini berfungsi untuk mengrequest data movie video trailer ke web service movieDB
    public MutableLiveData<List<MovieTrailer>> getMovieTrailer(int movieId) {
        if (listTrailerMovie == null) {
            listTrailerMovie = new MutableLiveData<>();
            apiService.getMovieTrailerApi(movieId, apiKey).enqueue(new Callback<MovieTrailerResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieTrailerResponse> call, @NonNull Response<MovieTrailerResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listTrailerMovie.setValue(response.body().getResults());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieTrailerResponse> call, @NonNull Throwable t) {
                    Log.e("Failure Trailer Movie 2", t.getMessage());
                }
            });
        }
        return listTrailerMovie;

    }

    // method ini berfungsi untuk mengrequest data movie review ke web service movieDB
    public MutableLiveData<List<MovieReview>> getMovieReview(int movieId) {
        if (listReviewMovie == null) {
            listReviewMovie = new MutableLiveData<>();
            apiService.getMovieReviewApi(movieId, apiKey).enqueue(new Callback<MovieReviewResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieReviewResponse> call, @NonNull Response<MovieReviewResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listReviewMovie.setValue(response.body().getResults());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieReviewResponse> call, @NonNull Throwable t) {
                    Log.e("Failure Review Movie 2", t.getMessage());
                }
            });
        }
        return listReviewMovie;

    }

    // method ini berfungsi untuk mengrequest data movie hasil pencarian user ke web service movieDB
    public MutableLiveData<List<MovieResults>> getQueryMovie(String queryResult, int page) {
        if (listQueryMovie == null) {
            listQueryMovie = new MutableLiveData<>();
            Call<MovieResponse> call = apiService.getQueryMovie(apiKey, queryResult, page);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            listQueryMovie.setValue(response.body().getResults());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {

                }
            });
        }
        return listQueryMovie;
    }
}

