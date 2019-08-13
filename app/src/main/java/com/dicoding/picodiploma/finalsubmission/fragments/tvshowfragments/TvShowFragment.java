package com.dicoding.picodiploma.finalsubmission.fragments.tvshowfragments;


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
import android.widget.ProgressBar;

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
import com.dicoding.picodiploma.finalsubmission.activity.DetailTvShowActivity;
import com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter.TvShowAdapter;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.utils.ItemClickSupport;
import com.dicoding.picodiploma.finalsubmission.viewmodels.TvShowViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_TV;


public class TvShowFragment extends Fragment {
    private TvShowAdapter tvShowAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.rv_tvshow)
    RecyclerView rvTvShow;

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        init(view.getContext());

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate menu xml yang akan digunakan
        inflater.inflate(R.menu.main_movie, menu);

        // method ini berfungsi umtuk melakukan pencarian data
        searchTvShow(menu);
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

    // statement ini berfungsi untuk menangkap data tv show dari webservice movieDb,
    // data yang telah di tangkap akan di masukan ke  tv show adapter yang nantinya akan di tampilkan
    private final Observer<List<TvShowResults>> getTvData = new Observer<List<TvShowResults>>() {
        @Override
        public void onChanged(List<TvShowResults> tvShowResults) {
            tvShowAdapter.setListTv(tvShowResults);
            tvShowAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            ItemClickSupport.addTo(rvTvShow).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI_TV + "/" + tvShowResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailTvShowActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailTvShowActivity.EXTRA_TV, tvShowResults.get(position));
                startActivity(intent);
            });
        }
    };

    // statement ini berfungsi untuk menangkap data tv show genre dari webservice movieDb,
    // data yang telah di tangkap akan di masukan ke tv show genre adapter yang nantinya akan di tampilkan
    private final Observer<List<TvShowGenres>> getTvGenre = new Observer<List<TvShowGenres>>() {
        @Override
        public void onChanged(List<TvShowGenres> tvShowGenres) {
            tvShowAdapter.setListTvGenre(tvShowGenres);
            tvShowAdapter.notifyDataSetChanged();
        }
    };

    // method ini berfungsi untuk meng inisialisasi RecyclerView, Adapter dan ViewModel
    private void init(Context context) {
        rvTvShow.setLayoutManager(new GridLayoutManager(context, 2));
        rvTvShow.setHasFixedSize(true);
        tvShowAdapter = new TvShowAdapter(getContext());
        rvTvShow.setAdapter(tvShowAdapter);
        progressBar.setVisibility(View.VISIBLE);

        TvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getTvFromRetrofit().observe(this, getTvData);
        tvShowViewModel.getTvGenre().observe(this, getTvGenre);
    }

    // method ini berfungsi untuk melakukan pencarian data
    private void searchTvShow(Menu menu) {
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
                searchView.setQueryHint(getString(R.string.search_tv_hint));

                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                        searchIntent.putExtra(SearchActivity.EXTRA_SEARCH, query);
                        searchIntent.setAction(SearchActivity.TV_SEARCH);
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

    // method ini berfungsi untuk menghilangkan keyboard setiap kali user melakukan pencarian data
    private void hidekeyboard(SearchView searchView) {
        if (getContext() != null) {
            InputMethodManager methodManager = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
    }
}


