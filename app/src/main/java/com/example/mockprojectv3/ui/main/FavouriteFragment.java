package com.example.mockprojectv3.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mockprojectv3.R;
import com.example.mockprojectv3.adapter.FavouriteMovieAdapter;
import com.example.mockprojectv3.adapter.MoviesAdapter;
import com.example.mockprojectv3.adapter.OnItemListener;
import com.example.mockprojectv3.databinding.FragmentFavouriteBinding;
import com.example.mockprojectv3.model.MovieModel;
import com.example.mockprojectv3.repositories.Resource;
import com.example.mockprojectv3.viewmodel.FireStoreViewModel;
import com.example.mockprojectv3.viewmodel.HomeViewModel;
import com.example.mockprojectv3.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {
    int i = 0;
    HomeViewModel homeViewModel;
    FragmentManager fragmentManager;
    List<MovieModel> movieModels = new ArrayList<>();
    UserViewModel userViewModel;
    RecyclerView rcFavourite;
    FavouriteMovieAdapter favouriteMovieAdapter;
    FragmentFavouriteBinding binding;
    FireStoreViewModel fireStoreViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fireStoreViewModel = new ViewModelProvider(this).get(FireStoreViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        binding.setLifecycleOwner(this);
        binding.setFireStoreViewModel(fireStoreViewModel);
        initUI();
        observeMovieIdChange();
        ConfigureRecycleView();
        return binding.getRoot();
    }

    private void initUI() {
        rcFavourite = binding.rcFavourite;
        fragmentManager = requireActivity().getSupportFragmentManager();
    }

    private void ConfigureRecycleView() {
        favouriteMovieAdapter = new FavouriteMovieAdapter(new OnItemListener() {
            @Override
            public void onItemClick(int position) {
                MovieModel movieModel = favouriteMovieAdapter.getSelectedMovie(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie", movieModel);

                MovieDetailFragment movieDetailsFragment = new MovieDetailFragment();
                movieDetailsFragment.setArguments(bundle);

                userViewModel.navigateTo(fragmentManager, movieDetailsFragment);
            }
        });

        rcFavourite.setAdapter(favouriteMovieAdapter);
        rcFavourite.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
    }

    private void observeMovieIdChange() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        fireStoreViewModel.getFavouriteMovies(user.getUid());
        fireStoreViewModel.getFavoriteMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<List<Integer>>>() {
            @Override
            public void onChanged(Resource<List<Integer>> resource) {
                if (resource.getStatus() == Resource.Status.LOADING) {
                    // Hiển thị loading state
                } else if (resource.getStatus() == Resource.Status.SUCCESS) {
                    List<Integer> movieIDs = resource.getData();

                    // Gọi hàm observeMovieByIdChange với danh sách movie IDs
                    observeMovieByIdChange(movieIDs);

//                    Toast.makeText(requireContext(), "Added to favorite movies", Toast.LENGTH_SHORT).show();
                } else if (resource.getStatus() == Resource.Status.ERROR) {
                    // Hiển thị lỗi
                    Toast.makeText(requireContext(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void observeMovieByIdChange(List<Integer> movieIDs) {
        HashSet<Integer> observedMovieIDs = new HashSet<>();


        for (Integer movieID : movieIDs) {
            homeViewModel.searchMovieByID(movieID);
            homeViewModel.getSearchMovieByID().observe(getViewLifecycleOwner(), new Observer<Resource<MovieModel>>() {
                @Override
                public void onChanged(Resource<MovieModel> resource) {
                    if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
                        MovieModel movie = resource.getData();

                        if (!observedMovieIDs.contains(movie.getMovie_id())) {
                            movieModels.add(movie);
                            observedMovieIDs.add(movie.getMovie_id());
                            favouriteMovieAdapter.setData(movieModels);
                        }
                    } else if (resource != null && resource.getStatus() == Resource.Status.ERROR) {
                        Toast.makeText(getContext(), resource.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}