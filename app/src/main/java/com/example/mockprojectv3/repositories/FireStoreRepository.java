package com.example.mockprojectv3.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mockprojectv3.model.MovieModel;

import java.util.List;

public interface FireStoreRepository {
    void addFavoriteMovieToFireStore(String userEmail, int movieID);
    void getDocumentDataFromFirestore(String userID);
    LiveData<Resource<List<Integer>>> getFavoriteMoviesLiveData();
}
