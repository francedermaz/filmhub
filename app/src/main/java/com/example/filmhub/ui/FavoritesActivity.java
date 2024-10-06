package com.example.filmhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmhub.R;
import com.example.filmhub.adapters.FavoritesAdapter;
import com.example.filmhub.api.ApiClient;
import com.example.filmhub.api.ApiService;
import com.example.filmhub.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavorites;
    private FavoritesAdapter favoritesAdapter;
    private ImageView imageViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerViewFavorites = findViewById(R.id.recycler_view_favorites);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewFavorites.setLayoutManager(gridLayoutManager);

        fetchFavorites();

        imageViewLogo = findViewById(R.id.image_view_logo);

        imageViewLogo.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchFavorites() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Movie>> call = apiService.getFavorites();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> favoriteMovies = response.body();
                    favoritesAdapter = new FavoritesAdapter(FavoritesActivity.this, favoriteMovies);
                    recyclerViewFavorites.setAdapter(favoritesAdapter);
                } else {
                    Log.e("FavoritesActivity", "Error cargando las películas");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("FavoritesActivity", "Error en la llamada API: " + t.getMessage());
            }
        });
    }
}
