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

    // mengset data tv show lalu melakukan notifikasi ke adapter
    public void setListTv(List<TvShowResults> listTv) {
        this.listTv = listTv;
        notifyDataSetChanged();
    }

    // mengset data tv show genre lalu melakukan notifikasi ke adapter
    public void setListTvGenre(List<TvShowGenres> listTvGenre) {
        this.listTvGenre = listTvGenre;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowAdapter.TvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout yang akan digunakan oleh adapter
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowAdapter.TvViewHolder holder, int position) {
        // mengload data yang telah di tangkap melalui method setListTvShow
        holder.txtTitle.setText(listTv.get(position).getName());
        holder.txtRate.setText(String.valueOf(listTv.get(position).getVoteAverage()));

        // mengatur 5 start rating bar agar bisa menampilkan  data tv show vote average
        float rate = (float) listTv.get(position).getVoteAverage() / 2;
        holder.ratingBar.setRating(rate);

        // jika listGenreTV tidak null, maka jalankan method get genre dan hasilnya di load ke txtGenre
        if (listTvGenre != null) {
            String genre = holder.getGenre(listTv.get(position).getGenreIds());
            holder.txtGenre.setText(genre);
        }

        // load image data
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listTv.get(position).getPosterPath();
        Glide.with(context).load(urlPhoto).apply(new RequestOptions()).into(holder.imgPhoto);
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
        // inisialisasi objek TextView, ImageView dan Ratting Bar
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

        // data dari listTvShowGenre berupa code integer dari genre tv show,
        // sehingga perlu di diubah terlebih dahulu sebelum ditampilkan
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
