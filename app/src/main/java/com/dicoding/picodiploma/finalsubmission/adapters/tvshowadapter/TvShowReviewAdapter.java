package com.dicoding.picodiploma.finalsubmission.adapters.tvshowadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.finalsubmission.R;
import com.dicoding.picodiploma.finalsubmission.models.tvshowmodels.TvShowReview;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowReviewAdapter extends RecyclerView.Adapter<TvShowReviewAdapter.TvViewHolder> {
    private Context context;
    private List<TvShowReview> listTv;

    public TvShowReviewAdapter(Context context) {
        this.context = context;
    }

    // mengset data tv show lalu melakukan notifikasi ke adapter
    public void setListTv(List<TvShowReview> listTv) {
        this.listTv = listTv;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowReviewAdapter.TvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout yang akan digunakan oleh adapter
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_review, parent, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowReviewAdapter.TvViewHolder holder, int position) {
        // mengload data yang telah di tangkap melalui method setListTv
            holder.txtAuthor.setText(listTv.get(position).getAuthor());
            holder.txtContent.setText(listTv.get(position).getContent());
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
        // inisialisasi objek TextView,
        @BindView(R.id.txt_author)
        TextView txtAuthor;
        @BindView(R.id.txt_content)
        TextView txtContent;

        TvViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
