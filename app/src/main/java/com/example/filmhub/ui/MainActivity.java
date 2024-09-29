package com.example.filmhub.ui;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmhub.R;
import com.example.filmhub.adapters.MovieAdapter;
import com.example.filmhub.api.ApiClient;
import com.example.filmhub.api.ApiService;
import com.example.filmhub.models.Movie;
import com.example.filmhub.models.MovieResponse;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    // Esto NO debería estar ACA!!! Lo dejo para que puedan probar la app
    private final String API_KEY = "50496d349af7ede42dc06e6fb73c7cce";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieAdapter = new MovieAdapter(this, new java.util.ArrayList<>());
        recyclerView.setAdapter(movieAdapter);

        loadPopularMovies();
    }

    private void loadPopularMovies() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Locale currentLocale = getResources().getConfiguration().locale;
        String lang = currentLocale.getLanguage();
        String region = currentLocale.getCountry();

        String language = lang + "-" + region;

        Call<MovieResponse> call = apiService.getPopularMovies(API_KEY, language, region);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    movieAdapter.setMovies(movies);
                } else {
                    Log.e("MainActivity", "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("MainActivity", "Error en la llamada API: " + t.getMessage());
            }
        });
    }
}
