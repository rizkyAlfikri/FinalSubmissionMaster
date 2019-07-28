package com.dicoding.picodiploma.finalsubmission.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieRepository;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private MutableLiveData<List<MovieResults>> listMovie;
    private MovieRepository movieRepository;
//    private MutableLiveData<List<MovieGenres>> listGenreMovie;

    public MovieViewModel() {
        movieRepository = MovieRepository.getInstance();
        listMovie = movieRepository.getMovieFromRetrofit();

//        listGenreMovie = movieRepository.getMovieGenreRetrofit();
    }

    public LiveData<List<MovieResults>> getMovieFromRetrofit() {
        return listMovie;
    }

//    public LiveData<List<MovieResults>> getQueryMovie(String apiKey, String query) {
//        return movieRepository.getQueryMovie(apiKey, query);
//    }

//    public LiveData<List<MovieGenres>> getMovieGenreRetrofit() {
//        return listGenreMovie;
//    }
}
