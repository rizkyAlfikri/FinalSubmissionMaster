package com.dicoding.picodiploma.finalsubmission.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieAdapter;
import com.dicoding.picodiploma.finalsubmission.detailactivity.DetailMovieActivity;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenreResponse;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        progressBar.setVisibility(View.VISIBLE);
        rvMovie.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvMovie.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(view.getContext());
        rvMovie.setAdapter(movieAdapter);

        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieFromRetrofit().observe(this, getMovieData);

//        movieViewModel.getMovieGenreRetrofit().observe(this, getMovieGenreData);
    }

    private final Observer<List<MovieResults>> getMovieData = new Observer<List<MovieResults>>() {
        @Override
        public void onChanged(List<MovieResults> movieResults) {
            movieAdapter.setListMovie(movieResults);
            movieAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            ItemClickSupport.addTo(rvMovie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Uri uri = Uri.parse(CONTENT_URI + "/" + movieResults.get(position).getId());
                    Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
                    intent.setData(uri);
                    intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieResults.get(position));
                    startActivity(intent);
                }
            });
        }
    };
//
//    private final Observer<List<MovieGenres>> getMovieGenreData = new Observer<List<MovieGenres>>() {
//        @Override
//        public void onChanged(List<MovieGenres> movieGenres) {
//            movieAdapter.setListGenreMovie(movieGenres);
//            movieAdapter.notifyDataSetChanged();
//        }
//    };
}
