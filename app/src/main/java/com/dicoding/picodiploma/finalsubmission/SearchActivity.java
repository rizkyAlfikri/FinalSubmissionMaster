package com.dicoding.picodiploma.finalsubmission;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.activity.DetailMovieActivity;
import com.dicoding.picodiploma.finalsubmission.activity.DetailTvShowActivity;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieSearchAdapter;
import com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter.TvShowSearchAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.utils.ItemClickSupport;
import com.dicoding.picodiploma.finalsubmission.viewmodels.MovieViewModel;
import com.dicoding.picodiploma.finalsubmission.viewmodels.TvShowViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_MOVIE;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_TV;

public class  SearchActivity extends AppCompatActivity {
    public static final String EXTRA_SEARCH = "extra_search";
    public static final String MOVIE_SEARCH = "movie_search";
    public static final String TV_SEARCH = "tv_search";
    private int pageNum = 1;
    private boolean isMovie = false;
    private MovieSearchAdapter movieSearchAdapter;
    private TvShowSearchAdapter tvShowSearchAdapter;
    private MovieViewModel movieViewModel;
    private TvShowViewModel tvShowViewModel;
    @BindView(R.id.txt_page)
    TextView txtPage;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rv_search_movie)
    RecyclerView rvSearch;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.searchView)
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        String setAction = getIntent().getAction();
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setHasFixedSize(true);

        String query = getIntent().getStringExtra(EXTRA_SEARCH);

        // satement dibawah ini berfungsi untuk memisahkan penggunaan search pada masimg masing data
        if (setAction != null) {
            if (setAction.equals(MOVIE_SEARCH)) {
                initMovie(query);
                isMovie = true;
            } else if (setAction.equals(TV_SEARCH)) {
                initTvShow(query);
                isMovie = false;
            }
        }
        searchInit(query);
        txtPage.setText(String.valueOf(pageNum));

    }

    // get data search movie from ViewModel
    private final Observer<List<MovieResults>> getMovieQueryData = new Observer<List<MovieResults>>() {
        @Override
        public void onChanged(List<MovieResults> movieResults) {
            movieSearchAdapter.setListMovie(movieResults);
            movieSearchAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            ItemClickSupport.addTo(rvSearch).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movieResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieResults.get(position));
                startActivity(intent);
            });
        }
    };

    // get data search movie from ViewModel
    private final Observer<List<MovieGenres>> getMovieGenreData = new Observer<List<MovieGenres>>() {
        @Override
        public void onChanged(List<MovieGenres> movieGenres) {
            movieSearchAdapter.setListGenreMovie(movieGenres);

        }
    };

    // get data search tv show from ViewModel
    private final Observer<List<TvShowResults>> getTvQueryData = new Observer<List<TvShowResults>>() {
        @Override
        public void onChanged(List<TvShowResults> tvShowResults) {
            tvShowSearchAdapter.setListTv(tvShowResults);
            tvShowSearchAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

            // statement ini membuat data yang ditampilkan dalam recyclerview dapat di access satu persatu
            ItemClickSupport.addTo(rvSearch).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI_TV + "/" + tvShowResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailTvShowActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailTvShowActivity.EXTRA_TV, tvShowResults.get(position));
                startActivity(intent);

            });
        }
    };

    // get data genre tv show from ViewModel
    private final Observer<List<TvShowGenres>> getTvGenreData = new Observer<List<TvShowGenres>>() {
        @Override
        public void onChanged(List<TvShowGenres> tvShowGenres) {
            tvShowSearchAdapter.setListGenreTv(tvShowGenres);
        }
    };

    // inisialisasi adapter movie dan judul action bar
    private void initMovie(String query) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.movie_search_result));
        }
        movieSearchAdapter = new MovieSearchAdapter(this);
        rvSearch.setAdapter(movieSearchAdapter);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        queryMovie(query);
    }

    // method ini berfungsi untuk melakukan request movie ke web service
    private void queryMovie(String query) {
        imgNext.setOnClickListener(v -> {
            pageNum += 1;
            txtPage.setText(String.valueOf(pageNum));
            progressBar.setVisibility(View.VISIBLE);

            movieViewModel.getQueryMovie(query, pageNum).observe(SearchActivity.this, getMovieQueryData);
            movieViewModel.getMovieGenre().observe(SearchActivity.this, getMovieGenreData);

        });

        imgBack.setOnClickListener(v -> {
            if (pageNum > 1) {
                pageNum -= 1;
                txtPage.setText(String.valueOf(pageNum));
                progressBar.setVisibility(View.VISIBLE);

                movieViewModel.getQueryMovie(query, pageNum).observe(SearchActivity.this, getMovieQueryData);
                movieViewModel.getMovieGenre().observe(SearchActivity.this, getMovieGenreData);
            }
        });

        movieViewModel.getQueryMovie(query, pageNum).observe(this, getMovieQueryData);
        movieViewModel.getMovieGenre().observe(this, getMovieGenreData);
    }

    // inisialisasi adapter tv show dan judul action bar
    private void initTvShow(String query) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.tv_search_result));
        }

        tvShowSearchAdapter = new TvShowSearchAdapter(this);
        rvSearch.setAdapter(tvShowSearchAdapter);
        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        queryTvShow(query);
    }

    // method ini untuk mengrequest tv show ke web service
    private void queryTvShow(String query) {
        imgNext.setOnClickListener(v -> {
            pageNum += 1;
            txtPage.setText(String.valueOf(pageNum));
            progressBar.setVisibility(View.VISIBLE);

            tvShowViewModel.getTvQuery(query, pageNum).observe(this, getTvQueryData);
            tvShowViewModel.getTvGenre().observe(this, getTvGenreData);

        });

        imgBack.setOnClickListener(v -> {
            if (pageNum > 1) {
                pageNum -= 1;
                txtPage.setText(String.valueOf(pageNum));
                progressBar.setVisibility(View.VISIBLE);

                tvShowViewModel.getTvQuery(query, pageNum).observe(this, getTvQueryData);
                tvShowViewModel.getTvGenre().observe(this, getTvGenreData);
            }
        });


        tvShowViewModel.getTvQuery(query, pageNum).observe(this, getTvQueryData);
        tvShowViewModel.getTvGenre().observe(this, getTvGenreData);
    }

    // inisialisasi komponen SearchView
    private void searchInit(String query) {

        searchView.setQuery(query, false);
        searchView.setIconified(false);
        searchView.clearFocus();

        // statement ini untuk menghilangkan keyboard setelah user mengetikan query
        InputMethodManager in = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

        // method ini berfungsi untuk melakukan aksi ketika searchview digunakan
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // apa yang diketika oleh user bisa menjadi query key pada saat request data pencarian ke web service
                if (isMovie) {
                    queryMovie(query);
                    pageNum = 1;
                    txtPage.setText(String.valueOf(pageNum));
                } else {
                    queryTvShow(query);
                    pageNum = 1;
                    txtPage.setText(String.valueOf(pageNum));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
