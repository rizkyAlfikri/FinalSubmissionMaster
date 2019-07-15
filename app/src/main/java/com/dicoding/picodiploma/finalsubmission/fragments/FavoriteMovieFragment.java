package com.dicoding.picodiploma.finalsubmission.fragments;


import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dicoding.picodiploma.finalsubmission.LoadCallback;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieFavoriteAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.CONTENT_URI;
import static com.dicoding.picodiploma.finalsubmission.utils.MappingHelper.mapCursorToArrayList;


public class FavoriteMovieFragment extends Fragment implements LoadCallback {
    private MovieFavoriteAdapter favoriteAdapter;
    private static HandlerThread handlerThread;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

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
        rvMovie.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvMovie.setHasFixedSize(true);

        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, view.getContext());
        getContext().getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        favoriteAdapter = new MovieFavoriteAdapter(view.getContext());
        rvMovie.setAdapter(favoriteAdapter);

        new LoadMovieAsynTask(getContext(), this).execute();
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(()
                -> progressBar.setVisibility(View.VISIBLE));
    }

    @Override
    public void postExecute(Cursor cursor) {
        progressBar.setVisibility(View.GONE);

        ArrayList<MovieResults> listMovie = mapCursorToArrayList(cursor);
        if (listMovie.size() > 0) {
            favoriteAdapter.setListMovie(listMovie);
        } else {
            showSnackbarMessage();
        }
    }

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
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    private void showSnackbarMessage() {
        Snackbar.make(rvMovie, "Tidak ada data saat ini", Snackbar.LENGTH_LONG).show();
    }

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
