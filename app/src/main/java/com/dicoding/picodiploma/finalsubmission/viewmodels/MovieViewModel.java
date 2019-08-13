package com.dicoding.picodiploma.finalsubmission.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieRepository;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieDetail;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReview;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieTrailer;

import java.util.List;

// kelas ini berfungsi untuk mengambil data yang telah di request ke web service.
// yang nantinya data tersebut akan di tampilkan ke activity / fragment
// viewmodel dapat mempertahankan data yang ditampilkan ketika ada perubahan configurasi pada android
// sehingga data yang telah di tampikan tidak perlu di load ulang
public class MovieViewModel extends ViewModel {

    // inisialiasi  variabel listMovie, listMovieGenre, dan movie repository
    private MutableLiveData<List<MovieResults>> listMovie;
    private MutableLiveData<List<MovieGenres>> listMovieGenre;
    private MovieRepository movieRepository;

    // construct ini akan membuat MovieViewModel dapat mengakses data dan method yang ada di movie repository
    public MovieViewModel() {
        movieRepository = MovieRepository.getInstance();
        listMovie = movieRepository.getMovieFromRetrofit();
        listMovieGenre = movieRepository.getMovieGenre();
    }

    // method ini berfungsi untuk mendapatkan data movie yang telah di request ke web service oleh movie repository
    public LiveData<List<MovieResults>> getMovieFromRetrofit() {
        return listMovie;
    }

    // method ini berfungsi untuk mendapatkan data movie genre yang telah di request ke web service oleh movie repository
    public LiveData<List<MovieGenres>> getMovieGenre() {
        return listMovieGenre;
    }

    // method ini berfungsi untuk mendapatkan data movie detail yang telah di request ke web service oleh movie repository
    public LiveData<MovieDetail> getMovieDetail(int movieId) {
        return movieRepository.getMovieDetail(movieId);
    }

    // method ini berfungsi untuk mendapatkan data movie trailer yang telah di request ke web service oleh movie repository
    public LiveData<List<MovieTrailer>> getMovieTrailer(int movieId) {
        return movieRepository.getMovieTrailer(movieId);
    }

    // method ini berfungsi untuk mendapatkan data movie review yang telah di request ke web service oleh movie repository
    public LiveData<List<MovieReview>> getMovieReview(int movieId) {
        return movieRepository.getMovieReview(movieId);
    }

    // method ini berfungsi untuk mendapatkan data movie hasil pencarian yang telah di request ke web service oleh movie repository
    public LiveData<List<MovieResults>> getQueryMovie(String query) {
        return movieRepository.getQueryMovie(query);
    }
}
