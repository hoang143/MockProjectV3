package com.example.mockprojectv3.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mockprojectv3.model.FavoriteMovies;
import com.example.mockprojectv3.model.User;
import com.example.mockprojectv3.repositories.FavouriteMovieRepository;
import com.example.mockprojectv3.repositories.FavouriteMovieRepositoryImpl;
import com.example.mockprojectv3.repositories.MovieRepositoryImpl;
import com.example.mockprojectv3.repositories.Resource;

import java.util.List;

public class FavouriteMovieViewModel extends ViewModel {
    private FavouriteMovieRepository repository;

    private LiveData<List<FavoriteMovies>> allFavouriteMovies;
    private LiveData<Resource<User>> user;

    private LiveData<Resource<User>> getUser(){
      return user;
    }

    public FavouriteMovieViewModel(Application application) {
        repository = new FavouriteMovieRepositoryImpl(application);
    }

    public void getUserByMail(String mail){
        repository.GetUserByMail(mail);
    }
}
