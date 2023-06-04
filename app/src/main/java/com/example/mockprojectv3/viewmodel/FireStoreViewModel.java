package com.example.mockprojectv3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mockprojectv3.model.MovieModel;
import com.example.mockprojectv3.repositories.FireStoreRepositoryImpl;
import com.example.mockprojectv3.repositories.MovieRepositoryImpl;
import com.example.mockprojectv3.repositories.Resource;

import java.util.List;

public class FireStoreViewModel extends ViewModel {
    private FireStoreRepositoryImpl fireStoreRepositoryImpl;

    private MovieRepositoryImpl movieRepository;
    private LiveData<Resource<List<Integer>>> favoriteMoviesLiveData;

    public FireStoreViewModel() {
        fireStoreRepositoryImpl = FireStoreRepositoryImpl.getInstance();
        favoriteMoviesLiveData = fireStoreRepositoryImpl.getFavoriteMoviesLiveData();
    }

    public LiveData<Resource<List<Integer>>> getFavoriteMoviesLiveData() {
        return favoriteMoviesLiveData;
    }

    public void getFavouriteMovies(String userID){
        fireStoreRepositoryImpl.getDocumentDataFromFirestore(userID);
    }

    public void addFavoriteMovie(String userID, int movieID) {
        fireStoreRepositoryImpl.addFavoriteMovieToFireStore(userID, movieID);
    }
}
