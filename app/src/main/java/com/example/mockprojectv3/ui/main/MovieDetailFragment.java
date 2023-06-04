package com.example.mockprojectv3.ui.main;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mockprojectv3.R;
import com.example.mockprojectv3.model.MovieModel;
import com.example.mockprojectv3.repositories.FireStoreRepository;
import com.example.mockprojectv3.repositories.FireStoreRepositoryImpl;
import com.example.mockprojectv3.repositories.Resource;
import com.example.mockprojectv3.utils.Credentials;
import com.example.mockprojectv3.viewmodel.FireStoreViewModel;
import com.example.mockprojectv3.viewmodel.FireStoreViewModelFactory;
import com.example.mockprojectv3.viewmodel.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {
    HomeViewModel homeViewModel;
    private static MovieDetailFragment instance;

    public static MovieDetailFragment getInstance() {
        if (instance == null) {
            instance = new MovieDetailFragment();
        }
        return instance;
    }

    private TextView tvTitle, tvOverView, tvReleaseDate, tvAddToFavourite;
    private ImageView ivPoster;

    int movieID;
    private RatingBar ratingBar;
    View view;

    private FireStoreViewModel fireStoreViewModel;

    public static MovieDetailFragment newInstance(Bundle bundle) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        FireStoreRepository fireStoreRepository = FireStoreRepositoryImpl.getInstance();
        FireStoreViewModelFactory viewModelFactory = new FireStoreViewModelFactory(fireStoreRepository);
        fireStoreViewModel = new ViewModelProvider(this, viewModelFactory).get(FireStoreViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_movie_detail, container, false);
        initUI();
        GetDataFromHome();
        initListener();

        // Inflate the layout for this fragment
        return view;
    }

    private void initListener() {
        tvAddToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.e("Debug", "id: " + movieID);

                // Thực hiện thêm phim vào danh sách yêu thích
                fireStoreViewModel.addFavoriteMovie(user.getUid(), movieID);

                // Quan sát trạng thái của LiveData khi thêm phim
                fireStoreViewModel.getFavoriteMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<List<Integer>>>() {
                    @Override
                    public void onChanged(Resource<List<Integer>> resource) {
                        if (resource.getStatus() == Resource.Status.LOADING) {
                            // Hiển thị loading state
                        } else if (resource.getStatus() == Resource.Status.SUCCESS) {
                            // Hiển thị thành công
                            Toast.makeText(requireContext(), "Added to favorite movies", Toast.LENGTH_SHORT).show();
                        } else if (resource.getStatus() == Resource.Status.ERROR) {
                            // Hiển thị lỗi
                            Toast.makeText(requireContext(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.containerFragment, HomeFragment.getInstance())
                        .commit();
            }
        });
    }

    private void initUI() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNav);
        bottomNavigationView.setVisibility(View.GONE);
        tvReleaseDate = view.findViewById(R.id.tvReleaseDate);
        ratingBar = view.findViewById(R.id.ratingBar);
        tvTitle = view.findViewById(R.id.tvTitle_MovieDetails);
        tvOverView = view.findViewById(R.id.tvOverView_MovieDetails);
        ivPoster = view.findViewById(R.id.ivPoster_MovieDetails);
        tvAddToFavourite = view.findViewById(R.id.tvAddToFavourite);
    }

    private void GetDataFromHome() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("movie")) {
            MovieModel movieModel = bundle.getParcelable("movie");
            tvTitle.setText(movieModel.getTitle());
            movieID = movieModel.getMovie_id();
            Log.e("Debug", "id in getData" + movieModel.getMovie_id());
            tvOverView.setText(movieModel.getMovie_overview());
            Glide.with(getContext())
                    .load(Credentials.BASE_URL_IMAGE + movieModel.getPoster_path())
                    .into(ivPoster);
            tvReleaseDate.setText(movieModel.getRelease_date());
            ratingBar.setRating(movieModel.getVote_average() / 2);

        }
    }
}