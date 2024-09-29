package com.example.filmhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmhub.R;
import com.example.filmhub.adapters.MovieAdapter;
import com.example.filmhub.api.ApiClient;
import com.example.filmhub.api.ApiService;
import com.example.filmhub.models.Movie;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private TextView textViewFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieAdapter = new MovieAdapter(this, new java.util.ArrayList<>());
        recyclerView.setAdapter(movieAdapter);

        loadPopularMovies();

        textViewFavorites = findViewById(R.id.text_view_favorites);

        textViewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadPopularMovies() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Locale currentLocale = getResources().getConfiguration().locale;
        String lang = currentLocale.getLanguage();
        String region = currentLocale.getCountry();

        String language = lang + "-" + region;

        Call<List<Movie>> call = apiService.getPopularMovies(language, region);

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();
                    movieAdapter.setMovies(movies);
                } else {
                    Log.e("MainActivity", "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("MainActivity", "Error en la llamada API: " + t.getMessage());
            }
        });
    }
}
