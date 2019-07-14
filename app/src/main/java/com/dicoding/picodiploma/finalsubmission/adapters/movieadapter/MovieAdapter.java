package com.dicoding.picodiploma.finalsubmission.adapters.movieadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieGenres;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.utils.Config;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<MovieResults> listMovie;
    private List<MovieGenres> listMovieGenre;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public void setListMovie(List<MovieResults> listMovie) {
        this.listMovie = listMovie;
        notifyDataSetChanged();
    }

    public void setListGenreMovie(List<MovieGenres> listGenreMovie) {
        this.listMovieGenre = listGenreMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.txtTitle.setText(listMovie.get(position).getTitle());
        holder.txtGenre.setText(String.valueOf(holder.getGenres(listMovie.get(position).getGenreIds())));
        holder.txtRate.setText(String.valueOf(listMovie.get(position).getVoteAverage()));
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listMovie.get(position).getPosterPath();
        Glide.with(context)
                .load(urlPhoto)
                .apply(new RequestOptions().override(100, 140))
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        if (listMovie != null) {
            return listMovie.size();
        } else {
            return 0;
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_genre)
        TextView txtGenre;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_rate)
        TextView txtRate;
        @BindView(R.id.img_photo)
        ImageView imgPhoto;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (MovieGenres genre : listMovieGenre) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }
    }
}