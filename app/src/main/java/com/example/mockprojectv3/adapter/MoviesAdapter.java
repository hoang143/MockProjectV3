package com.example.mockprojectv3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.example.mockprojectv3.R;
import com.example.mockprojectv3.model.Category;
import com.example.mockprojectv3.model.MovieModel;
import com.example.mockprojectv3.utils.Credentials;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<MovieModel> movies;
    private OnItemListener onItemListener;

    public MoviesAdapter(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }

    public void setData(List<MovieModel> list){
        this.movies = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        return new MovieViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel movie = movies.get(position);
        if(movie == null){
            return;
        }
        holder.tvTitle.setText(movie.getTitle());
        Glide.with(holder.itemView.getContext()).load(Credentials.BASE_URL_IMAGE + movie.getPoster_path())
                .error(R.drawable.img_not_found)
                .into(holder.ivPoter);
    }

    @Override
    public int getItemCount() {
        if(movies !=null){
            return movies.size();
        }
        return 0;
    }

    public MovieModel getSelectedMovie(int position) {
        if(movies != null){
            if(movies.size() >0){
                return movies.get(position);
            }
        }
        return null;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private ImageView ivPoter;
        OnItemListener onItemListener;

        public MovieViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivPoter = itemView.findViewById(R.id.ivMovie);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
}
