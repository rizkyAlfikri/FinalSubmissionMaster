package com.dicoding.picodiploma.finalsubmission.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.SettingsActivity;
import com.dicoding.picodiploma.finalsubmission.activity.DetailMovieActivity;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.utils.ItemClickSupport;
import com.dicoding.picodiploma.finalsubmission.viewmodels.MovieViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.CONTENT_URI;


public class MovieFragment extends Fragment {
    private MovieAdapter movieAdapter;
    private List<MovieGenres> movieGenres;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingIntent = new Intent(getContext(), SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        init(view.getContext());

        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieGenre().observe(this, getGenreMovieData);
        movieViewModel.getMovieFromRetrofit().observe(this, getMovieData);
    }

    private final Observer<List<MovieResults>> getMovieData = new Observer<List<MovieResults>>() {
        @Override
        public void onChanged(List<MovieResults> movieResults) {
            movieAdapter.setListMovie(movieResults, movieGenres);
            movieAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI + "/" + movieResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieResults.get(position));
                startActivity(intent);
            });
        }
    };

    private final Observer<List<MovieGenres>> getGenreMovieData = new Observer<List<MovieGenres>>() {
        @Override
        public void onChanged(List<MovieGenres> movieGenresData) {
            movieGenres = movieGenresData;
        }
    };

    private void init(Context context) {
        progressBar.setVisibility(View.VISIBLE);
        rvMovie.setLayoutManager(new GridLayoutManager(context, 2));
        rvMovie.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(context);
        rvMovie.setAdapter(movieAdapter);
    }
}
