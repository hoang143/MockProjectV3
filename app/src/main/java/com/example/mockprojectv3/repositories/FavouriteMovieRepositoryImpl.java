package com.example.mockprojectv3.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mockprojectv3.model.FavoriteMovies;
import com.example.mockprojectv3.model.FavouriteMovieDAO;
import com.example.mockprojectv3.model.FavouriteMovieDB;
import com.example.mockprojectv3.model.User;
import com.example.mockprojectv3.model.UserDAO;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteMovieRepositoryImpl implements FavouriteMovieRepository {
    private FavouriteMovieDAO favouriteMovieDAO;
    private UserDAO userDAO;

    private final CompositeDisposable compositeDisposable;
    private MutableLiveData<Resource<List<FavoriteMovies>>> favouriteMovies;
    private MutableLiveData<Resource<User>> user;

    private LiveData<Resource<List<FavoriteMovies>>> getFavouriteMovies(){
        return  favouriteMovies;
    };
    private LiveData<Resource<User>> getUser(){
        return  user;
    };

    public FavouriteMovieRepositoryImpl(Application application) {
        compositeDisposable = new CompositeDisposable();
        favouriteMovies = new MutableLiveData<>();
        user = new MutableLiveData<>();
        FavouriteMovieDB favouriteMovieDB = FavouriteMovieDB.getInstance(application);
        favouriteMovieDAO = favouriteMovieDB.favouriteMovieDAO();
        userDAO = favouriteMovieDB.userDAO();
    }

    @Override
    public void GetUserByMail(String mail) {
        compositeDisposable.add(
                userDAO.getUserByMail(mail)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> user.postValue(Resource.loading()))
                        .subscribe(
                                response -> {
                                    user.postValue(Resource.success(response));
                                },
                                throwable -> {
                                    user.postValue(Resource.error("Failed to get user"));
                                }
                        )
        );
    }

    // Implement other methods if needed
}
