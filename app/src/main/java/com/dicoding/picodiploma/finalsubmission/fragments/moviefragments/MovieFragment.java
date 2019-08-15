package com.dicoding.picodiploma.finalsubmission.fragments.moviefragments;

import android.app.SearchManager;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.SearchActivity;
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

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_MOVIE;


public class MovieFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;
    private int newPosition;
    private int pageNum = 1;
    @BindView(R.id.txt_page)
    TextView txtPage;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.spinner_movie)
    Spinner spinnerMovie;
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
        setHasOptionsMenu(true);
        init(getContext());
        initPage();
        selectMovieByCategory();

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_movie, menu);
        // method ini berfungsi umtuk melakukan pencarian data
        searchMovie(menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            // ketika tombol action_setting di tekan, maka akan menampilkan activity pengaturan
            Intent settingIntent = new Intent(getContext(), SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectCategory = getString(R.string.select_category_movie);
        this.newPosition = position;
        if (parent.getItemAtPosition(position).equals(selectCategory)) {

            movieViewModel.getMovieDiscovery(pageNum).observe(this, getMovieDiscoveryData);
            movieViewModel.getMovieGenre().observe(this, getGenreMovieData);

        } else {
            switch (position) {
                case 1:
                    progressBar.setVisibility(View.VISIBLE);
                    pageNum = 1;
                    txtPage.setText(String.valueOf(pageNum));
                    movieViewModel.getMoviePopular(pageNum).observe(this, getMoviePopularData);
                    movieViewModel.getMovieGenre().observe(this, getGenreMovieData);
                    break;
                case 2:
                    progressBar.setVisibility(View.VISIBLE);
                    pageNum = 1;
                    txtPage.setText(String.valueOf(pageNum));
                    movieViewModel.getMovieGenre().observe(this, getGenreMovieData);
                    movieViewModel.getMovieTop(pageNum).observe(this, getMovieTopData);
                    break;
                case 3:
                    progressBar.setVisibility(View.VISIBLE);
                    pageNum = 1;
                    txtPage.setText(String.valueOf(pageNum));
                    movieViewModel.getMovieGenre().observe(this, getGenreMovieData);
                    movieViewModel.getMovieUpcoming(pageNum).observe(this, getMovieUpComingData);
                    break;
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_next) {
            pageNum += 1;
            progressBar.setVisibility(View.VISIBLE);
            txtPage.setText(String.valueOf(pageNum));
            getCategoryMovie(newPosition);
        } else if (v.getId() == R.id.img_back) {
            if (pageNum > 1) {
                pageNum -= 1;
                progressBar.setVisibility(View.VISIBLE);
                txtPage.setText(String.valueOf(pageNum));
                getCategoryMovie(newPosition);
            }
        }
    }


    // method ini berfungsi untuk meng inisialisasi komponen yang akan digunakan
    private void init(Context context) {
        progressBar.setVisibility(View.VISIBLE);
        rvMovie.setLayoutManager(new GridLayoutManager(context, 2));
        rvMovie.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(context);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
    }


    private void initPage() {
        txtPage.setText(String.valueOf(pageNum));
        imgNext.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }


    // method ini untuk menampilkan category movie
    private void selectMovieByCategory() {
        if (getActivity() != null) {
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.spinner_movie, android.R.layout.simple_spinner_item);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMovie.setAdapter(spinnerAdapter);
            spinnerMovie.setOnItemSelectedListener(this);
        }
    }


    // method ini untuk memilih category movie
    private void getCategoryMovie(int position) {
        switch (position) {
            case 1:
                progressBar.setVisibility(View.VISIBLE);
                movieViewModel.getMoviePopular(pageNum).observe(this, getMoviePopularData);
                movieViewModel.getMovieGenre().observe(this, getGenreMovieData);
                break;
            case 2:
                progressBar.setVisibility(View.VISIBLE);
                movieViewModel.getMovieGenre().observe(this, getGenreMovieData);
                movieViewModel.getMovieTop(pageNum).observe(this, getMovieTopData);
                break;
            case 3:
                progressBar.setVisibility(View.VISIBLE);
                movieViewModel.getMovieGenre().observe(this, getGenreMovieData);
                movieViewModel.getMovieUpcoming(pageNum).observe(this, getMovieUpComingData);
                break;
        }
    }


    // method dibawah ini untuk mendapatkan data dari ViewModel
    private final Observer<List<MovieResults>> getMovieDiscoveryData = new Observer<List<MovieResults>>() {
        @Override
        public void onChanged(List<MovieResults> movieResults) {
            movieAdapter.setListMovie(movieResults);
            movieAdapter.notifyDataSetChanged();
            rvMovie.setAdapter(movieAdapter);
            progressBar.setVisibility(View.GONE);

            // statement ini membuat list yang ditampilkan oleh recyclerview dapat di access / diklik
            ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movieResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieResults.get(position));
                startActivityForResult(intent, DetailMovieActivity.REQUEST_MOVIE_UPDATE);
            });
        }
    };


    private final Observer<List<MovieResults>> getMoviePopularData = new Observer<List<MovieResults>>() {
        @Override
        public void onChanged(List<MovieResults> movieResults) {
            movieAdapter.setListMovie(movieResults);
            movieAdapter.notifyDataSetChanged();
            rvMovie.setAdapter(movieAdapter);
            progressBar.setVisibility(View.GONE);

            ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movieResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieResults.get(position));
                startActivityForResult(intent, DetailMovieActivity.REQUEST_MOVIE_UPDATE);
            });
        }
    };


    private final Observer<List<MovieResults>> getMovieTopData = new Observer<List<MovieResults>>() {
        @Override
        public void onChanged(List<MovieResults> movieResults) {
            movieAdapter.setListMovie(movieResults);
            movieAdapter.notifyDataSetChanged();
            rvMovie.setAdapter(movieAdapter);
            progressBar.setVisibility(View.GONE);

            ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movieResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieResults.get(position));
                startActivityForResult(intent, DetailMovieActivity.REQUEST_MOVIE_UPDATE);
            });
        }
    };


    private final Observer<List<MovieResults>> getMovieUpComingData = new Observer<List<MovieResults>>() {
        @Override
        public void onChanged(List<MovieResults> movieResults) {
            movieAdapter.setListMovie(movieResults);
            movieAdapter.notifyDataSetChanged();
            rvMovie.setAdapter(movieAdapter);
            progressBar.setVisibility(View.GONE);

            ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movieResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieResults.get(position));
                startActivityForResult(intent, DetailMovieActivity.REQUEST_MOVIE_UPDATE);
            });
        }
    };


    private final Observer<List<MovieGenres>> getGenreMovieData = new Observer<List<MovieGenres>>() {
        @Override
        public void onChanged(List<MovieGenres> movieGenresData) {
            movieAdapter.setListGenreMovie(movieGenresData);
        }
    };


    // method ini berfungsi untuk menghilangkan keyboard setiap kali user melakukan pencarian data
    private void hidekeyboard(SearchView searchView) {
        if (getContext() != null) {
            InputMethodManager methodManager = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
    }


    // method ini berfungsi untuk melakukan pencarian data
    private void searchMovie(Menu menu) {
        SearchManager searchManager;
        if (getContext() != null) {
            searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
            if (searchManager != null) {
                SearchView searchView =
                        (SearchView) (menu.findItem(R.id.action_movie_search)).getActionView();
                if (getActivity() != null) {
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                }

                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                searchView.setQueryHint(getString(R.string.search_movie_hint));


                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                        searchIntent.putExtra(SearchActivity.EXTRA_SEARCH, query);
                        searchIntent.setAction(SearchActivity.MOVIE_SEARCH);
                        startActivity(searchIntent);
                        hidekeyboard(searchView);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                };

                searchView.setOnQueryTextListener(queryTextListener);
            }
        }
    }
}



