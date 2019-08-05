package com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter;

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
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvViewHolder> {
    private Context context;
    private List<TvShowResults> listTv;
    private List<TvShowGenres> listTvGenre;

    public TvShowAdapter(Context context) {
        this.context = context;
    }

    public void setListTv(List<TvShowResults> listTv) {
        this.listTv = listTv;
        notifyDataSetChanged();
    }

    public void setListTvGenre(List<TvShowGenres> listTvGenre) {
        this.listTvGenre = listTvGenre;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowAdapter.TvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowAdapter.TvViewHolder holder, int position) {
        holder.txtTitle.setText(listTv.get(position).getName());
        holder.txtRate.setText(String.valueOf(listTv.get(position).getVoteAverage()));
        float rate = (float) listTv.get(position).getVoteAverage() / 2;
        holder.ratingBar.setRating(rate);

        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listTv.get(position).getPosterPath();
        Glide.with(context).load(urlPhoto).apply(new RequestOptions()).into(holder.imgPhoto);

        if (listTvGenre != null) {
            String genre = holder.getGenre(listTv.get(position).getGenreIds());
            holder.txtGenre.setText(genre);
        }
    }

    @Override
    public int getItemCount() {
        if (listTv != null) {
            return listTv.size();
        } else {
            return 0;
        }
    }

    class TvViewHolder extends RecyclerView.ViewHolder {
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
        TvViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private String getGenre(List<Integer> genreIds) {
            List<String> tvGenre = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (TvShowGenres genres : listTvGenre) {
                    if (genres.getId() == genreId) {
                        tvGenre.add(genres.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", tvGenre);
        }
    }
}
