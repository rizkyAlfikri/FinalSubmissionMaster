package com.dicoding.picodiploma.finalsubmission.detailactivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.utils.Config;
import com.dicoding.picodiploma.finalsubmission.widget.MovieFavoriteWidget;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import butterknife.BindString;
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
    private Uri uri;
    private int position;
    private boolean isFavorite = false;

    @BindView(R.id.txt_overview)
    TextView txtOverview;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_language)
    TextView txtLanguage;
    @BindView(R.id.txt_vote_average)
    TextView txtVoteAverage;
    @BindView(R.id.txt_vote_count)
    TextView txtVoteCount;
    @BindView(R.id.txt_favorite)
    TextView txtFavorite;
    @BindView(R.id.img_favorite)
    ImageView imgFavorite;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindString(R.string.favorite)
    String favorite;
    @BindString(R.string.unfavorite)
    String unfavorite;
    @BindString(R.string.add_favorite)
    String addFavorite;
    @BindString(R.string.delete_favorite)
    String deleteFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

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

        txtOverview.setText(movieResults.getOverview());
        txtDate.setText(movieResults.getReleaseDate());
        txtLanguage.setText(movieResults.getOriginalLanguage());
        txtVoteAverage.setText(String.valueOf(movieResults.getVoteAverage()));
        txtVoteCount.setText(String.valueOf(movieResults.getVoteCount()));
        collapsingToolbarLayout.setTitle(movieResults.getTitle());
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + movieResults.getPosterPath();
        Glide.with(this)
                .load(urlPhoto)
                .apply(new RequestOptions())
                .into(imgPhoto);



        if (isFavorite) {
            Glide.with(this)
                    .load(R.drawable.ic_favorite_black_24dp)
                    .apply(new RequestOptions().override(36, 36))
                    .into(imgFavorite);
            txtFavorite.setText(unfavorite);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_favorite_border_black_24dp)
                    .apply(new RequestOptions().override(36, 36))
                    .into(imgFavorite);
            txtFavorite.setText(favorite);
        }

        imgFavorite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MOVIE, movieResults);
        intent.putExtra(EXTRA_MOVIE, position);
        if (!isFavorite) {
            Glide.with(this)
                    .load(R.drawable.ic_favorite_black_24dp)
                    .apply(new RequestOptions().override(36, 36))
                    .into(imgFavorite);
            txtFavorite.setText(unfavorite);
            ContentValues values = getContentValue(movieResults);
            getContentResolver().insert(CONTENT_URI, values);
            Toast.makeText(this, movieResults.getTitle() + " " + addFavorite, Toast.LENGTH_SHORT).show();
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
            txtFavorite.setText(favorite);
            getContentResolver().delete(uri, null, null);
            Toast.makeText(this, movieResults.getTitle() + " " + deleteFavorite, Toast.LENGTH_SHORT).show();
            isFavorite = false;
            setResult(RESULT_DELETE, intent);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            ComponentName thisWidget = new ComponentName(getApplicationContext(), MovieFavoriteWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        }
    }

}
