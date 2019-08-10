package com.dicoding.picodiploma.finalsubmission.fragments.tvshowfragments;


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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.LoadCallback;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.activity.DetailTvShowActivity;
import com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter.TvShowFavoriteAdapter;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.utils.ItemClickSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.picodiploma.finalsubmission.activity.DetailTvShowActivity.EXTRA_TV;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.CONTENT_URI_TV;
import static com.dicoding.picodiploma.finalsubmission.utils.MappingHelper.mapCursorToArrayListTv;


public class FavoriteTvShowFragment extends Fragment implements LoadCallback {
    private TvShowFavoriteAdapter favoriteAdapter;
    private ArrayList<TvShowResults> listTvShow;
    private static final int REQUEST_TV_UPDATE = 102;
    public static final int RESULT_TV_ADD = 301;
    public static final int RESULT_TV_DELETE = 201;
    @BindView(R.id.rv_tv)
    RecyclerView rvTv;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    public FavoriteTvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        rvTv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvTv.setHasFixedSize(true);
        HandlerThread handlerThread = new HandlerThread("Data Observer");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, view.getContext());

        if (getContext() != null) {
            getContext().getContentResolver()
                    .registerContentObserver(CONTENT_URI_TV, true, myObserver);
        }

        favoriteAdapter = new TvShowFavoriteAdapter(view.getContext());
        rvTv.setAdapter(favoriteAdapter);

        new LoadTvAsynTask(getContext(), this).execute();

        ItemClickSupport.addTo(rvTv).setOnItemClickListener((recyclerView, position, v) -> {
            Uri uri = Uri.parse(CONTENT_URI_TV + "/" + listTvShow.get(position).getId());
            Intent intent = new Intent(recyclerView.getContext(), DetailTvShowActivity.class);
            intent.setData(uri);
            intent.putExtra(DetailTvShowActivity.EXTRA_POSITION, position);
            startActivityForResult(intent, REQUEST_TV_UPDATE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TV_UPDATE) {
            if (resultCode == RESULT_TV_DELETE) {
                if (data != null) {
                    int position = data.getIntExtra(DetailTvShowActivity.EXTRA_POSITION, 0);
                    favoriteAdapter.removeItem(position);
                    Toast.makeText(getContext(), getString(R.string.delete_item), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_TV_ADD) {
                if (data != null) {
                    TvShowResults tvShowResults = data.getParcelableExtra(EXTRA_TV);
                    favoriteAdapter.addItem(tvShowResults);
                    rvTv.smoothScrollToPosition(favoriteAdapter.getItemCount() - 1);
                }
            }
        }
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
        if (cursor != null) {
            listTvShow = mapCursorToArrayListTv(cursor);
            if (listTvShow.size() > 0) {
                favoriteAdapter.setListTv(listTvShow);
            } else {
                showToastMessage();
            }
        }
    }

    private static class LoadTvAsynTask extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallback> weakCallback;

        LoadTvAsynTask(Context context, LoadCallback loadCallback) {
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
            return context.getContentResolver().query(CONTENT_URI_TV,
                    null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    private void showToastMessage() {
        Toast.makeText(rvTv.getContext(), getString(R.string.no_data_tv), Toast.LENGTH_SHORT).show();
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
            new LoadTvAsynTask(context, (LoadCallback) context).execute();
        }
    }
}
