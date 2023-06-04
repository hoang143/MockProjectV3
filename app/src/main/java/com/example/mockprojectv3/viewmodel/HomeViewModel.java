package com.example.mockprojectv3.viewmodel;

import android.database.Observable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mockprojectv3.model.MovieModel;
import com.example.mockprojectv3.repositories.MovieRepository;
import com.example.mockprojectv3.repositories.MovieRepositoryImpl;
import com.example.mockprojectv3.repositories.Resource;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private int mPageNumber;
    private int mPageNumberSearchMovies;
    private final SingleLiveEvent<Resource<List<MovieModel>>> mSearchMovies;
    private final SingleLiveEvent<Resource<List<MovieModel>>> mTrendingMovies;
    private final SingleLiveEvent<Resource<List<MovieModel>>> mPopularMovies;
    private final SingleLiveEvent<Resource<MovieModel>> mSearchMovieByID;

    public int s = 6;

    public HomeViewModel() {
        Log.d("TAG", "HomeViewModel: ");
        movieRepository = MovieRepositoryImpl.getInstance();
        mSearchMovies = movieRepository.getSearchMovies();
        mTrendingMovies = movieRepository.getTrendingMovies();
        mPopularMovies = movieRepository.getPopularMovies();
        mSearchMovieByID = movieRepository.getMovieByID();
        loadMovies(1);
    }

    public SingleLiveEvent<Resource<List<MovieModel>>> getSearchMovies() {
        return mSearchMovies;
    }

    public SingleLiveEvent<Resource<List<MovieModel>>> getTrendingMovies() {
        return mTrendingMovies;
    }

    public SingleLiveEvent<Resource<List<MovieModel>>> getPopularMovies() {
        return mPopularMovies;
    }

    public LiveData<Resource<MovieModel>> getSearchMovieByID() {
        return mSearchMovieByID;
    }

    public void searchMovieByID(int movieID) {
        movieRepository.getMovieByID(movieID);
    }

    public void loadMovies(int pageNumber) {
        mPageNumber = pageNumber;
        movieRepository.loadMoviesApi(pageNumber);
    }

    public void searchMovies(String query, int pageNumber) {
        mPageNumberSearchMovies = pageNumber;
        movieRepository.searchMovies(query, pageNumber);
    }

    public void searchNextPage() {
        movieRepository.searchNextPage();
    }

    public void loadNextPage() {
        movieRepository.loadNextPage();
    }

    public void setPageNumber(int mPageNumber) {
        this.mPageNumber = mPageNumber;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

}