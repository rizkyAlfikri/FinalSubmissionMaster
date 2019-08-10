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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieViewHolder> {
    private Context context;
    private List<MovieReview> listMovie;


    public MovieReviewAdapter(Context context) {
        this.context = context;
    }

    public void setListMovie(List<MovieReview> listMovie) {
        this.listMovie = listMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieReviewAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_review, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.MovieViewHolder holder, int position) {
        holder.txtAuthor.setText(listMovie.get(position).getAuthor());
        holder.txtContent.setText(listMovie.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        if (listMovie != null) {
            return listMovie.size();
        } else {
            return 0;
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_author)
        TextView txtAuthor;
        @BindView(R.id.txt_content)
        TextView txtContent;
        @BindString(R.string.no_movie_review)
        String noReview;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
