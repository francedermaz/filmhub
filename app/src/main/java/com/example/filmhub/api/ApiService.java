package com.example.filmhub.api;

import com.example.filmhub.models.FavoriteResponse;
import com.example.filmhub.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movies")
    Call<List<Movie>> getPopularMovies(
            @Query("language") String language,
            @Query("region") String region
    );

    @POST("favorites")
    Call<FavoriteResponse> addFavorite(@Body MovieIdRequest request);

    class MovieIdRequest {
        private int movieId;

        public MovieIdRequest(int movieId) {
            this.movieId = movieId;
        }

        public int getMovieId() {
            return movieId;
        }
    }

    @GET("favorites")
    Call<List<Movie>> getFavorites();

    @GET("movies/{id}")
    Call<Movie> getMovieDetails(@Path("id") int movieId,
                                @Query("language") String language);
}
