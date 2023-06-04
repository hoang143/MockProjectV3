package com.example.mockprojectv3.ui.main;

import static com.example.mockprojectv3.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mockprojectv3.MainActivity;
import com.example.mockprojectv3.R;
import com.example.mockprojectv3.databinding.FragmentProfileBinding;
import com.example.mockprojectv3.repositories.FirebaseRepositoryImpl;
import com.example.mockprojectv3.repositories.Resource;
import com.example.mockprojectv3.viewmodel.HomeViewModel;
import com.example.mockprojectv3.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileFragment extends Fragment {
    private static ProfileFragment instance;

    HomeViewModel homeViewModel;

    public static ProfileFragment getInstance() {
        if (instance == null) {
            instance = new ProfileFragment();
        }
        return instance;
    }
    FragmentProfileBinding mFragmentProfileBinding;
    private UserViewModel userViewModel;
    FragmentManager fragmentManager;
    private Uri mUri;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        initUI();
        showUserInformation();
        initListener();
        ObserveProfileChange();

        return mFragmentProfileBinding.getRoot();
    }

    private void initUI() {

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mFragmentProfileBinding.setUserViewModel(userViewModel);
        mFragmentProfileBinding.etUserNameSignUp.setSingleLine();
        progressDialog = new ProgressDialog(getContext());
        fragmentManager = requireActivity().getSupportFragmentManager();
    }

    private void initListener() {
        mFragmentProfileBinding.tvSignOutProfilefragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                userViewModel.navigateTo(fragmentManager, new LoginFragment());
            }
        });
        mFragmentProfileBinding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        mFragmentProfileBinding.tvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickUpdateProfile();
            }
        });
    }

    private void onCLickUpdateProfile() {
        progressDialog.show();
        String username = mFragmentProfileBinding.etUserNameSignUp.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            username = user.getDisplayName();
        }


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(mUri)
                .build();
        Log.v("Debug", "in"+ mUri);

        userViewModel.updateProfile(profileUpdates);
        userViewModel.setUser(FirebaseAuth.getInstance().getCurrentUser());
    }

    private void ObserveProfileChange(){
        userViewModel.getCurrentUserState().observe(getViewLifecycleOwner(), firebaseUserState -> {
            if (firebaseUserState.getStatus() == Resource.Status.SUCCESS && firebaseUserState.getData() != null) {
                Log.v("Debug", "in function " + firebaseUserState.getData().getPhotoUrl());
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Update success", Toast.LENGTH_SHORT).show();
                showUserInformation();

            } else if (firebaseUserState.getStatus() == Resource.Status.ERROR) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), firebaseUserState.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickRequestPermission() {

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mainActivity.openGallery();
            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mainActivity.openGallery();
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    private void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null){
            mFragmentProfileBinding.tvUsername.setVisibility(View.GONE);
        }else {
            mFragmentProfileBinding.tvUsername.setVisibility(View.VISIBLE);
            mFragmentProfileBinding.tvUsername.setText(name);
        }

        mFragmentProfileBinding.tvMail.setText(email);
        Glide.with(this).load(photoUrl)
                .error(R.drawable.avatar)
                .into(mFragmentProfileBinding.ivAvatar);
    }

    public void setBitMapImageView(Bitmap bitmap){
        mFragmentProfileBinding.ivAvatar.setImageBitmap(bitmap);
    }

    public void setmUri(Uri uri) {
        if(uri == null){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            this.mUri = user.getPhotoUrl();
            return;
        }
        this.mUri = uri;
    }

}