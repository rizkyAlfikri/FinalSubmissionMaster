package com.dicoding.picodiploma.finalsubmission.adapters.movieadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
    private List<MovieGenres> listGenreMovie;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public void setListMovie(List<MovieResults> listMovie, List<MovieGenres> listGenreMovie) {
        this.listMovie = listMovie;
        this.listGenreMovie = listGenreMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        holder.txtTitle.setText(listMovie.get(position).getTitle());
        holder.txtRate.setText(String.valueOf(listMovie.get(position).getVoteAverage()));
        float rating = (float) (listMovie.get(position).getVoteAverage() / 2);
        holder.ratingBar.setRating(rating);
        String genre = holder.getGenres(listMovie.get(position).getGenreIds());
        holder.txtGenre.setText(genre);
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listMovie.get(position).getPosterPath();
        Glide.with(context).load(urlPhoto).apply(new RequestOptions()).into(holder.imgPhoto);

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
        @BindView(R.id.txt_rate)
        TextView txtRate;
        @BindView(R.id.img_photo)
        ImageView imgPhoto;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (MovieGenres genre : listGenreMovie) {
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
