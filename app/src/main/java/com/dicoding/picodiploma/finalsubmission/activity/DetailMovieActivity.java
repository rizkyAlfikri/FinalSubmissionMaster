package com.dicoding.picodiploma.finalsubmission.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.adapters.MovieReviewAdapter;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieTrailerAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieDetail;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReview;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieTrailer;
import com.dicoding.picodiploma.finalsubmission.utils.Config;
import com.dicoding.picodiploma.finalsubmission.viewmodels.MovieViewModel;
import com.dicoding.picodiploma.finalsubmission.widget.MovieFavoriteWidget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.CONTENT_URI;
import static com.dicoding.picodiploma.finalsubmission.fragments.FavoriteMovieFragment.RESULT_ADD;
import static com.dicoding.picodiploma.finalsubmission.fragments.FavoriteMovieFragment.RESULT_DELETE;
import static com.dicoding.picodiploma.finalsubmission.utils.ContentValueHelper.getContentValue;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";
    private MovieResults movieResults;
    private MovieTrailerAdapter trailerAdapter;
    private MovieReviewAdapter reviewAdapter;
    int movieId;
    private Uri uri;
    private int position;
    private boolean isFavorite = false;

    @BindView(R.id.img_trailer)
    ImageView imgTrailer;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.img_favorite)
    ImageView imgFavorite;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_genre)
    TextView txtGenre;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_duration)
    TextView txtDuration;
    @BindView(R.id.txt_rate)
    TextView txtRate;
    @BindView(R.id.txt_overview)
    TextView txtOverview;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.rv_movie_video)
    RecyclerView rvMovieVideo;
    @BindView(R.id.rv_movie_review)
    RecyclerView rvMovieReview;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.txt_popularity)
    TextView txtPopular;
    @BindView(R.id.txt_vote_count)
    TextView txtVoteCount;
    @BindView(R.id.txt_rating)
    TextView txtRating;
    @BindView(R.id.txt_home_page)
    TextView txtHomePage;
    @BindView(R.id.txt_tagline)
    TextView txtTagline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        String apiKey = Config.API_KEY;
        progressBar.setVisibility(View.VISIBLE);
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri,
                    null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movieResults = new MovieResults(cursor);
                cursor.close();
            }
        }

        if (movieResults != null) {
            isFavorite = true;
        } else {
            isFavorite = false;
            movieResults = getIntent().getParcelableExtra(EXTRA_MOVIE);
        }

        movieId = movieResults.getId();
        rvMovieVideo.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,
                false));
        rvMovieVideo.setHasFixedSize(true);
        trailerAdapter = new MovieTrailerAdapter(this);
        rvMovieVideo.setAdapter(trailerAdapter);

        rvMovieReview.setLayoutManager(new LinearLayoutManager(this));
        rvMovieReview.setHasFixedSize(true);
        reviewAdapter = new MovieReviewAdapter(this);
        rvMovieReview.setAdapter(reviewAdapter);


        if (isFavorite) {
            Glide.with(this)
                    .load(R.drawable.ic_favorite_black_24dp)
                    .apply(new RequestOptions().override(36, 36))
                    .into(imgFavorite);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_favorite_border_black_24dp)
                    .apply(new RequestOptions().override(36, 36))
                    .into(imgFavorite);
        }


        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieDetail(movieId, apiKey).observe(this, getMovieDetailData);
        movieViewModel.getMovieTrailer(movieId, apiKey).observe(this, getMovieTrailerData);
        movieViewModel.getMovieReview(movieId, apiKey).observe(this, getMovieReviewData);
        imgFavorite.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_favorite) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOVIE, movieResults);
            intent.putExtra(EXTRA_MOVIE, position);
            if (!isFavorite) {
                Glide.with(this)
                        .load(R.drawable.ic_favorite_black_24dp)
                        .apply(new RequestOptions().override(36, 36))
                        .into(imgFavorite);
                ContentValues values = getContentValue(movieResults);
                getContentResolver().insert(CONTENT_URI, values);
                Toast.makeText(this, movieResults.getTitle() + " " + getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
                isFavorite = true;
                setResult(RESULT_ADD, intent);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                ComponentName thisWidget = new ComponentName(getApplicationContext(), MovieFavoriteWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_favorite_border_black_24dp)
                        .apply(new RequestOptions().override(36, 36))
                        .into(imgFavorite);
                getContentResolver().delete(uri, null, null);
                Toast.makeText(this, movieResults.getTitle() + " " + getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show();
                isFavorite = false;
                setResult(RESULT_DELETE, intent);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                ComponentName thisWidget = new ComponentName(getApplicationContext(), MovieFavoriteWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            }
        }
    }

    private final Observer<MovieDetail> getMovieDetailData = this::loadMovieDetail;

    private final Observer<List<MovieTrailer>> getMovieTrailerData = new Observer<List<MovieTrailer>>() {
        @Override
        public void onChanged(List<MovieTrailer> movieTrailers) {
            String urlImage = "https://img.youtube.com/vi/" + movieTrailers.get(0).getKey() + "/sddefault.jpg";
            Glide.with(getApplicationContext())
                    .load(urlImage)
                    .apply(new RequestOptions().override(200, 120))
                    .into(imgTrailer);
            trailerAdapter.setListMovie(movieTrailers);
        }
    };

    private final Observer<List<MovieReview>> getMovieReviewData = new Observer<List<MovieReview>>() {
        @Override
        public void onChanged(List<MovieReview> movieReviews) {
            reviewAdapter.setListMovie(movieReviews);
            reviewAdapter.notifyDataSetChanged();
        }
    };


    private void loadMovieDetail(MovieDetail movieDetailData) {
        txtTitle.setText(movieDetailData.getTitle());
        txtDate.setText(movieDetailData.getReleaseDate());
        txtRate.setText(String.valueOf(movieDetailData.getVoteAverage()));
        txtDuration.setText(String.valueOf(movieDetailData.getRuntime()));
        txtOverview.setText(movieDetailData.getOverview());
        float rating = (float) movieDetailData.getVoteAverage() / 2;
        ratingBar.setRating(rating);
        txtStatus.setText(movieDetailData.getStatus());
        txtPopular.setText(String.valueOf(movieDetailData.getPopularity()));
        txtVoteCount.setText(String.valueOf(movieDetailData.getVoteCount()));
        txtRating.setText(String.valueOf(movieDetailData.getVoteAverage()));
        txtHomePage.setText(movieDetailData.getHomepage());
        txtTagline.setText(movieDetailData.getTagline());

        for (int i = 0; i < movieDetailData.getGenres().size(); i++) {
            MovieGenres movieGenres = movieDetailData.getGenres().get(i);
            if (i < movieDetailData.getGenres().size() - 1) {
                txtGenre.append(movieGenres.getName() + ", ");
            } else {
                txtGenre.append(movieGenres.getName());
            }
        }
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + movieDetailData.getPosterPath();
        Glide.with(getApplicationContext()).load(urlPhoto).apply(new RequestOptions().override(132, 180)).into(imgPhoto);
        progressBar.setVisibility(View.GONE);
    }


}
