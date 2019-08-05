package com.dicoding.picodiploma.finalsubmission.adapters;

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
    private boolean isShowAll;

    public MovieReviewAdapter(Context context, boolean isShowAll) {
        this.context = context;
        this.isShowAll = isShowAll;
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
        if (isShowAll) {
            holder.txtAuthor.setText(listMovie.get(position).getAuthor());
            holder.txtContent.setText(listMovie.get(position).getContent());
        } else {
            holder.txtAuthor.setText(listMovie.get(position).getAuthor());
            holder.txtContent.setText(listMovie.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        if (listMovie != null) {
            if (isShowAll) {
                return listMovie.size();
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
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
