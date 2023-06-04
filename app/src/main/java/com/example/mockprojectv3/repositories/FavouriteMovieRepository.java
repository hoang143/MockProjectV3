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
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public interface FavouriteMovieRepository {
    void GetUserByMail(String mail);
    // void InsertUser(User user);
}