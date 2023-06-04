package com.example.mockprojectv3.repositories;
import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mockprojectv3.model.MovieModel;
import com.example.mockprojectv3.request.ApiService;
import com.example.mockprojectv3.response.MovieSearchResponse;
import com.example.mockprojectv3.utils.Credentials;
import com.example.mockprojectv3.viewmodel.SingleLiveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class MovieRepositoryImpl implements MovieRepository {

    private static MovieRepositoryImpl instance;

    public static MovieRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new MovieRepositoryImpl();
        }
        return instance;
    }
    private final SingleLiveEvent<Resource<List<MovieModel>>> mSearchMovies;
    private final SingleLiveEvent<Resource<List<MovieModel>>> mTrendingMovies;
    private final SingleLiveEvent<Resource<List<MovieModel>>> mPopularMovies;
    private final SingleLiveEvent<Resource<MovieModel>> mMovieByID;
    private final List<MovieModel> currentSearchResults;
    private final List<MovieModel> currentPopular;
    private final List<MovieModel> currentTrending;
    private String mQuery;
    private int mPageNumber;
    private int mPageNumberMovies;
    private final CompositeDisposable compositeDisposable;
    private RetrieveMovieRunnable retrieveMovieRunnable;
    private RetrieveMovies retrieveMovies;

    public MovieRepositoryImpl() {
        mSearchMovies = new SingleLiveEvent<>();
        mTrendingMovies = new SingleLiveEvent<>();
        mPopularMovies = new SingleLiveEvent<>();
        currentSearchResults = new ArrayList<>();
        mMovieByID = new SingleLiveEvent<>();
        currentPopular = new ArrayList<>();
        currentTrending = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
    }
    @Override
    public SingleLiveEvent<Resource<MovieModel>> getMovieByID() {
        return mMovieByID;
    }

    @Override
    public SingleLiveEvent<Resource<List<MovieModel>>> getSearchMovies() {
        return mSearchMovies;
    }

    @Override
    public SingleLiveEvent<Resource<List<MovieModel>>> getPopularMovies() {
        return mPopularMovies;
    }

    @Override
    public SingleLiveEvent<Resource<List<MovieModel>>> getTrendingMovies() {
        return mTrendingMovies;
    }

    @Override
    public void getMovieByID(int movieID) {
        Single<Response<MovieModel>> movieByID = ApiService.getMovieApi().searchSingleMovie(
                movieID,
                Credentials.API_KEY
        );
        compositeDisposable.add(
                movieByID
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .subscribe(
                                response->{
                                    MovieModel movie = new MovieModel();
                                    if (response.isSuccessful()) {
                                        movie = response.body();
                                    }
                                    mMovieByID.setValue(Resource.success(movie));
                                }, throwable -> {
                                    String errorMessage = throwable.getMessage();
                                    mMovieByID.setValue(Resource.error(errorMessage));
                                }
                        )
        );

    }

    @Override
    public void loadMoviesApi(int pageNumber) {
        mPageNumber = pageNumber;

        if (retrieveMovies == null) {
            retrieveMovies = new RetrieveMovies(pageNumber);
        }

        Single<Response<MovieSearchResponse>> trendingMoviesSingle = retrieveMovies.getTrendingMovies();
        Single<Response<MovieSearchResponse>> popularMoviesSingle = retrieveMovies.getPopularMovies();

        compositeDisposable.add(
                Flowable.zip(
                                popularMoviesSingle.toFlowable(),
                                trendingMoviesSingle.toFlowable(),
                                (moviesPopular, moviesTrending) -> {
                                    List<MovieModel> popularResults = new ArrayList<>();
                                    List<MovieModel> trendingResults = new ArrayList<>();
                                    if (moviesPopular.isSuccessful()) {
                                        popularResults.addAll(moviesPopular.body().getMovies());
                                    }
                                    if (moviesTrending.isSuccessful()) {
                                        trendingResults.addAll(moviesTrending.body().getMovies());
                                    }
                                    return new Pair<>(popularResults, trendingResults);
                                }
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .subscribe(
                                pair -> {
                                    List<MovieModel> trendingResults = pair.second;
                                    List<MovieModel> popularResults = pair.first;
                                    currentTrending.addAll(trendingResults);
                                    currentPopular.addAll(popularResults);
                                    mTrendingMovies.postValue(Resource.success(currentTrending));
                                    mPopularMovies.postValue(Resource.success(currentPopular));
                                },
                                throwable -> {
                                    // Handle error
                                    String errorMessage;
                                    if (throwable instanceof IOException) {
                                        errorMessage = "Network error!";
                                    } else {
                                        errorMessage = "Error occurred during data retrieval";
                                    }
                                    mPopularMovies.postValue(Resource.error(errorMessage));
                                    mTrendingMovies.postValue(Resource.error(errorMessage));
                                }
                        )
        );
    }

    @Override
    public void searchMovies(String query, int pageNumber) {
        mPageNumberMovies = pageNumber;
        mQuery = query;
        Log.e("Debug", "Query in search" + query);

        retrieveMovieRunnable = new RetrieveMovieRunnable(query, pageNumber);
        clearSearchResults();

        Single<Response<MovieSearchResponse>> searchMoviesSingle = retrieveMovieRunnable.getSearchMovies();
        compositeDisposable.add(
                searchMoviesSingle
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .subscribe(
                                response -> {
                                    List<MovieModel> searchResults = new ArrayList<>();
                                    if (response.isSuccessful()) {
                                        currentSearchResults.addAll(response.body().getMovies());
                                        searchResults.addAll(currentSearchResults);
                                    }
                                    mSearchMovies.postValue(Resource.success(searchResults));
                                },
                                throwable -> {
                                    // Handle error
                                    String errorMessage = throwable.getMessage();
                                    mSearchMovies.postValue(Resource.error(errorMessage));
                                }
                        )
        );
    }

    @Override
    public void searchNextPage() {
        searchMovies(mQuery, mPageNumberMovies + 1);
    }

    @Override
    public void loadNextPage() {
        loadMoviesApi(mPageNumber + 1);
    }

    private static class RetrieveMovieRunnable {
        private final String query;
        private final int pageNumber;

        public RetrieveMovieRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
        }

        public Single<Response<MovieSearchResponse>> getSearchMovies() {
            return ApiService.getMovieApi().searchMovies(
                    query,
                    pageNumber,
                    Credentials.API_KEY
            );
        }
    }

    private static class RetrieveMovies {
        private final int pageNumber;

        public RetrieveMovies(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public Single<Response<MovieSearchResponse>> getPopularMovies() {
            return ApiService.getMovieApi().getPopularMovies(
                    pageNumber,
                    Credentials.API_KEY
            );
        }

        public Single<Response<MovieSearchResponse>> getTrendingMovies() {
            return ApiService.getMovieApi().getTrendingMovies(
                    pageNumber,
                    Credentials.API_KEY
            );
        }
    }
    private void clearSearchResults() {
        currentSearchResults.clear();
    }

}
