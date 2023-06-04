package com.example.mockprojectv3.repositories;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public interface FirebaseRepository {
    void signUp(String email, String password);
    void updateProfile(UserProfileChangeRequest profileUpdates);
    void signIn(String email, String password);
    void signOut();
    MutableLiveData<Resource<FirebaseUser>> getCurrentUser();
}