package com.example.filmhub.api;

import com.example.filmhub.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("movie/popular?api_key=")
    Call<List<Movie>> getPopularMovies();
}
