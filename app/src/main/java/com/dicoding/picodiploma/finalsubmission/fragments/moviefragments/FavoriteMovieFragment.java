package com.dicoding.picodiploma.finalsubmission.fragments.moviefragments;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.LoadCallback;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.adapters.movieadapter.MovieFavoriteAdapter;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_MOVIE;
import static com.dicoding.picodiploma.finalsubmission.utils.MappingHelper.mapCursorToArrayListMovie;


public class FavoriteMovieFragment extends Fragment implements LoadCallback {
    private static final String EXTRA_STATE = "extra_state";
    private MovieFavoriteAdapter favoriteAdapter;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindString(R.string.snackbar_delete)
    String snackbarDelete;
    @BindString(R.string.undo)
    String undo;

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
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, view.getContext());

        if (getContext() != null) {
            getContext().getContentResolver()
                    .registerContentObserver(CONTENT_URI_MOVIE, true, myObserver);
        }

        favoriteAdapter = new MovieFavoriteAdapter(getActivity());
        rvMovie.setAdapter(favoriteAdapter);

        if (savedInstanceState == null) {
            new LoadMovieAsynTask(getContext(), this).execute();
        } else {
            ArrayList<MovieResults> movieResults = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (movieResults != null) {
                favoriteAdapter.setListMovie(movieResults);
            }
        }


//        ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
//            Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + listMovie.get(position).getId());
//            Intent intent = new Intent(recyclerView.getContext(), DetailMovieActivity.class);
//            intent.setData(uri);
//            intent.putExtra(DetailMovieActivity.EXTRA_POSITION, position);
//            startActivityForResult(intent, REQUEST_MOVIE_UPDATE);
//        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_MOVIE_UPDATE) {
//            if (resultCode == RESULT_MOVIE_DELETE) {
//                int position = data.getIntExtra(DetailMovieActivity.EXTRA_POSITION, 0);
//                favoriteAdapter.removeItem(position);
//                Toast.makeText(getContext(), getString(R.string.delete_item), Toast.LENGTH_SHORT).show();
//            } else if (resultCode == RESULT_MOVIE_ADD) {
//                MovieResults movieResults = data.getParcelableExtra(EXTRA_MOVIE);
//                favoriteAdapter.addItem(movieResults);
//            }
//        } else {
//            int position = data.getIntExtra(DetailMovieActivity.EXTRA_POSITION, 0);
//            favoriteAdapter.removeItem(position);
//            Toast.makeText(getContext(),    "delete", Toast.LENGTH_SHORT).show();
//        }
//    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.getListMovie());
    }

    @Override
    public void preExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(()
                    -> progressBar.setVisibility(View.VISIBLE));
        }
    }

    @Override
    public void postExecute(Cursor cursor) {
        progressBar.setVisibility(View.GONE);
        ArrayList<MovieResults> listMovie = mapCursorToArrayListMovie(cursor);
        if (listMovie.size() > 0) {
            favoriteAdapter.setListMovie(listMovie);

        } else {
            favoriteAdapter.setListMovie(new ArrayList<>());
            showToastMessage();
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
            return context.getContentResolver().query(CONTENT_URI_MOVIE,
                    null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    private void showToastMessage() {
        Toast.makeText(rvMovie.getContext(), getString(R.string.no_data), Toast.LENGTH_LONG).show();
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
