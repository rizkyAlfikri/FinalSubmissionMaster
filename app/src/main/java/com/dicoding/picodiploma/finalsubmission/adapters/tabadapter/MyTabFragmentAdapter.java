package com.dicoding.picodiploma.finalsubmission.adapters.tabadapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.fragments.moviefragments.FavoriteMovieFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.tvshowfragments.FavoriteTvShowFragment;

public class MyTabFragmentAdapter extends FragmentPagerAdapter {
    private final Context context;

    // menentukan judul untuk TabFragment
    @StringRes
    private static final int[] TAB_TITLE = new int[]{R.string.movie, R.string.tv_shows};


    public MyTabFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // jika position di angka 0, maka akan menampilkan FavoriteMovieFragment
                return new FavoriteMovieFragment();
            case 1:
                // jika position di angka 1, maka akan menampilkan FavoriteTvShowFragment
                return new FavoriteTvShowFragment();
        }
        return PlaceholderFragment.newInstance(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // menentukan judul tab berdasarkan position tab
        return context.getResources().getString(TAB_TITLE[position]);
    }

    @Override
    public int getCount() {
        // method ini berfungsi untuk menentukan jumlah data / fragment yang akan di tampilkan
        return 2;
    }
}
