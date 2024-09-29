package com.example.filmhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.filmhub.R;
import com.example.filmhub.api.ApiClient;
import com.example.filmhub.api.ApiService;
import com.example.filmhub.models.FavoriteResponse;

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

    private TextView textViewFavorites;

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
        String title = intent.getStringExtra("title");
        String overview = intent.getStringExtra("overview");
        String posterPath = intent.getStringExtra("posterPath");
        String releaseDate = intent.getStringExtra("releaseDate");
        String voteAverage = intent.getStringExtra("voteAverage");
        movieId = intent.getIntExtra("movieId", -1);
        isFavorite = intent.getBooleanExtra("isFavorite", false);

        if (title != null && overview != null && posterPath != null) {
            textViewTitle.setText(title);
            textViewOverview.setText(overview);
            String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
            Glide.with(this).load(imageUrl).into(imageViewPoster);
        }

        if (releaseDate != null) {
            textViewReleaseDate.setText(getString(R.string.release_date) + ": " + releaseDate);
        }

        if (voteAverage != null) {
            textViewVoteAverage.setText(getString(R.string.vote_average) + ": " + voteAverage);
        }

        buttonFavorite.setOnClickListener(v -> {
            if (!isFavorite) {
                addMovieToFavorites(movieId);
            }
        });

        textViewFavorites = findViewById(R.id.text_view_favorites);

        textViewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailActivity.this, FavoritesActivity.class);
                startActivity(intent);
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
                    Log.i("MovieDetailActivity", "Película añadida a favoritos con éxito.");
                    Toast.makeText(MovieDetailActivity.this, "Película añadida a favoritos.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("MovieDetailActivity", "Error al añadir a favoritos: " + response.message());
                    Toast.makeText(MovieDetailActivity.this, "Error al añadir a favoritos: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                Log.e("MovieDetailActivity", "Error en la llamada API: " + t.getMessage());
                Toast.makeText(MovieDetailActivity.this, "Error en la llamada API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
