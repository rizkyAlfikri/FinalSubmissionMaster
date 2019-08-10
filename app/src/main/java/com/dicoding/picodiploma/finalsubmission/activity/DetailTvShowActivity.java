package com.dicoding.picodiploma.finalsubmission.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
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
import com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter.TvShowReviewAdapter;
import com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter.TvShowTrailerAdapter;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowDetail;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowReview;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowTrailer;
import com.dicoding.picodiploma.finalsubmission.utils.Config;
import com.dicoding.picodiploma.finalsubmission.utils.ItemClickSupport;
import com.dicoding.picodiploma.finalsubmission.viewmodels.TvShowViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_TV;
import static com.dicoding.picodiploma.finalsubmission.fragments.tvshowfragments.FavoriteTvShowFragment.RESULT_TV_ADD;
import static com.dicoding.picodiploma.finalsubmission.fragments.tvshowfragments.FavoriteTvShowFragment.RESULT_TV_DELETE;
import static com.dicoding.picodiploma.finalsubmission.utils.ContentValueHelper.getContentValueTv;

public class DetailTvShowActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_TV = "extra_tv";
    public static final String EXTRA_POSITION = "extra_position";
    private TvShowResults tvShowResults;
    private TvShowTrailerAdapter trailerAdapter;
    private TvShowReviewAdapter reviewAdapter;
    int tvId;
    private StringBuilder genreBuilder = new StringBuilder();
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
    @BindView(R.id.txt_language)
    TextView txtLanguage;
    @BindView(R.id.txt_rate)
    TextView txtRate;
    @BindView(R.id.txt_overview)
    TextView txtOverview;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.rv_movie_video)
    RecyclerView rvTvVideo;
    @BindView(R.id.rv_movie_review)
    RecyclerView rvTvReview;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_bar2)
    ProgressBar progressBar2;
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
    @BindView(R.id.txt_num_season)
    TextView txtSeason;
    @BindView(R.id.txt_num_episode)
    TextView txtEpisode;
    @BindView(R.id.linear_movie_trailer)
    FrameLayout frameTvTrailer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri,
                    null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) tvShowResults = new TvShowResults(cursor);
                cursor.close();
            }
        }

        if (tvShowResults != null) {
            isFavorite = true;
        } else {
            isFavorite = false;
            tvShowResults = getIntent().getParcelableExtra(EXTRA_TV);
        }

        init();

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

        TvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getTvDetail(tvId).observe(this, getTvDetailData);
        tvShowViewModel.getTvTrailer(tvId).observe(this, getTvTrailerData);
        tvShowViewModel.getTvReview(tvId).observe(this, getTvReviewData);
        imgFavorite.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_favorite) {
            tvShowResults.setGenre(genreBuilder.toString());
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TV, tvShowResults);
            intent.putExtra(EXTRA_POSITION, position);

            if (!isFavorite) {
                Glide.with(this)
                        .load(R.drawable.ic_favorite_black_24dp)
                        .apply(new RequestOptions().override(36, 36))
                        .into(imgFavorite);
                ContentValues values = getContentValueTv(tvShowResults);
                getContentResolver().insert(CONTENT_URI_TV, values);
                Toast.makeText(this, tvShowResults.getName() + getString(R.string.add_favorite),
                        Toast.LENGTH_SHORT).show();

                isFavorite = true;
                setResult(RESULT_TV_ADD, intent);

            } else {
                Glide.with(this)
                        .load(R.drawable.ic_favorite_border_black_24dp)
                        .apply(new RequestOptions().override(36, 36))
                        .into(imgFavorite);

                getContentResolver().delete(uri, null, null);
                Toast.makeText(this, tvShowResults.getName() + getString(R.string.delete_favorite),
                        Toast.LENGTH_SHORT).show();
                isFavorite = false;
                setResult(RESULT_TV_DELETE, intent);

            }
        }
    }


    private final Observer<TvShowDetail> getTvDetailData = this::loadTvShowDetail;


    private final Observer<List<TvShowTrailer>> getTvTrailerData = new Observer<List<TvShowTrailer>>() {
        @Override
        public void onChanged(List<TvShowTrailer> tvShowTrailers) {
            trailerAdapter.setListTvTrailer(tvShowTrailers);
            trailerAdapter.notifyDataSetChanged();
            progressBar2.setVisibility(View.GONE);
            openYoutubeTvTailer(tvShowTrailers.get(0).getKey());

            ItemClickSupport.addTo(rvTvVideo).setOnItemClickListener((recyclerView, position, v)
                    -> openYoutubeTvVideo(tvShowTrailers.get(position).getKey()));
        }
    };


    private final Observer<List<TvShowReview>> getTvReviewData = new Observer<List<TvShowReview>>() {
        @Override
        public void onChanged(List<TvShowReview> tvShowReviews) {
            reviewAdapter.setListTv(tvShowReviews);
            reviewAdapter.notifyDataSetChanged();
        }
    };


    private void init() {
        tvId = tvShowResults.getId();

        rvTvVideo.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL,
                false));
        rvTvVideo.setHasFixedSize(true);
        trailerAdapter = new TvShowTrailerAdapter(this);
        rvTvVideo.setAdapter(trailerAdapter);

        rvTvReview.setLayoutManager(new LinearLayoutManager(this));
        rvTvReview.setHasFixedSize(true);
        reviewAdapter = new TvShowReviewAdapter(this);
        rvTvReview.setAdapter(reviewAdapter);
    }


    private void loadTvShowDetail(TvShowDetail tvShowDetail) {
        txtTitle.setText(tvShowDetail.getName());
        txtDate.setText(tvShowDetail.getFirstAirDate());
        txtRate.setText(String.valueOf(tvShowDetail.getVoteAverage()));
        txtLanguage.setText(tvShowDetail.getOriginalLanguage());
        txtOverview.setText(tvShowDetail.getOverview());
        float rating = (float) tvShowDetail.getVoteAverage() / 2;
        ratingBar.setRating(rating);
        txtStatus.setText(tvShowDetail.getStatus());
        txtPopular.setText(String.valueOf(tvShowDetail.getPopularity()));
        txtVoteCount.setText(String.valueOf(tvShowDetail.getVoteCount()));
        txtRating.setText(String.valueOf(tvShowDetail.getVoteAverage()));
        txtHomePage.setText(tvShowDetail.getHomepage());
        txtSeason.setText(String.valueOf(tvShowDetail.getNumberOfSeasons()));
        txtEpisode.setText(String.valueOf(tvShowDetail.getNumberOfEpisodes()));

        for (int i = 0; i < tvShowDetail.getGenres().size(); i++) {
            TvShowGenres tvShowGenres = tvShowDetail.getGenres().get(i);
            if (i < tvShowDetail.getGenres().size() - 1) {
                txtGenre.append(tvShowGenres.getName() + " ,");
                genreBuilder.append(tvShowGenres.getName()).append(", ");
            } else {
                txtGenre.append(tvShowGenres.getName());
                genreBuilder.append(tvShowGenres.getName());
            }
        }

        String urlPhoto = Config.IMAGE_URL_BASE_PATH + tvShowDetail.getPosterPath();
        Glide.with(this)
                .load(urlPhoto)
                .apply(new RequestOptions().override(132, 180))
                .into(imgPhoto);

        progressBar.setVisibility(View.GONE);
    }


    private void openYoutubeTvTailer(String keyTvTrailer) {
        String urlImage = "https://img.youtube.com/vi/" + keyTvTrailer + "/hqdefault.jpg";
        Glide.with(getApplicationContext())
                .load(urlImage)
                .apply(new RequestOptions())
                .into(imgTrailer);

        View view = LayoutInflater.from(this)
                .inflate(R.layout.layout_tv_trailer, frameTvTrailer, false);

        Intent appTrailerIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + keyTvTrailer));
        Intent webTrailerIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + keyTvTrailer));

        view.setOnClickListener(v -> {
            try {
                startActivity(appTrailerIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webTrailerIntent);
            }
        });
        frameTvTrailer.addView(view);
    }


    private void openYoutubeTvVideo(String keyTvVideo) {
        Intent appVideoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + keyTvVideo));
        Intent webVideoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + keyTvVideo));
        try {
            startActivity(appVideoIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webVideoIntent);
        }
    }

}
