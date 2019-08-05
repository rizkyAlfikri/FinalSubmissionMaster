package com.dicoding.picodiploma.finalsubmission.db.tvshowdb;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenreResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.network.ApiService;
import com.dicoding.picodiploma.finalsubmission.network.RetrofitService;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowRepository {
    private String apiKey = Config.API_KEY;
    private ApiService apiService;
    private static TvShowRepository repository;

    private TvShowRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public static TvShowRepository getInstance() {
        if (repository == null) {
            repository = new TvShowRepository(RetrofitService.createService(ApiService.class));
        }
        return repository;
    }

    public MutableLiveData<List<TvShowResults>> getTvFromRetrofit() {
        MutableLiveData<List<TvShowResults>> listTv = new MutableLiveData<>();
        apiService.getTvFromApi(apiKey).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTv.setValue(response.body().getResults());
                    }
                }
                Log.d("Failure Get Tv 1", response.message());
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                Log.d("Failure Get Tv 2", t.getMessage());
            }
        });
        return listTv;
    }

    public MutableLiveData<List<TvShowGenres>> getTvGenre() {
        MutableLiveData<List<TvShowGenres>> listTvGenre = new MutableLiveData<>();
        apiService.getTvGenreApi(apiKey).enqueue(new Callback<TvShowGenreResponse>() {
            @Override
            public void onResponse(Call<TvShowGenreResponse> call, Response<TvShowGenreResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTvGenre.setValue(response.body().getGenres());
                    }
                }
                Log.d("Failure Get Tv Genre 1", response.message());
            }

            @Override
            public void onFailure(Call<TvShowGenreResponse> call, Throwable t) {
                Log.d("Failure Get Tv Genre 2", t.getMessage());
            }
        });
        return listTvGenre;
    }
}
