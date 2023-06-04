package com.example.mockprojectv3.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseRepositoryImpl implements FirebaseRepository {
    private static FirebaseRepositoryImpl instance;

    public static FirebaseRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new FirebaseRepositoryImpl();
        }
        return instance;
    }

    private final FirebaseAuth mAuth;
    private MutableLiveData<Resource<FirebaseUser>> mCurrentUser;

    private FirebaseRepositoryImpl() {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = new MutableLiveData<>();
    }

    @Override
    public void signUp(String email, String password) {
        mCurrentUser.setValue(Resource.loading());

        if (email.isEmpty() || password.isEmpty()) {
            mCurrentUser.setValue(Resource.error("Email and password cannot be empty."));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mCurrentUser.setValue(Resource.error(e.getMessage()));
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            mCurrentUser.setValue(Resource.success(user));
                        } else {
                            String errorMessage = task.getException().getMessage();
                            if (errorMessage.contains("password is invalid")) {
                                mCurrentUser.setValue(Resource.error("Invalid email or password."));
                            } else {
                                mCurrentUser.setValue(Resource.error(errorMessage));
                            }
                        }
                    }
                });
    }

    @Override
    public void updateProfile(UserProfileChangeRequest profileUpdates) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return;
        }

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mCurrentUser.setValue(Resource.success(user));
                        } else {
                            String errorMessage = task.getException().getMessage();
                            mCurrentUser.setValue(Resource.error(errorMessage));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = e.getMessage();
                        mCurrentUser.setValue(Resource.error(errorMessage));
                    }
                });
    }

    @Override
    public void signIn(String email, String password) {
        mCurrentUser.setValue(Resource.loading());

        if (email.isEmpty() || password.isEmpty()) {
            mCurrentUser.setValue(Resource.error("Email and password cannot be empty."));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            mCurrentUser.setValue(Resource.success(user));
                        } else {
                            String errorMessage = task.getException().getMessage();
                            if (errorMessage.contains("password is invalid") ||
                                    errorMessage.contains("There is no user record corresponding to this identifier")) {
                                mCurrentUser.setValue(Resource.error("Invalid email or password."));
                            } else {
                                mCurrentUser.setValue(Resource.error(errorMessage));
                            }
                        }
                    }
                });
    }

    @Override
    public void signOut() {
        mAuth.signOut();
        mCurrentUser.setValue(Resource.success(null));
    }

    @Override
    public MutableLiveData<Resource<FirebaseUser>> getCurrentUser() {
        return mCurrentUser;
    }
}
