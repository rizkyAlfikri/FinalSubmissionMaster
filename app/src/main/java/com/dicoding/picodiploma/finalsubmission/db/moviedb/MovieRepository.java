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
    private ApiService apiService;
    private static MovieRepository repository;

    private MovieRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public static MovieRepository getInstance() {
        if (repository == null) {
            repository = new MovieRepository(RetrofitService.createService(ApiService.class));
        }
        return repository;
    }

    public MutableLiveData<List<MovieResults>> getMovieFromRetrofit() {
        MutableLiveData<List<MovieResults>> listMovie = new MutableLiveData<>();
        apiService.getMovieFromApi(Config.API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listMovie.setValue(response.body().getResults());
                    }
                }
                Log.d("Failure Get Movie 1", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("Failure Get Movie 2", t.getMessage());
            }
        });
        return listMovie;
    }

    public MutableLiveData<List<MovieGenres>> getMovieGenre() {
        MutableLiveData<List<MovieGenres>> listGenreMovie = new MutableLiveData<>();
        apiService.getMovieGenreApi(Config.API_KEY).enqueue(new Callback<MovieGenreResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieGenreResponse> call,
                                   @NonNull Response<MovieGenreResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listGenreMovie.setValue(response.body().getGenres());
                    }
                }
                Log.d("Failure Genre Movie 1", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<MovieGenreResponse> call, @NonNull Throwable t) {
                Log.e("Failure Genre Movie 2", t.getMessage());
            }
        });
        return listGenreMovie;
    }

    public MutableLiveData<MovieDetail> getMovieDetail(int movieId, String apiKey) {
        MutableLiveData<MovieDetail> listDetailMovie = new MutableLiveData<>();
        apiService.getMovieDetailApi(movieId, apiKey).enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listDetailMovie.setValue(response.body());
                    }
                }
                Log.d("Failure Detail Movie 1", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
                Log.e("Failure Detail Movie 2", t.getMessage());
            }
        });
        return listDetailMovie;
    }

    public MutableLiveData<List<MovieTrailer>> getMovieTrailer(int movieId, String apiKey) {
        MutableLiveData<List<MovieTrailer>> listTrailerMovie = new MutableLiveData<>();
        apiService.getMovieTrailerApi(movieId, apiKey).enqueue(new Callback<MovieTrailerResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieTrailerResponse> call, @NonNull Response<MovieTrailerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTrailerMovie.setValue(response.body().getResults());
                    }
                }
                Log.d("Failure Trailer Movie 1", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<MovieTrailerResponse> call, @NonNull Throwable t) {
                Log.e("Failure Trailer Movie 2", t.getMessage());
            }
        });
        return listTrailerMovie;
    }

    public MutableLiveData<List<MovieReview>> getMovieReview(int movieId, String apiKey) {
        MutableLiveData<List<MovieReview>> listReviewMovie = new MutableLiveData<>();
        apiService.getMovieReviewApi(movieId, apiKey).enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieReviewResponse> call, @NonNull Response<MovieReviewResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listReviewMovie.setValue(response.body().getResults());
                    }
                }
                Log.d("Failure Review Movie 1", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<MovieReviewResponse> call, @NonNull Throwable t) {
                Log.e("Failure Review Movie 2", t.getMessage());
            }
        });
        return listReviewMovie;
    }

    public MutableLiveData<List<MovieResults>> getQueryMovie(String apiKey, String queryResult) {
        MutableLiveData<List<MovieResults>> listQueryMovie = new MutableLiveData<>();
        apiService.getQueryMovie(apiKey, queryResult).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listQueryMovie.setValue(response.body().getResults());
                    }
                }
                Log.d("Failure Query Movie", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("Failure Query Movie", t.getMessage());
            }
        });
        return listQueryMovie;
    }
}
