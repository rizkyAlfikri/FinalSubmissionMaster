package com.dicoding.picodiploma.finalsubmission.adapters.movieadapter;

import android.content.Context;
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
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieTrailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieHolder> {
    private Context context;
    private List<MovieTrailer> listMovie;

    public MovieTrailerAdapter(Context context) {
        this.context = context;
    }

    public void setListMovie(List<MovieTrailer> listMovie) {
        this.listMovie = listMovie;
    }

    @NonNull
    @Override
    public MovieTrailerAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_trailer, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerAdapter.MovieHolder holder, int position) {
        holder.txtName.setText(listMovie.get(position).getName());
        String urlPhoto = "https://img.youtube.com/vi/" + listMovie.get(position).getKey() + "/mqdefault.jpg";
        Glide.with(context).load(urlPhoto).apply(new RequestOptions()).into(holder.imgTrailer);
    }

    @Override
    public int getItemCount() {
        if (listMovie != null) {
            if (listMovie.size() >= 5) {
                return 5;
            } else {
                return listMovie.size();
            }
        } else {
            return 0;
        }
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_trailer)
        ImageView imgTrailer;
        @BindView(R.id.txt_name)
        TextView txtName;
        MovieHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
