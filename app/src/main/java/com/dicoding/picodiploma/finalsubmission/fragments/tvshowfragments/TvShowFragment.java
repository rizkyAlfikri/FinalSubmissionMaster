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


public class TvShowFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private TvShowAdapter tvShowAdapter;
    private TvShowViewModel tvShowViewModel;
    private int newPosition;
    private int pageNum = 1;
    @BindView(R.id.txt_page)
    TextView txtPage;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.rv_tvshow)
    RecyclerView rvTvShow;
    @BindView(R.id.spinner_tv)
    Spinner spinnerTv;

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        init(view.getContext());
        initPage();
        selectedTvByCategory();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_movie, menu);
        // method ini untuk melakukan pencarian data
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


    // method ini berfungsi untuk memilih category tv show
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectCategory = getString(R.string.select_category_movie);
        this.newPosition = position;
        if (parent.getItemAtPosition(position).equals(selectCategory)) {

            tvShowViewModel.getTvFromRetrofit(pageNum).observe(this, getTvData);
            tvShowViewModel.getTvGenre().observe(this, getTvGenre);

        } else {
            switch (position) {
                case 1:
                    progressBar.setVisibility(View.VISIBLE);
                    pageNum = 1;
                    txtPage.setText(String.valueOf(pageNum));
                    tvShowViewModel.getTvPopular(pageNum).observe(this, getTvPopularData);
                    tvShowViewModel.getTvGenre().observe(this, getTvGenre);
                    break;
                case 2:
                    progressBar.setVisibility(View.VISIBLE);
                    pageNum = 1;
                    txtPage.setText(String.valueOf(pageNum));
                    tvShowViewModel.getTvTopRated(pageNum).observe(this, getTvTopData);
                    tvShowViewModel.getTvGenre().observe(this, getTvGenre);
                    break;
                case 3:
                    progressBar.setVisibility(View.VISIBLE);
                    pageNum = 1;
                    txtPage.setText(String.valueOf(pageNum));
                    tvShowViewModel.getTvOnAir(pageNum).observe(this, getTvOnAir);
                    tvShowViewModel.getTvGenre().observe(this, getTvGenre);
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
            getCategoryTv(newPosition);
        } else if (v.getId() == R.id.img_back) {
            if (pageNum > 1) {
                pageNum -= 1;
                progressBar.setVisibility(View.VISIBLE);
                txtPage.setText(String.valueOf(pageNum));
                getCategoryTv(newPosition);
            }
        }
    }


    // method ini untuk meng inisialisasi komponen  yang akan digunakan
    private void init(Context context) {
        rvTvShow.setLayoutManager(new GridLayoutManager(context, 2));
        rvTvShow.setHasFixedSize(true);
        tvShowAdapter = new TvShowAdapter(getContext());
        rvTvShow.setAdapter(tvShowAdapter);
        progressBar.setVisibility(View.VISIBLE);

        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
    }


    private void initPage() {
        txtPage.setText(String.valueOf(pageNum));
        imgNext.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }


    // method ini untuk inisialisasi category tv show
    private void selectedTvByCategory() {
        if (getActivity() != null) {
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                    getActivity(), R.array.spinner_tv, android.R.layout.simple_spinner_item);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTv.setAdapter(spinnerAdapter);
            spinnerTv.setOnItemSelectedListener(this);
        }
    }


    private void getCategoryTv(int position) {
        switch (position) {
            case 1:
                progressBar.setVisibility(View.VISIBLE);
                tvShowViewModel.getTvPopular(pageNum).observe(this, getTvPopularData);
                tvShowViewModel.getTvGenre().observe(this, getTvGenre);
                break;
            case 2:
                progressBar.setVisibility(View.VISIBLE);
                tvShowViewModel.getTvTopRated(pageNum).observe(this, getTvTopData);
                tvShowViewModel.getTvGenre().observe(this, getTvGenre);
                break;
            case 3:
                progressBar.setVisibility(View.VISIBLE);
                tvShowViewModel.getTvOnAir(pageNum).observe(this, getTvOnAir);
                tvShowViewModel.getTvGenre().observe(this, getTvGenre);
                break;
        }
    }


    // method dibawah ini untuk mendapatkan data dari ViewModel
    private final Observer<List<TvShowResults>> getTvData = new Observer<List<TvShowResults>>() {
        @Override
        public void onChanged(List<TvShowResults> tvShowResults) {
            tvShowAdapter.setListTv(tvShowResults);
            tvShowAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

            // statement ini membuat list yang ditampilkan oleh recyclerview dapat di access / diklik
            ItemClickSupport.addTo(rvTvShow).setOnItemClickListener((recyclerView, position, v) -> {
                Uri uri = Uri.parse(CONTENT_URI_TV + "/" + tvShowResults.get(position).getId());
                Intent intent = new Intent(recyclerView.getContext(), DetailTvShowActivity.class);
                intent.setData(uri);
                intent.putExtra(DetailTvShowActivity.EXTRA_TV, tvShowResults.get(position));
                startActivity(intent);
            });
        }
    };


    private final Observer<List<TvShowResults>> getTvPopularData = new Observer<List<TvShowResults>>() {
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


    private final Observer<List<TvShowResults>> getTvTopData = new Observer<List<TvShowResults>>() {
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


    private final Observer<List<TvShowResults>> getTvOnAir = new Observer<List<TvShowResults>>() {
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


    private final Observer<List<TvShowGenres>> getTvGenre = new Observer<List<TvShowGenres>>() {
        @Override
        public void onChanged(List<TvShowGenres> tvShowGenres) {
            tvShowAdapter.setListTvGenre(tvShowGenres);
            tvShowAdapter.notifyDataSetChanged();
        }
    };


    // method ini untuk melakukan pencarian data
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


    // method ini untuk menghilangkan keyboard setiap kali user melakukan pencarian data
    private void hidekeyboard(SearchView searchView) {
        if (getContext() != null) {
            InputMethodManager methodManager = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
    }
}


