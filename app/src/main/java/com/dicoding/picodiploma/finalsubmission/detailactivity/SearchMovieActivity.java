package com.dicoding.picodiploma.finalsubmission.detailactivity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.SettingsActivity;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResponse;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.network.ApiService;
import com.dicoding.picodiploma.finalsubmission.network.RetrofitService;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieActivity extends AppCompatActivity{
    public static final String EXTRA_SEARCH = "extra_search";
    private List<MovieResults> listMovie = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private SearchView searchView;
    private ApiService apiService = RetrofitService.createService(ApiService.class);
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        ButterKnife.bind(this);
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        rvMovie.setAdapter(movieAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager;
        searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if (searchManager != null) {

            searchView = (SearchView) (menu.findItem(R.id.action_activity_search)).getActionView();
            String queryIntent = getIntent().getStringExtra(EXTRA_SEARCH);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SettingsActivity.class)));
            searchView.setQuery(queryIntent, true);
            searchView.setQueryHint("Search Movie");
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.clearFocus();
            searchView.onActionViewExpanded();
            searchQuery(searchView);
        }

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

    private void searchQuery(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                apiService.getQueryMovie(Config.API_KEY, query).enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            if (response.isSuccessful()) {
                                listMovie = response.body().getResults();
                                movieAdapter.setListMovie(listMovie);
                                movieAdapter.notifyDataSetChanged();
                            }

                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
