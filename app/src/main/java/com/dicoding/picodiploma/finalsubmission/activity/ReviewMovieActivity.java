package com.dicoding.picodiploma.finalsubmission.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.adapters.MovieReviewAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReview;
import com.dicoding.picodiploma.finalsubmission.utils.Config;
import com.dicoding.picodiploma.finalsubmission.viewmodels.MovieViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewMovieActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "extra_movieid";
    private MovieReviewAdapter reviewAdapter;
    @BindView(R.id.rv_movie_review)
    RecyclerView rvMovieReview;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_movie);
        ButterKnife.bind(this);
        int movieid = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
        init();

        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieReview(movieid, Config.API_KEY).observe(this, getMovieReviewData);

    }

    private void init() {
        progressBar.setVisibility(View.VISIBLE);
        rvMovieReview.setLayoutManager(new LinearLayoutManager(this));
        rvMovieReview.setHasFixedSize(true);
        reviewAdapter = new MovieReviewAdapter(this, true);
        rvMovieReview.setAdapter(reviewAdapter);
    }

    private final Observer<List<MovieReview>> getMovieReviewData = new Observer<List<MovieReview>>() {
        @Override
        public void onChanged(List<MovieReview> movieReviewList) {
            reviewAdapter.setListMovie(movieReviewList);
            reviewAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    };
}
