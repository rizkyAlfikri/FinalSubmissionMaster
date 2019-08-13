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

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.MovieViewHolder> {

    private Context context;
    private List<MovieResults> listMovie;
    private List<MovieGenres> listGenreMovie;

    public MovieSearchAdapter(Context context) {
        this.context = context;
    }

    // mengset data movie  lalu melakukan notifikasi ke adapter
    public void setListMovie(List<MovieResults> listMovie) {
        this.listMovie = listMovie;
        notifyDataSetChanged();
    }

    // mengset data movie genre, lalu melakukan notifikasi ke adapter
    public void setListGenreMovie(List<MovieGenres> listGenreMovie) {
        this.listGenreMovie = listGenreMovie;
        notifyDataSetChanged();
    }
    

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout yang akan digunakan oleh adapter
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // mengload data yang telah di tangkap melalui method setListMovie
        holder.txtTitle.setText(listMovie.get(position).getTitle());
        holder.txtRate.setText(String.valueOf(listMovie.get(position).getVoteAverage()));
        holder.txtDate.setText(String.valueOf(listMovie.get(position).getReleaseDate()));

        // mengload data movie genre jika listMovieGenre tidak null
        if (listGenreMovie != null) {
            String genre = holder.getGenres(listMovie.get(position).getGenreIds());
            holder.txtGenre.setText(genre);
        }

        // load image data
        String urlPhoto = Config.IMAGE_URL_BASE_PATH + listMovie.get(position).getPosterPath();
        Glide.with(context)
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

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        // data dari listMovieGenre berupa code integer dari genre movie,
        // sehingga perlu di diubah terlebih dahulu sebelum ditampilkan
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