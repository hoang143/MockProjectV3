package com.example.mockprojectv3.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mockprojectv3.model.MovieModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreRepositoryImpl implements FireStoreRepository {
    private static FireStoreRepositoryImpl instance;
    DocumentReference userRef;

    public static FireStoreRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new FireStoreRepositoryImpl();
        }
        return instance;
    }

    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private MutableLiveData<Resource<List<Integer>>> favoriteMoviesLiveData;

    public FireStoreRepositoryImpl() {
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("Users");
        favoriteMoviesLiveData = new MutableLiveData<>();
    }

    public LiveData<Resource<List<Integer>>> getFavoriteMoviesLiveData() {
        return favoriteMoviesLiveData;
    }

    public void getDocumentDataFromFirestore(String userID) {
        userRef = usersCollection.document(userID);
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Long> movieIDs = (List<Long>) documentSnapshot.get("FavouriteMovies");
                            List<Integer> integerList = new ArrayList<>();
                            // Sử dụng danh sách movieIDs ở đây
                            for (Long i : movieIDs) {
                                integerList.add(i.intValue());
                            }
                            favoriteMoviesLiveData.postValue(Resource.success(integerList));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        throwError(e);
                    }
                });
    }

    public void addFavoriteMovieToFireStore(String userID, int movieID) {
        favoriteMoviesLiveData.postValue(Resource.loading());
        addDocument(userID, movieID);
    }

    private void updateDocument(int movieID) {
        userRef.update("FavouriteMovies", FieldValue.arrayUnion(movieID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        List<Integer> currentMovies = favoriteMoviesLiveData.getValue().getData();
                        if (currentMovies == null) {
                            currentMovies = new ArrayList<>();
                        }
                        currentMovies.add(movieID);
                        favoriteMoviesLiveData.postValue(Resource.success(currentMovies));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        throwError(e);
                    }
                });
    }

    private void addDocument(String userID, int movieID) {
        Map<String, Object> newData = new HashMap<>();
        userRef = usersCollection.document(userID);
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Tài liệu đã tồn tại
                            updateDocument(movieID);
                        } else {
                            userRef.set(newData);
                            updateDocument(movieID);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        throwError(e);
                    }
                });
    }

    private void throwError(@NonNull Exception e) {
        String errorMessage;
        if (e instanceof FirebaseFirestoreException) {
            FirebaseFirestoreException firestoreException = (FirebaseFirestoreException) e;
            FirebaseFirestoreException.Code errorCode = firestoreException.getCode();
            switch (errorCode) {
                case CANCELLED:
                    errorMessage = "Operation cancelled.";
                    break;
                case UNKNOWN:
                    errorMessage = "Unknown error occurred.";
                    break;
                case INVALID_ARGUMENT:
                    errorMessage = "Invalid argument provided.";
                    break;
                case DEADLINE_EXCEEDED:
                    errorMessage = "Deadline exceeded.";
                    break;
                case NOT_FOUND:
                    errorMessage = "Requested document not found.";
                    break;
                case ALREADY_EXISTS:
                    errorMessage = "Document already exists.";
                    break;
                case PERMISSION_DENIED:
                    errorMessage = "Permission denied.";
                    break;
                case RESOURCE_EXHAUSTED:
                    errorMessage = "Resource exhausted.";
                    break;
                case FAILED_PRECONDITION:
                    errorMessage = "Precondition failed.";
                    break;
                case ABORTED:
                    errorMessage = "Operation aborted.";
                    break;
                case OUT_OF_RANGE:
                    errorMessage = "Out of range.";
                    break;
                case UNIMPLEMENTED:
                    errorMessage = "Operation is not implemented.";
                    break;
                case INTERNAL:
                    errorMessage = "Internal error occurred.";
                    break;
                case UNAVAILABLE:
                    errorMessage = "Service is unavailable.";
                    break;
                case DATA_LOSS:
                    errorMessage = "Data loss occurred.";
                    break;
                default:
                    errorMessage = "Error occurred: " + e.getMessage();
                    break;
            }
        } else {
            errorMessage = "Error occurred: " + e.getMessage();
        }

        favoriteMoviesLiveData.postValue(Resource.error(errorMessage));
    }

}
