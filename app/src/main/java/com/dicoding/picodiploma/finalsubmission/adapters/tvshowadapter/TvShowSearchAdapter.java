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

    // mengset data tv show  lalu melakukan notifikasi ke adapter
    public void setListTv(List<TvShowResults> listTv) {
        this.listTv = listTv;
    }

    // mengset data tv show genre, lalu melakukan notifikasi ke adapter
    public void setListGenreTv(List<TvShowGenres> listGenreTv) {
        this.listGenreTv = listGenreTv;
    }

    @NonNull
    @Override
    public TvShowSearchAdapter.TvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout yang akan digunakan oleh adapter
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_movie, parent, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowSearchAdapter.TvViewHolder holder, int position) {
        // mengload data yang telah di tangkap melalui method setListTv
        holder.txtTitle.setText(listTv.get(position).getName());
        holder.txtRate.setText(String.valueOf(listTv.get(position).getVoteAverage()));
        holder.txtDate.setText(listTv.get(position).getFirstAirDate());

        // load image data
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listTv.get(position).getPosterPath();
        Glide.with(context)
                .load(urlPhoto)
                .apply(new RequestOptions())
                .into(holder.imgPhoto);
        // mengload data tv show genre jika setListGenreTv tidak null
        if (listGenreTv != null) {
            String genre = holder.getGenre(listTv.get(position).getGenreIds());
            holder.txtGenre.setText(genre);
        }
    }

    @Override
    public int getItemCount() {
        // jika listTv tidak null, maka adapter akan menampilkan data yang jumlahnya sama dengan jumlah data listTv
        // jika listTv null, maka adapter tidak akan menampikan data
        if (listTv != null) {
            return listTv.size();
        } else {
            return 0;
        }
    }

    class TvViewHolder extends RecyclerView.ViewHolder {
        // inisialisasi objek TextView, ImageView
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

        // data dari setListGenreTv berupa code integer dari genre tv show,
        // sehingga perlu di diubah terlebih dahulu sebelum ditampilkan
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
