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
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowTrailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowTrailerAdapter extends RecyclerView.Adapter<TvShowTrailerAdapter.TvViewHolder> {
    private Context context;
    private List<TvShowTrailer> listTvTrailer;

    public TvShowTrailerAdapter(Context context) {
        this.context = context;
    }

    // mengset data tv show lalu melakukan notifikasi ke adapter
    public void setListTvTrailer(List<TvShowTrailer> listTvTrailer) {
        this.listTvTrailer = listTvTrailer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowTrailerAdapter.TvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout yang akan digunakan oleh adapter
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_trailer, parent, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowTrailerAdapter.TvViewHolder holder, int position) {
        // mengload data yang telah di tangkap melalui method setListTvTrailer
        holder.txtName.setText(listTvTrailer.get(position).getName());
        String trailerPhoto = "https://img.youtube.com/vi/" + listTvTrailer.get(position).getKey() + "/mqdefault.jpg";
        Glide.with(context).load(trailerPhoto).apply(new RequestOptions()).into(holder.imgTrailer);
    }

    @Override
    public int getItemCount() {
        // jika listTvTrailer tidak null dan jumlah datanya tidak lebih sama dengan dari 5,
        // maka adapter akan menampilkan data yang jumlahnya sama dengan jumlah data listTvTrailer
        // jika jumlah data di listTvTrailer lebih dari 5, maka adapter akan menampilkan 5 buah data
        // jika listTvTrailer null, maka adapter tidak akan menampikan data
        if (listTvTrailer != null) {
            if (listTvTrailer.size() >= 5) {
                return 5;
            } else {
                return listTvTrailer.size();
            }
        } else {
            return 0;
        }
    }

    class TvViewHolder extends RecyclerView.ViewHolder {
        // inisialisasi objek TextView dan ImageView
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.img_trailer)
        ImageView imgTrailer;
        TvViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
