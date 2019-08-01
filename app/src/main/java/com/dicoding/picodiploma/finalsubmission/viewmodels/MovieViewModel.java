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

public class MovieViewModel extends ViewModel {
    private MutableLiveData<List<MovieResults>> listMovie;
    private MutableLiveData<List<MovieGenres>> listMovieGenre;
    private MovieRepository movieRepository;

    public MovieViewModel() {
        movieRepository = MovieRepository.getInstance();
        listMovie = movieRepository.getMovieFromRetrofit();
        listMovieGenre = movieRepository.getMovieGenre();
    }

    public LiveData<List<MovieResults>> getMovieFromRetrofit() {
        return listMovie;
    }

    public LiveData<List<MovieGenres>> getMovieGenre() {
        return listMovieGenre;
    }

    public LiveData<MovieDetail> getMovieDetail(int movieId, String apiKey) {
        return movieRepository.getMovieDetail(movieId, apiKey);
    }

    public LiveData<List<MovieTrailer>> getMovieTrailer(int movieId, String apiKey) {
        return movieRepository.getMovieTrailer(movieId, apiKey);
    }

    public LiveData<List<MovieReview>> getMovieReview(int movieId, String apikey) {
        return movieRepository.getMovieReview(movieId, apikey);
    }

    public LiveData<List<MovieResults>> getQueryMovie(String apiKey, String query) {
        return movieRepository.getQueryMovie(apiKey, query);
    }
}
