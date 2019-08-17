package com.dicoding.picodiploma.finalsubmission.db.tvshowdb;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowDetail;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenreResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowReview;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowReviewResponse;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowTrailer;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowTrailerResponse;
import com.dicoding.picodiploma.finalsubmission.network.ApiService;
import com.dicoding.picodiploma.finalsubmission.network.RetrofitService;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowRepository {
    // inisialisasi ApiService, dan membuat construct movie repository ke dalam
    // visibility private karena akan menggunakan pola singleton
    private String apiKey = Config.API_KEY;
    private ApiService apiService;
    private static TvShowRepository repository;

    // method constract ini menginisialisasi apiService
    private TvShowRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    // method ini membantu movie reposity melakukan construct
    public static TvShowRepository getInstance() {
        if (repository == null) {
            repository = new TvShowRepository(RetrofitService.createService(ApiService.class));
        }
        return repository;
    }

    // method ini berfungsi untuk mengrequest data tv show ke web service movieDB
    public MutableLiveData<List<TvShowResults>> getTvFromRetrofit(int page) {
        MutableLiveData<List<TvShowResults>> listTv = new MutableLiveData<>();
        apiService.getTvFromApi(apiKey, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTv.setValue(response.body().getResults());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                Log.d("Failure Get Tv 2", t.getMessage());
            }
        });
        return listTv;

    }

    // method ini berfungsi untuk mengrequest data popular tv show ke web service movieDB
    public MutableLiveData<List<TvShowResults>> getTvPopular(int page) {
        MutableLiveData<List<TvShowResults>> listPopularTv = new MutableLiveData<>();
        apiService.getTvPopular(apiKey, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listPopularTv.setValue(response.body().getResults());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                Log.e("Failure GetTvPopular", t.getMessage());
            }
        });
        return listPopularTv;
    }

    // method ini berfungsi untuk mengrequest data top rated tv show ke web service movieDB
    public MutableLiveData<List<TvShowResults>> getTvTopRated(int page) {
        MutableLiveData<List<TvShowResults>> listTvTop = new MutableLiveData<>();
        apiService.getTvTopRated(apiKey, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTvTop.setValue(response.body().getResults());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                Log.e("Failure Tv Top", t.getMessage());
            }
        });
        return listTvTop;
    }

    // method ini berfungsi untuk mengrequest data on the air tv show ke web service movieDB
    public MutableLiveData<List<TvShowResults>> getTvOnAir(int page) {
        MutableLiveData<List<TvShowResults>> listTvAir = new MutableLiveData<>();
        apiService.getTvOnAir(apiKey, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTvAir.setValue(response.body().getResults());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                Log.e("Failure get Tv On Air", t.getMessage());
            }
        });
        return listTvAir;
    }

    // method ini berfungsi untuk mengrequest data tv show genre ke web service movieDB
    public MutableLiveData<List<TvShowGenres>> getTvGenre() {
        MutableLiveData<List<TvShowGenres>> listTvGenre = new MutableLiveData<>();
        apiService.getTvGenreApi(apiKey).enqueue(new Callback<TvShowGenreResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowGenreResponse> call, @NonNull Response<TvShowGenreResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTvGenre.setValue(response.body().getGenres());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowGenreResponse> call, @NonNull Throwable t) {
                Log.d("Failure Get Tv Genre 2", t.getMessage());
            }
        });
        return listTvGenre;

    }

    // method ini berfungsi untuk mengrequest data tv show detail ke web service movieDB
    public MutableLiveData<TvShowDetail> getTvDetail(int tvId) {
        MutableLiveData<TvShowDetail> listTvDetail = new MutableLiveData<>();
        apiService.getTvDetailApi(tvId, apiKey).enqueue(new Callback<TvShowDetail>() {
            @Override
            public void onResponse(@NonNull Call<TvShowDetail> call, @NonNull Response<TvShowDetail> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTvDetail.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowDetail> call, @NonNull Throwable t) {

                Log.d("Failure Get Tv Detail 2", t.getMessage());
            }
        });
        return listTvDetail;

    }

    // method ini berfungsi untuk mengrequest data tv show hasil pencarian user ke web service movieDB
    public MutableLiveData<List<TvShowResults>> getTvQuery(String query, int page) {
        MutableLiveData<List<TvShowResults>> listTvQuery = new MutableLiveData<>();
        apiService.getQueryTv(apiKey, query, page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTvQuery.setValue(response.body().getResults());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                Log.d("Failure Get Tv Query 2", t.getMessage());
            }
        });
        return listTvQuery;

    }

    // method ini berfungsi untuk mengrequest data tv show video trailer ke web service movieDB
    public MutableLiveData<List<TvShowTrailer>> getTvTrailer(int tvId) {
        MutableLiveData<List<TvShowTrailer>> listTvTrailer = new MutableLiveData<>();
        apiService.getTvTrailerApi(tvId, apiKey).enqueue(new Callback<TvShowTrailerResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowTrailerResponse> call, @NonNull Response<TvShowTrailerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTvTrailer.setValue(response.body().getResults());
                    }
                }

                Log.d("Failure Tv Trailer 1", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowTrailerResponse> call, @NonNull Throwable t) {
                Log.d("Failure Tv Trailer 1", t.getMessage());
            }
        });
        return listTvTrailer;

    }

    // method ini berfungsi untuk mengrequest data tv show review ke web service movieDB
    public MutableLiveData<List<TvShowReview>> getTvReview(int tvId) {
        MutableLiveData<List<TvShowReview>> listTvReview = new MutableLiveData<>();
        apiService.getTvReviewApi(tvId, apiKey).enqueue(new Callback<TvShowReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowReviewResponse> call, @NonNull Response<TvShowReviewResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listTvReview.setValue(response.body().getResults());
                    }
                }
                Log.d("Failure Get Tv Review 1", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowReviewResponse> call, @NonNull Throwable t) {
                Log.d("Failure Get Tv Review 2", t.getMessage());
            }
        });
        return listTvReview;

    }
}
