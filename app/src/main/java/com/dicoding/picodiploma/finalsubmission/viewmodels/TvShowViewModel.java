package com.dicoding.picodiploma.finalsubmission.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.picodiploma.finalsubmission.db.tvshowdb.TvShowRepository;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowDetail;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowReview;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowTrailer;

import java.util.List;

// kelas ini berfungsi untuk mengambil data yang telah di request ke web service.
// yang nantinya data tersebut akan di tampilkan ke activity / fragment
// viewmodel dapat mempertahankan data yang ditampilkan ketika ada perubahan configurasi pada android
// sehingga data yang telah di tampikan tidak perlu di load ulang
public class TvShowViewModel extends ViewModel {

    // inisialisasi variabe; listTv, listTvGenre, dan tv repository
    private MutableLiveData<List<TvShowResults>> listTv;
    private MutableLiveData<List<TvShowGenres>> listTvGenre;
    private TvShowRepository tvShowRepository;

    // construct ini akan membuat TvShowViewModel dapat mengakses data dan method yang ada di tv show repository
    public TvShowViewModel() {
        tvShowRepository = TvShowRepository.getInstance();
        listTv = tvShowRepository.getTvFromRetrofit();
        listTvGenre = tvShowRepository.getTvGenre();
    }

    // method ini berfungsi untuk mendapatkan data tv show yang telah di request ke web service oleh tv show repository
    public LiveData<List<TvShowResults>> getTvFromRetrofit() {
        return listTv;
    }

    // method ini berfungsi untuk mendapatkan data tv show genre yang telah di request ke web service oleh tv show repository
    public LiveData<List<TvShowGenres>> getTvGenre() {
        return listTvGenre;
    }

    // method ini berfungsi untuk mendapatkan data tv show detail yang telah di request ke web service oleh tv show repository
    public LiveData<TvShowDetail> getTvDetail(int id) {
        return tvShowRepository.getTvDetail(id);
    }

    // method ini berfungsi untuk mendapatkan data tv show hasil pencarian yang telah di request ke web service oleh tv show repository
    public LiveData<List<TvShowResults>> getTvQuery(String query) {
        return tvShowRepository.getTvQuery(query);
    }

    // method ini berfungsi untuk mendapatkan data tv show trailer yang telah di request ke web service oleh tv show repository
    public LiveData<List<TvShowTrailer>> getTvTrailer(int id) {
        return tvShowRepository.getTvTrailer(id);
    }

    // method ini berfungsi untuk mendapatkan data tv show review yang telah di request ke web service oleh tv show repository
    public LiveData<List<TvShowReview>> getTvReview(int id) {
        return tvShowRepository.getTvReview(id);
    }
}
