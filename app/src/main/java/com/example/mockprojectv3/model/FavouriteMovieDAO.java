package com.example.mockprojectv3.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FavouriteMovieDAO {

    @Insert
    Completable insert(FavoriteMovies favoriteMovies);

    @Update
    Completable update(FavoriteMovies favoriteMovies);

    @Delete
    Completable delete(FavoriteMovies favoriteMovies);

    @Query("SELECT * FROM favourite_movie WHERE user_id ==:userId")
    Flowable<List<FavoriteMovies>> getFavouriteMovies(int userId);
}
