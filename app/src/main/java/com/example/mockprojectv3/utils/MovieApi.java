package com.example.mockprojectv3.utils;

import com.example.mockprojectv3.model.MovieModel;
import com.example.mockprojectv3.response.MovieSearchResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    // Search for movies
    @GET("/3/search/movie")
    Single<Response<MovieSearchResponse>> searchMovies(
            @Query("query") String query,
            @Query("page") int page,
            @Query("api_key") String key
    );

    // Get Popular
    @GET("/3/movie/popular")
    Single<Response<MovieSearchResponse>> getPopularMovies(
            @Query("page") int page,
            @Query("api_key") String key
    );

    // Get Trending
    @GET("/3/movie/top_rated")
    Single<Response<MovieSearchResponse>> getTrendingMovies(
            @Query("page") int page,
            @Query("api_key") String key
    );

    // Search for movies
    @GET("3/movie/{movie_id}?")
    Single<Response<MovieModel>> searchSingleMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String key
    );
}
