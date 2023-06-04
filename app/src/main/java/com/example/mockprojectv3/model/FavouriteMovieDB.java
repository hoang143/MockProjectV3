package com.example.mockprojectv3.model;

import android.content.Context;
import android.text.NoCopySpan;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabase.Callback;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, FavoriteMovies.class}, version = 1)
public abstract class FavouriteMovieDB extends RoomDatabase {
    public abstract FavouriteMovieDAO favouriteMovieDAO();

    public abstract UserDAO userDAO();

    // Singleton Pattern
    private static FavouriteMovieDB instance;

    public static synchronized FavouriteMovieDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            FavouriteMovieDB.class, "favourite_movie_DB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
