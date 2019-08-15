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
    private MutableLiveData<List<MovieGenres>> listMovieGenre;
    private MovieRepository movieRepository;

    // construct ini akan membuat MovieViewModel dapat mengakses data dan method yang ada di movie repository
    public MovieViewModel() {
        movieRepository = MovieRepository.getInstance();
        listMovieGenre = movieRepository.getMovieGenre();
    }

    // get LiveData movie from repository
    public LiveData<List<MovieResults>> getMovieDiscovery(int page) {
        return movieRepository.getMovieDiscovery(page);
    }

    // get LiveData popular movie from repository
    public LiveData<List<MovieResults>> getMoviePopular(int page) {
        return movieRepository.getMoviePopular(page);
    }

    // get LiveData top rated movie from repository
    public LiveData<List<MovieResults>> getMovieTop(int page) {
        return movieRepository.getMovieTopRated(page);
    }

    // get LiveData upcoming movie from repository
    public LiveData<List<MovieResults>> getMovieUpcoming(int page) {
        return movieRepository.getMovieUpcoming(page);
    }

    // get LiveData genre movie from repository
    public LiveData<List<MovieGenres>> getMovieGenre() {
        return listMovieGenre;
    }

    // get LiveData detail movie from repository
    public LiveData<MovieDetail> getMovieDetail(int movieId) {
        return movieRepository.getMovieDetail(movieId);
    }


    // get LiveData trailer movie from repository
    public LiveData<List<MovieTrailer>> getMovieTrailer(int movieId) {
        return movieRepository.getMovieTrailer(movieId);
    }


    // get LiveData review movie from repository
    public LiveData<List<MovieReview>> getMovieReview(int movieId) {
        return movieRepository.getMovieReview(movieId);
    }


    // get LiveData search movie from repository
    public LiveData<List<MovieResults>> getQueryMovie(String query, int page) {
        return movieRepository.getQueryMovie(query, page);
    }
}
