package com.dicoding.picodiploma.finalsubmission.adapters.movieadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieResults;
import com.dicoding.picodiploma.finalsubmission.utils.Config;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.MovieViewHolder> {
    private Activity activity;
    private ArrayList<MovieResults> listMovie = new ArrayList<>();

    public MovieFavoriteAdapter(Activity activity) {
        this.activity = activity;
    }

    // mengset data movie lalu melakukan notifikasi ke adapter
    public void setListMovie(ArrayList<MovieResults> listMovie) {
        this.listMovie.clear();
        this.listMovie.addAll(listMovie);
        notifyDataSetChanged();

    }


    public ArrayList<MovieResults> getListMovie() {
        return listMovie;
    }

    // adapter melakukan notifikasi ketika ada penambahan data
    public void addItem(MovieResults movieResults) {
        this.listMovie.add(movieResults);
        notifyItemInserted(listMovie.size() - 1);
    }

    // adapter melakukan notifikasi ketika ada pengurangan data
    public void removeItem(int position) {
        this.listMovie.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.listMovie.size());
    }

    @NonNull
    @Override
    public MovieFavoriteAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout yang akan digunakan oleh adapter
        View view = LayoutInflater.from(activity).inflate(R.layout.item_favorite_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieFavoriteAdapter.MovieViewHolder holder, int position) {
        // mengload data yang telah di tangkap melalui method setListMovie
        holder.txtTitle.setText(listMovie.get(position).getTitle());
        holder.txtDate.setText(listMovie.get(position).getReleaseDate());
        holder.txtRate.setText(String.valueOf(listMovie.get(position).getVoteAverage()));
        holder.txtGenre.setText(listMovie.get(position).getGenre());

        // load image data
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listMovie.get(position).getPosterPath();
        Glide.with(activity)
                .load(urlPhoto)
                .apply(new RequestOptions().override(100, 140))
                .into(holder.imgPhoto);
    }


    @Override
    public int getItemCount() {
        // jika listMovie tidak null, maka adapter akan menampilkan data yang jumlahnya sama dengan jumlah data listmovie
        // jika listMovie null, maka adapter tidak akan menampikan data
        if (listMovie != null) {
            return listMovie.size();
        } else {
            return 0;
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        // inisialisasi objek TextView, ImageView dan CardView
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
        @BindView(R.id.card_view)
        CardView cardView;
        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
