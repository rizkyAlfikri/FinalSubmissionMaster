package com.dicoding.picodiploma.finalsubmission.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.SettingsActivity;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
//        SearchManager searchManager;
//        if (getContext() != null) {
//            searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
//            if (searchManager != null) {
//                SearchView searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
//                if (getActivity() != null) {
//                    searchView.setSearchableInfo(searchManager.getSearchableInfo(
//                            new ComponentName(searchView.getContext(), SearchMovieActivity.class)));
//                }
//                searchView.setQueryHint(getString(R.string.search_hint));
//
//                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        Intent searchMovieIntent = new Intent(getContext(), SearchMovieActivity.class);
//                        searchMovieIntent.putExtra(EXTRA_SEARCH, query);
//                        startActivity(searchMovieIntent);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        return false;
//                    }
//                };
//
//                searchView.setOnQueryTextListener(queryTextListener);
//                super.onCreateOptionsMenu(menu, inflater);
//            }
//        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingIntent = new Intent(getContext(), SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
