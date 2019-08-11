package com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter;

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
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowGenres;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowSearchAdapter extends RecyclerView.Adapter<TvShowSearchAdapter.TvViewHolder> {
    private Context context;
    private List<TvShowResults> listTv;
    private List<TvShowGenres> listGenreTv;

    public TvShowSearchAdapter(Context context) {
        this.context = context;
    }

    public void setListTv(List<TvShowResults> listTv) {
        this.listTv = listTv;
    }

    public void setListGenreTv(List<TvShowGenres> listGenreTv) {
        this.listGenreTv = listGenreTv;
    }

    @NonNull
    @Override
    public TvShowSearchAdapter.TvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_movie, parent, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowSearchAdapter.TvViewHolder holder, int position) {
        holder.txtTitle.setText(listTv.get(position).getName());
        holder.txtRate.setText(String.valueOf(listTv.get(position).getVoteAverage()));
        holder.txtDate.setText(listTv.get(position).getFirstAirDate());

        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listTv.get(position).getPosterPath();
        Glide.with(context)
                .load(urlPhoto)
                .apply(new RequestOptions())
                .into(holder.imgPhoto);

        if (listGenreTv != null) {
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
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_rate)
        TextView txtRate;
        @BindView(R.id.txt_genre)
        TextView txtGenre;
        @BindView(R.id.img_photo)
        ImageView imgPhoto;

        TvViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private String getGenre(List<Integer> genreIds) {
            List<String> tvGenre = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (TvShowGenres genres : listGenreTv) {
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
