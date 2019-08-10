package com.dicoding.picodiploma.finalsubmission.fragments.moviefragments;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieFavoriteAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.utils.Config;
import com.dicoding.picodiploma.finalsubmission.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMovieFragment extends Fragment {
    public static final String MOVIE_QUERY_EXTRA = "movie_query_extra";
    private String queryBundle;
    private MovieViewModel movieViewModel;
    private MovieFavoriteAdapter searchAdapter;
    private ArrayList<MovieResults> listMovie = new ArrayList<>();
    @BindView(R.id.rv_search_movie)
    RecyclerView rvSearchMovie;


    public SearchMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_movie, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        rvSearchMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchMovie.setHasFixedSize(true);
        searchAdapter = new MovieFavoriteAdapter(getActivity());
        rvSearchMovie.setAdapter(searchAdapter);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager;
        if (getContext() != null) {
            searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
            if (searchManager != null) {
                SearchView searchView =
                        (SearchView) (menu.findItem(R.id.action_movie_search)).getActionView();
                searchView.setQueryHint(getString(R.string.search_movie_hint));
                searchView.setQuery(queryBundle, true);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        searchQueryMovie(newText);
                        return true;
                    }
                });
            }
        }
    }

    private void searchQueryMovie(String query) {
        movieViewModel.getQueryMovie(Config.API_KEY, query).observe(this, new Observer<List<MovieResults>>() {
            @Override
            public void onChanged(List<MovieResults> movieResults) {
                listMovie.addAll(movieResults);
                searchAdapter.setListMovie(listMovie);
                searchAdapter.notifyDataSetChanged();
            }
        });
    }
}
