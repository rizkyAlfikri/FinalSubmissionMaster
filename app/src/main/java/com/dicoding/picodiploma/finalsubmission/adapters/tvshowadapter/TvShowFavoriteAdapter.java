package com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter;

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
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowResults;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowFavoriteAdapter extends RecyclerView.Adapter<TvShowFavoriteAdapter.TvViewHolder> {
    private Context context;
    private ArrayList<TvShowResults> listTv = new ArrayList<>();

    public TvShowFavoriteAdapter(Context context) {
        this.context = context;
    }

    public void setListTv(ArrayList<TvShowResults> listTv) {
        this.listTv.clear();
        this.listTv.addAll(listTv);
        notifyDataSetChanged();
    }

    public void addItem(TvShowResults tvShowResults) {
        this.listTv.add(tvShowResults);
        notifyItemInserted(listTv.size() - 1);
    }

    public void removeItem(int position) {
        this.listTv.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @NonNull
    @Override
    public TvShowFavoriteAdapter.TvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_movie, parent, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowFavoriteAdapter.TvViewHolder holder, int position) {
        holder.txtTitle.setText(listTv.get(position).getName());
        holder.txtDate.setText(listTv.get(position).getFirstAirDate());
        holder.txtRate.setText(String.valueOf(listTv.get(position).getVoteAverage()));
        holder.txtGenre.setText(listTv.get(position).getGenre());

        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listTv.get(position).getPosterPath();
        Glide.with(context)
                .load(urlPhoto)
                .apply(new RequestOptions().override(100, 140))
                .into(holder.imgPhoto);
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
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_rate)
        TextView txtRate;
        @BindView(R.id.img_photo)
        ImageView imgPhoto;
        TvViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
