package com.dicoding.picodiploma.finalsubmission.fragments.tvshowfragments;


import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter.TvShowAdapter;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.viewmodels.TvShowViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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

        rvTvShow.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        rvTvShow.setHasFixedSize(true);
        tvShowAdapter = new TvShowAdapter(getContext());
        rvTvShow.setAdapter(tvShowAdapter);
        progressBar.setVisibility(View.VISIBLE);


        TvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getTvFromRetrofit().observe(this, getTvData);
        tvShowViewModel.getTvGenre().observe(this, getTvGenre);
    }

    private final Observer<List<TvShowResults>> getTvData = new Observer<List<TvShowResults>>() {
        @Override
        public void onChanged(List<TvShowResults> tvShowResults) {
            tvShowAdapter.setListTv(tvShowResults);
            tvShowAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    };

    private final Observer<List<TvShowGenres>> getTvGenre = new Observer<List<TvShowGenres>>() {
        @Override
        public void onChanged(List<TvShowGenres> tvShowGenres) {
            tvShowAdapter.setListTvGenre(tvShowGenres);
            tvShowAdapter.notifyDataSetChanged();
        }
    };

}
