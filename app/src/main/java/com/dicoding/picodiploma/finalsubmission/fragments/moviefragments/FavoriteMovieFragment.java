package com.dicoding.picodiploma.finalsubmission.fragments.moviefragments;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.utils.LoadCallback;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.activity.DetailMovieActivity;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieFavoriteAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.utils.ItemClickSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.activity.DetailMovieActivity.EXTRA_MOVIE;
import static com.dicoding.picodiploma.finalsubmission.activity.DetailMovieActivity.REQUEST_MOVIE_UPDATE;
import static com.dicoding.picodiploma.finalsubmission.activity.DetailMovieActivity.RESULT_MOVIE_ADD;
import static com.dicoding.picodiploma.finalsubmission.activity.DetailMovieActivity.RESULT_MOVIE_DELETE;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_MOVIE;
import static com.dicoding.picodiploma.finalsubmission.utils.MappingHelper.mapCursorToArrayListMovie;


public class FavoriteMovieFragment extends Fragment implements LoadCallback {
    private static final String EXTRA_STATE = "extra_state";
    private MovieFavoriteAdapter favoriteAdapter;
    private ArrayList<MovieResults> listMovie;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.img_no_data)
    ImageView imgNoData;


    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // method ini berfungsi untuk inisialisasi Adapter, Recyclerview
        init();

        // statement ini berfungsi supaya user dapat mengakses setiap data
        // ketika data telah dipilih maka detail activity dari data yang dipilih akan ditampilkan
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
            Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + listMovie.get(position).getId());
            Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
            intent.setData(uri);
            intent.putExtra(EXTRA_MOVIE, listMovie.get(position));
            intent.putExtra(DetailMovieActivity.EXTRA_POSITION, position);
            startActivityForResult(intent, REQUEST_MOVIE_UPDATE);
        });

        if (savedInstanceState == null) {
            new LoadMovieAsynTask(getContext(), this).execute();
        } else {
            listMovie = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // method ini digunakan untuk notifikasi ke adapter setiap kali ada penambahan atau pengurangan data
        if (requestCode == REQUEST_MOVIE_UPDATE) {
            if (resultCode == RESULT_MOVIE_DELETE) {
                int position = data.getIntExtra(DetailMovieActivity.EXTRA_POSITION, 0);
                favoriteAdapter.removeItem(position);
                Toast.makeText(getContext(), getString(R.string.delete_item), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_MOVIE_ADD) {
                MovieResults movieResults = data.getParcelableExtra(EXTRA_MOVIE);
                favoriteAdapter.addItem(movieResults);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // method ini akan dijalankan ketika fragment berada di state onResume
        // statement ini akan menjalankan query ke database
        new LoadMovieAsynTask(getContext(), this).execute();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, listMovie);
    }

    @Override
    public void preExecute() {
        // method ini akan menjalankan progress bar, sebagai penanda bahwa data sedang di load dari database
        if (getActivity() != null) {
            getActivity().runOnUiThread(()
                    -> progressBar.setVisibility(View.VISIBLE));
        }
    }

    @Override
    public void postExecute(Cursor cursor) {
        // method ini merupakan hasil output dari kelas LoadTvAsynTask
        // statement dibawah ini berfungsi untuk memproses data yang telah di query dari database
        // lalu datanya akan ditampilkan melalui adapter
        progressBar.setVisibility(View.GONE);
        listMovie = mapCursorToArrayListMovie(cursor);
        if (listMovie.size() > 0) {
            favoriteAdapter.setListMovie(listMovie);
            imgNoData.setVisibility(View.GONE);
        } else {
            imgNoData.setVisibility(View.VISIBLE);
        }
    }

    // method ini berisi inisialisasi RecyclerView, HandlerThread dan Adapter
    private void init() {
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setHasFixedSize(true);
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext());

        if (getContext() != null) {
            getContext().getContentResolver()
                    .registerContentObserver(CONTENT_URI_MOVIE, true, myObserver);
        }

        favoriteAdapter = new MovieFavoriteAdapter(getActivity());
        rvMovie.setAdapter(favoriteAdapter);
    }

    // class ini bertugas untuk melakukan proses query data ke database dengan menggunakan worker thread
    private static class LoadMovieAsynTask extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallback> weakCallback;

        LoadMovieAsynTask(Context context, LoadCallback loadCallback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(loadCallback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI_MOVIE,
                    null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    // kelas ini unuk mengobservasi perubahan data dari database yang sedang ditampilkan
    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieAsynTask(context, (LoadCallback) context).execute();
        }
    }
}
