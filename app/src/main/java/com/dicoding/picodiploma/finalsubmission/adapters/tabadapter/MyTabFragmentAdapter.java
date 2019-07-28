package com.dicoding.picodiploma.finalsubmission.adapters.tabadapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.fragments.FavoriteMovieFragment;
import com.dicoding.picodiploma.finalsubmission.fragments.FavoriteTvShowFragment;

public class MyTabFragmentAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLE = new int[]{R.string.movie, R.string.tv_shows};
    private final Context context;

    public MyTabFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FavoriteMovieFragment();
            case 1:
                return new FavoriteTvShowFragment();
        }
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLE[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
