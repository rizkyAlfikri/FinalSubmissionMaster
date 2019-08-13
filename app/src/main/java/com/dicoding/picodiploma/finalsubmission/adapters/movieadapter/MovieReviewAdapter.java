package com.dicoding.picodiploma.finalsubmission.adapters.movieadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.models.moviemodels.MovieReview;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieViewHolder> {
    private Context context;
    private List<MovieReview> listMovie;

    public MovieReviewAdapter(Context context) {
        this.context = context;
    }

    // mengset data movie lalu melakukan notifikasi ke adapter
    public void setListMovie(List<MovieReview> listMovie) {
        this.listMovie = listMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieReviewAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout yang akan digunakan oleh adapter
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_review, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.MovieViewHolder holder, int position) {
        // mengload data yang telah di tangkap melalui method setListMovie
        holder.txtAuthor.setText(listMovie.get(position).getAuthor());
        holder.txtContent.setText(listMovie.get(position).getContent());
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
        // inisialisasi objek TextView,
        @BindView(R.id.txt_author)
        TextView txtAuthor;
        @BindView(R.id.txt_content)
        TextView txtContent;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
