package com.example.mockprojectv3.viewmodel;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mockprojectv3.R;
import com.example.mockprojectv3.repositories.FirebaseRepositoryImpl;
import com.example.mockprojectv3.repositories.Resource;
import com.example.mockprojectv3.repositories.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<Resource<FirebaseUser>> currentUserState;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public LiveData<FirebaseUser> getUser(){
        return user;
    }
    public void setUser(FirebaseUser user1){
        user.setValue(user1);
    }

    public UserViewModel() {
        firebaseRepository = FirebaseRepositoryImpl.getInstance();
        currentUserState = firebaseRepository.getCurrentUser();
    }

    public void signUp(String email, String password) {
        firebaseRepository.signUp(email, password);
    }

    public void signIn(String email, String password) {
        email = email.trim();
        password = password.trim();
        firebaseRepository.signIn(email, password);
    }

    public void updateProfile(UserProfileChangeRequest userProfileChangeRequest) {
        firebaseRepository.updateProfile(userProfileChangeRequest);
    }

    public void signOut() {
        firebaseRepository.signOut();
    }

    public MutableLiveData<Resource<FirebaseUser>> getCurrentUserState() {
        return currentUserState;
    }

    public void navigateTo(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.containerFragment, fragment)
                .commit();
    }
    public void navigateToAndAdd(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.containerFragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
