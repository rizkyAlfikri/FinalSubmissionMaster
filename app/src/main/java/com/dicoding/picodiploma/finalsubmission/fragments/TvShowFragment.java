package com.dicoding.picodiploma.finalsubmission.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieDetail;
import com.dicoding.picodiploma.finalsubmission.utils.Config;
import com.dicoding.picodiploma.finalsubmission.viewmodels.MovieViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    TextView textView;

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.txt_test);

        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieDetail(666, Config.API_KEY).observe(this, getdetailData);

    }

    private final Observer<MovieDetail> getdetailData = new Observer<MovieDetail>() {
        @Override
        public void onChanged(MovieDetail movieDetail) {
            if (movieDetail != null) {
                textView.setText(movieDetail.getTitle());
            } else {
                Log.d("Failure Detail3", "No data");
            }
        }
    };
}
