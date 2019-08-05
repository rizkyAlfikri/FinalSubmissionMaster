package com.dicoding.picodiploma.finalsubmission.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.picodiploma.finalsubmission.db.tvshowdb.TvShowRepository;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;

import java.util.List;

public class TvShowViewModel extends ViewModel {
    private MutableLiveData<List<TvShowResults>> listTv;
    private MutableLiveData<List<TvShowGenres>> listTvGenre;
    private TvShowRepository tvShowRepository;

    public TvShowViewModel() {
        tvShowRepository = TvShowRepository.getInstance();
        listTv = tvShowRepository.getTvFromRetrofit();
        listTvGenre = tvShowRepository.getTvGenre();
    }

    public LiveData<List<TvShowResults>> getTvFromRetrofit() {
        return listTv;
    }

    public LiveData<List<TvShowGenres>> getTvGenre() {
        return listTvGenre;
    }
}
