package com.example.filmhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filmhub.R;
import com.example.filmhub.models.Movie;
import com.example.filmhub.ui.MovieDetailActivity;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewPoster;
        public TextView textViewTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.image_view_poster);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        if (movie != null) {
            String posterPath = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();

            Glide.with(holder.itemView.getContext())
                    .load(posterPath)
                    .into(holder.imageViewPoster);

            holder.textViewTitle.setText(movie.getTitle());

            holder.imageViewPoster.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), MovieDetailActivity.class);
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("overview", movie.getOverview());
                intent.putExtra("posterPath", movie.getPosterPath());
                intent.putExtra("releaseDate", movie.getReleaseDate() != null ? movie.getReleaseDate() : "N/A"); // Agregando la fecha de lanzamiento
                intent.putExtra("voteAverage", String.valueOf(movie.getVoteAverage())); // Agregando la calificación
                holder.itemView.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }
}
