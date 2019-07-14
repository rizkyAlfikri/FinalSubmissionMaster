package com.dicoding.picodiploma.finalsubmission.db.moviedb;

import androidx.lifecycle.MutableLiveData;

import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenreResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.network.ApiService;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private ApiService apiService;
    private static MovieRepository repository;

    private MovieRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public static MovieRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MovieRepository(retrofit.create(ApiService.class));

        }
        return repository;
    }

    public MutableLiveData<List<MovieResults>> getMovieFromRetrofit() {
        MutableLiveData<List<MovieResults>> listMovie = new MutableLiveData<>();
        apiService.getMovieFromApi(Config.API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                listMovie.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
        return listMovie;
    }

    public MutableLiveData<List<MovieGenres>> getMovieGenreRetrofit() {
        MutableLiveData<List<MovieGenres>> listGenreMovie = new MutableLiveData<>();
        apiService.getMovieGenreApi(Config.API_KEY).enqueue(new Callback<MovieGenreResponse>() {
            @Override
            public void onResponse(Call<MovieGenreResponse> call, Response<MovieGenreResponse> response) {
                listGenreMovie.postValue(response.body().getGenres());
            }

            @Override
            public void onFailure(Call<MovieGenreResponse> call, Throwable t) {

            }
        });
        return listGenreMovie;
    }
}
