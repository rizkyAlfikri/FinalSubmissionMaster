package com.dicoding.picodiploma.finalsubmission.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieRepository;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;

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

    public LiveData<List<MovieResults>> getQueryMovie(String apiKey, String query) {
        return movieRepository.getQueryMovie(apiKey, query);
    }
}
