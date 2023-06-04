package com.example.mockprojectv3.repositories;

import androidx.lifecycle.LiveData;
import com.example.mockprojectv3.model.MovieModel;
import com.example.mockprojectv3.viewmodel.SingleLiveEvent;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface MovieRepository {
    SingleLiveEvent<Resource<List<MovieModel>>> getSearchMovies();

    SingleLiveEvent<Resource<List<MovieModel>>> getPopularMovies();

    SingleLiveEvent<Resource<List<MovieModel>>> getTrendingMovies();

    SingleLiveEvent<Resource<MovieModel>> getMovieByID();

    void getMovieByID(int movieID);

    void loadMoviesApi(int pageNumber);

    void searchMovies(String query, int pageNumber);

    void searchNextPage();

    void loadNextPage();

}
