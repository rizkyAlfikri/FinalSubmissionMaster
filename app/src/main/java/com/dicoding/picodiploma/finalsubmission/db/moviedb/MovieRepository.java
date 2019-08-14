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
    private static MovieRepository repository;
    private String apiKey = Config.API_KEY;

    // method constract ini menginisialisasi apiService
    private MovieRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    // method ini membantu movie reposity melakukan construct
    public static MovieRepository getInstance() {
        if (repository == null) {
            repository = new MovieRepository(RetrofitService.createService(ApiService.class));
        }
        return repository;

    }

    // method ini berfungsi untuk mengrequest data movie ke web service movieDB
    public MutableLiveData<List<MovieResults>> getMovieDiscovery() {
        MutableLiveData<List<MovieResults>> listMovie = new MutableLiveData<>();
        apiService.getMovieDiscovery(apiKey).enqueue(new Callback<MovieResponse>() {
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
                Log.e("Failure Get Movie Data", t.getMessage());
            }
        });
        return listMovie;

    }

    // method ini berfungsi untuk mengrequest data popular movie ke web service movieDB
    public MutableLiveData<List<MovieResults>> getMoviePopular() {
        MutableLiveData<List<MovieResults>> listMovie = new MutableLiveData<>();
        apiService.getMoviePopular(apiKey).enqueue(new Callback<MovieResponse>() {
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
        return listMovie;

    }

    // method ini berfungsi untuk mengrequest data top rated movie ke web service movieDB
    public MutableLiveData<List<MovieResults>> getMovieTopRated() {
        MutableLiveData<List<MovieResults>> listTopMovie = new MutableLiveData<>();
        apiService.getMovieTopRated(apiKey).enqueue(new Callback<MovieResponse>() {
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
        return listTopMovie;

    }

    // method ini berfungsi untuk mengrequest data upcoming movie ke web service movieDB
    public MutableLiveData<List<MovieResults>> getMovieUpcoming() {
        MutableLiveData<List<MovieResults>> listUpMovie = new MutableLiveData<>();
        apiService.getMovieUpcoming(apiKey).enqueue(new Callback<MovieResponse>() {
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
        return listUpMovie;
    }


    // method ini berfungsi untuk mengrequest data movie genre ke web service movieDB
    public MutableLiveData<List<MovieGenres>> getMovieGenre() {
        MutableLiveData<List<MovieGenres>> listGenreMovie = new MutableLiveData<>();
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
        return listGenreMovie;

    }

    // method ini berfungsi untuk mengrequest data movie detail ke web service movieDB
    public MutableLiveData<MovieDetail> getMovieDetail(int movieId) {
        MutableLiveData<MovieDetail> listDetailMovie = new MutableLiveData<>();
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
        return listDetailMovie;

    }

    // method ini berfungsi untuk mengrequest data movie video trailer ke web service movieDB
    public MutableLiveData<List<MovieTrailer>> getMovieTrailer(int movieId) {
        MutableLiveData<List<MovieTrailer>> listTrailerMovie = new MutableLiveData<>();
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
        return listTrailerMovie;

    }

    // method ini berfungsi untuk mengrequest data movie review ke web service movieDB
    public MutableLiveData<List<MovieReview>> getMovieReview(int movieId) {
        MutableLiveData<List<MovieReview>> listReviewMovie = new MutableLiveData<>();
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
        return listReviewMovie;

    }

    // method ini berfungsi untuk mengrequest data movie hasil pencarian user ke web service movieDB
    public MutableLiveData<List<MovieResults>> getQueryMovie(String queryResult) {
        MutableLiveData<List<MovieResults>> listQueryMovie = new MutableLiveData<>();
        apiService.getQueryMovie(apiKey, queryResult).enqueue(new Callback<MovieResponse>() {
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
                Log.e("Failure Query Movie", t.getMessage());
            }
        });
        return listQueryMovie;

    }

}
