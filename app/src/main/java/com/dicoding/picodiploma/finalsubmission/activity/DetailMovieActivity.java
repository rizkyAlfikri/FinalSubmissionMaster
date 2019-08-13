package com.dicoding.picodiploma.finalsubmission.activity;

import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
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
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieReviewAdapter;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieTrailerAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieDetail;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReview;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieTrailer;
import com.dicoding.picodiploma.finalsubmission.utils.Config;
import com.dicoding.picodiploma.finalsubmission.utils.ItemClickSupport;
import com.dicoding.picodiploma.finalsubmission.viewmodels.MovieViewModel;
import com.dicoding.picodiploma.finalsubmission.widget.MovieFavoriteWidget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_MOVIE;
import static com.dicoding.picodiploma.finalsubmission.utils.ContentValueHelper.getContentValueMovie;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_MOVIE_UPDATE = 101;
    public static final int RESULT_MOVIE_ADD = 301;
    public static final int RESULT_MOVIE_DELETE = 201;
    private MovieResults movieResults;
    private MovieTrailerAdapter trailerAdapter;
    private MovieReviewAdapter reviewAdapter;
    int movieId;
    private StringBuilder genreBuilder = new StringBuilder();
    private Uri uri;
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
    @BindView(R.id.txt_tagline)
    TextView txtTagline;
    @BindView(R.id.linear_movie_trailer)
    FrameLayout frameMovieTrailer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        // uri ini menangkap data yang dikirim melalui objek intent dari MovieFragment
        uri = getIntent().getData();

        // cek apakah movie sudah ada di database atau belum, jika sudah ada ambil data yang sesuai yang ada di database
        // lalu masukan ken movieResult
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri,
                    null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movieResults = new MovieResults(cursor);
                cursor.close();
            }
        }

        // jika belum ada ambil data dari objek intent yang sudah dikirim sebelumnya oleh MovieFragment
        // variabel isFavorite digunakan sebagai flag apakah data sudah ada atau belum di database
        if (movieResults != null) {
            isFavorite = true;
        } else {
            isFavorite = false;
            movieResults = getIntent().getParcelableExtra(EXTRA_MOVIE);
        }

        // inisialisasi recycvleview, adapter, dan viewmodel
        init();

        // icon favorite ini berfungsi sebagai petanda apakah data sudah ada atau belum di database
        if (isFavorite) {
            // jika data sebelumnnya sudah ada di database, maka icon favorite akan  seperti dibawah ini
            Glide.with(this)
                    .load(R.drawable.ic_favorite_black_24dp)
                    .apply(new RequestOptions().override(36, 36))
                    .into(imgFavorite);
        } else {
            // jika data sebelumnnya tidak ada di database maka  icon favorite akan seperti dibawah ini
            Glide.with(this)
                    .load(R.drawable.ic_favorite_border_black_24dp)
                    .apply(new RequestOptions().override(36, 36))
                    .into(imgFavorite);
        }

        imgFavorite.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_favorite) {
            // menyimpan data position yang dikirim dari MovieFragment, dimana nantinya data ini akan dikirim ke FavoriteMovie
            // menset data genre ke object movieResult
            int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            movieResults.setGenre(genreBuilder.toString());

            // memasukan data movieResult dan  position kedalam object intent
            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOVIE, movieResults);
            intent.putExtra(EXTRA_POSITION, position);

            if (!isFavorite) {
                // jika  data belum ada di database, maka akan menjalankan program dibawah ini
                Glide.with(this)
                        .load(R.drawable.ic_favorite_black_24dp)
                        .apply(new RequestOptions().override(36, 36))
                        .into(imgFavorite);

                // object movieResult akan di konversi menjadi objet Content Value,
                // object content value inilah yang nantinya akan dimasukan ke database
                ContentValues values = getContentValueMovie(movieResults);

                // memasukan object Content value ke database
                getContentResolver().insert(CONTENT_URI_MOVIE, values);
                Toast.makeText(this, movieResults.getTitle() + " " +
                        getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
                isFavorite = true;

                // Intent yang telah dibuat tadi dikirim ke Favorite Movie dengan Flag Movie RESULT_MOVIE_ADD
                // statement ini berfungsi untuk animasi penambahan data di recyclerview
                setResult(RESULT_MOVIE_ADD, intent);

                // statement ini berfungsi untuk melakukan update otomatis pada widget jika ada penambahan data baru
                autoUpdateWidget();

            } else {
                // jika  data belum ada di database, maka akan menjalankan program dibawah ini
                Glide.with(this)
                        .load(R.drawable.ic_favorite_border_black_24dp)
                        .apply(new RequestOptions().override(36, 36))
                        .into(imgFavorite);

                // mendelete data movie yang telah tersimpan di database
                getContentResolver().delete(uri, null, null);
                Toast.makeText(this, movieResults.getTitle() + " " +
                        getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show();
                isFavorite = false;

                // Intent yang telah dibuat tadi dikirim ke Favorite Movie dengan Flag Movie RESULT_MOVIE_DELETE
                // statement ini berfungsi untuk animasi penghapusan data di recyclerview
                setResult(RESULT_MOVIE_DELETE, intent);

                // statement ini berfungsi untuk melakukan update otomatis pada widget jika ada penghapusan data
                autoUpdateWidget();
            }
        }
    }

    // statement ini berfungsi untuk menangkap data movie detail dari webservice movieDb,
    // data yang telah di tangkap dijadikan parameter oleh method loadMovieDetail
    private final Observer<MovieDetail> getMovieDetailData = this::loadMovieDetail;

    // statement ini berfungsi untuk menangkap data movie trailer dari webservice movieDb,
    private final Observer<List<MovieTrailer>> getMovieTrailerData = new Observer<List<MovieTrailer>>() {
        @Override
        public void onChanged(List<MovieTrailer> movieTrailers) {
            // load data movieTrailer ke adapter movieTrailer
            trailerAdapter.setListMovie(movieTrailers);
            trailerAdapter.notifyDataSetChanged();
            progressBar2.setVisibility(View.GONE);

            // method ini berfungsi untuk mengload video trailer
            openYoutubeTrailer(movieTrailers.get(0).getKey());

            // statement ini juga sama untuk mengload video trailer
            ItemClickSupport.addTo(rvMovieVideo).setOnItemClickListener((recyclerView, position, v)
                    -> openYoutubeMovieVideo(movieTrailers.get(position).getKey()));
        }
    };

    // statement ini berfungsi untuk menangkap data movie review dari webservice movieDb,
    // data yang telah di tangkap akan di masukan ke review movie adapter yang nantinya akan di tampilkan
    private final Observer<List<MovieReview>> getMovieReviewData = new Observer<List<MovieReview>>() {
        @Override
        public void onChanged(List<MovieReview> movieReviews) {
            reviewAdapter.setListMovie(movieReviews);
            reviewAdapter.notifyDataSetChanged();
        }
    };

    // inisialisasi RecyclerView, Adapter, dan MovieViewModel
    private void init() {
        movieId = movieResults.getId();
        rvMovieVideo.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL,
                false));

        rvMovieVideo.setHasFixedSize(true);
        trailerAdapter = new MovieTrailerAdapter(this);
        rvMovieVideo.setAdapter(trailerAdapter);

        rvMovieReview.setLayoutManager(new LinearLayoutManager(this));
        rvMovieReview.setHasFixedSize(true);
        reviewAdapter = new MovieReviewAdapter(this);
        rvMovieReview.setAdapter(reviewAdapter);


        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieDetail(movieId).observe(this, getMovieDetailData);
        movieViewModel.getMovieTrailer(movieId).observe(this, getMovieTrailerData);
        movieViewModel.getMovieReview(movieId).observe(this, getMovieReviewData);
    }

    // method ini berfungsi mengload / menampilkan data movie detail
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

        // looping ini berfungsi untuk membuat data movie genre dan menampilkannya
        // data movie genre yang telah dibuat, satu persatu dimasukan kedalam StringBuilder
        // lalu StringBuilder tersebut ditampilkan melalui txtGenre
        for (int i = 0; i < movieDetailData.getGenres().size(); i++) {
            MovieGenres movieGenres = movieDetailData.getGenres().get(i);
            if (i < movieDetailData.getGenres().size() - 1) {
                txtGenre.append(movieGenres.getName() + ", ");
                genreBuilder.append(movieGenres.getName()).append(", ");
            } else {
                txtGenre.append(movieGenres.getName());
                genreBuilder.append(movieGenres.getName());
            }
        }

        // mengload data image
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + movieDetailData.getPosterPath();
        Glide.with(getApplicationContext())
                .load(urlPhoto)
                .apply(new RequestOptions().override(132, 180))
                .into(imgPhoto);
        progressBar.setVisibility(View.GONE);
    }

    // method ini berfungsi ketika user mengklik image trailer, maka secara otomatis app menjalankan Youtube
    // untuk menayangkan video trailer
    private void openYoutubeMovieVideo(String keyMovie) {
        Intent appVideoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + keyMovie));

        Intent webVideoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + keyMovie));
        try {
            startActivity(appVideoIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webVideoIntent);
        }
    }

    // method ini juga berfungsi ketika user mengklik image trailer, maka secara otomatis app menjalankan Youtube
    // untuk menampilkan video trailer
    public void openYoutubeTrailer(String keyMovie) {
        String urlImage = "https://img.youtube.com/vi/" + keyMovie + "/hqdefault.jpg";
        Glide.with(getApplicationContext())
                .load(urlImage)
                .apply(new RequestOptions())
                .into(imgTrailer);

        View view = LayoutInflater.from(this)
                .inflate(R.layout.layout_movie_trailer, frameMovieTrailer, false);

        Intent appTrailerIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + keyMovie));
        Intent webTrailerIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + keyMovie));

        view.setOnClickListener(v -> {
            try {
                startActivity(appTrailerIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webTrailerIntent);
            }
        });
        frameMovieTrailer.addView(view);
    }

    // method ini berfungsi untuk melakukan pembaruan / update pada widget ketika ada perubahan pada database
    private void autoUpdateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName thisWidget = new ComponentName(getApplicationContext(), MovieFavoriteWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
    }
}
