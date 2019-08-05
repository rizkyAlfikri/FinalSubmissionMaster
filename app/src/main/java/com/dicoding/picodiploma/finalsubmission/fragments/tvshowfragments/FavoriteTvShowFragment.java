package com.dicoding.picodiploma.finalsubmission.fragments.tvshowfragments;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dicoding.picodiploma.finalsubmission.LoadCallback;
import com.dicoding.picodiploma.finalsubmission.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvShowFragment extends Fragment implements LoadCallback {


    public FavoriteTvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor cursor) {

    }
}
