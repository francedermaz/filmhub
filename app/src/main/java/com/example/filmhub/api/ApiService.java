package com.example.filmhub.api;

import com.example.filmhub.models.Movie;
import com.example.filmhub.models.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("movie/popular?api_key=50496d349af7ede42dc06e6fb73c7cce")
    Call<MovieResponse> getPopularMovies();
}
