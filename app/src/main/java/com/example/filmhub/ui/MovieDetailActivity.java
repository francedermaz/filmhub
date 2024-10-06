package com.example.filmhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmhub.models.Movie;
import com.google.android.material.snackbar.Snackbar;

import com.bumptech.glide.Glide;
import com.example.filmhub.R;
import com.example.filmhub.api.ApiClient;
import com.example.filmhub.api.ApiService;
import com.example.filmhub.models.FavoriteResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewOverview;
    private TextView textViewReleaseDate;
    private TextView textViewVoteAverage;
    private Button buttonBack;
    private Button buttonFavorite;

    private boolean isFavorite;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageViewPoster = findViewById(R.id.image_view_poster);
        textViewTitle = findViewById(R.id.text_view_title);
        textViewOverview = findViewById(R.id.text_view_overview);
        textViewReleaseDate = findViewById(R.id.text_view_release_date);
        textViewVoteAverage = findViewById(R.id.text_view_vote_average);
        buttonBack = findViewById(R.id.button_back);
        buttonFavorite = findViewById(R.id.button_favorite);

        buttonBack.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        movieId = intent.getIntExtra("movieId", -1);

        loadMovieDetails(movieId);

        buttonFavorite.setOnClickListener(v -> {
            if (!isFavorite) {
                showSnackbar(getString(R.string.added_to_favorites), false);
                addMovieToFavorites(movieId);
            } else {
                showSnackbar(getString(R.string.already_in_favorites), true);
            }
        });
    }

    private void loadMovieDetails(int movieId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Locale currentLocale = getResources().getConfiguration().locale;
        String lang = currentLocale.getLanguage();
        String region = currentLocale.getCountry();
        String language = lang + "-" + region;

        Call<Movie> call = apiService.getMovieDetails(movieId, language);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Movie movie = response.body();
                    textViewTitle.setText(movie.getTitle());
                    textViewOverview.setText(movie.getOverview());
                    textViewReleaseDate.setText(getString(R.string.release_date) + ": " + movie.getReleaseDate());
                    textViewVoteAverage.setText(getString(R.string.vote_average) + ": " + movie.getVoteAverage());
                    String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
                    Glide.with(MovieDetailActivity.this).load(imageUrl).into(imageViewPoster);
                    isFavorite = movie.getIsFavorite();

                    if (isFavorite) {
                        buttonFavorite.setVisibility(View.GONE);
                    } else {
                        buttonFavorite.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("MovieDetailActivity", "Error en la llamada API: " + t.getMessage());
            }
        });
    }

    private void addMovieToFavorites(int movieId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ApiService.MovieIdRequest request = new ApiService.MovieIdRequest(movieId);

        Call<FavoriteResponse> call = apiService.addFavorite(request);
        call.enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                if (response.isSuccessful()) {
                    isFavorite = true;
                    buttonFavorite.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                Log.e("MovieDetailActivity", "Error en la llamada API: " + t.getMessage());
            }
        });
    }

    private void showSnackbar(String message, boolean isError) {
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
