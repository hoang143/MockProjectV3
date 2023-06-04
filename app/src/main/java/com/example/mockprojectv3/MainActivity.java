package com.example.mockprojectv3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mockprojectv3.ui.main.FavouriteFragment;
import com.example.mockprojectv3.ui.main.HomeFragment;
import com.example.mockprojectv3.ui.main.NotificationFragment;
import com.example.mockprojectv3.ui.main.ProfileFragment;
import com.example.mockprojectv3.viewmodel.HomeViewModel;
import com.example.mockprojectv3.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    public static final int MY_REQUEST_CODE = 10;
    private ProfileFragment profileFragment = ProfileFragment.getInstance();
    HomeFragment homeFragment = HomeFragment.getInstance();
    FavouriteFragment favouriteFragment = new FavouriteFragment();
    NotificationFragment notificationFragment = new NotificationFragment();

    public HomeViewModel homeViewModel;
    public UserViewModel userViewModel;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent == null){
                    return;
                }
                Uri uri = intent.getData();
                Log.v("Debug", uri.toString());
                profileFragment.setmUri(uri);
                Log.v("Debug", "in");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profileFragment.setBitMapImageView(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel .class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel .class);

        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setVisibility(View.GONE);
    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        // Ẩn tất cả các Fragment hiện tại trước khi thêm Fragment mới
//        if (homeFragment.isAdded()) {
//            transaction.hide(homeFragment);
//        }
//        if (favouriteFragment.isAdded()) {
//            transaction.hide(favouriteFragment);
//        }
//        if (notificationFragment.isAdded()) {
//            transaction.hide(notificationFragment);
//        }
//        if (profileFragment.isAdded()) {
//            transaction.hide(profileFragment);
//        }
//
//        // Thực hiện replace Fragment mới
//        switch (item.getItemId()){
//            case R.id.home:
//                if (homeFragment.isAdded()) {
//                    transaction.show(homeFragment);
//                } else {
//                    transaction.add(R.id.containerFragment, homeFragment);
//                }
//                break;
//
//            case R.id.favourite:
//                if (favouriteFragment.isAdded()) {
//                    transaction.show(favouriteFragment);
//                } else {
//                    transaction.add(R.id.containerFragment, favouriteFragment);
//                }
//                break;
//
//            case R.id.notification:
//                if (notificationFragment.isAdded()) {
//                    transaction.show(notificationFragment);
//                } else {
//                    transaction.add(R.id.containerFragment, notificationFragment);
//                }
//                break;
//
//            case R.id.profile:
//                if (profileFragment.isAdded()) {
//                    transaction.show(profileFragment);
//                } else {
//                    transaction.add(R.id.containerFragment, profileFragment);
//                }
//                break;
//        }
//
//        transaction.commit();
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()){
        case R.id.home:
//            Navigation.findNavController(getCurrentFocus()).navigate(R.id.home);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, homeFragment).commit();
            return true;

        case R.id.favourite:
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, favouriteFragment).commit();
            return true;

        case R.id.notification:
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, notificationFragment).commit();
            return true;

        case R.id.profile:
            getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, profileFragment).commit();
            return true;
    }
    return false;
}


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.length > 0){

                openGallery();
            }else {
                Toast.makeText(this, "Please give us permission to continue", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}