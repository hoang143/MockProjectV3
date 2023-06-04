package com.example.mockprojectv3.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mockprojectv3.R;
import com.example.mockprojectv3.databinding.FragmentLoginBinding;
import com.example.mockprojectv3.repositories.Resource;
import com.example.mockprojectv3.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private UserViewModel userViewModel;
    private FragmentManager fragmentManager;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.setUserViewModel(userViewModel);
        binding.setLifecycleOwner(this);

        initUI();
        initListeners();

        return view;
    }

    private void initUI() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNav);
        bottomNavigationView.setVisibility(View.GONE);
        fragmentManager = requireActivity().getSupportFragmentManager();

        progressDialog = new ProgressDialog(requireContext());

        binding.etUserNameLogin.setSingleLine();
        binding.etPasswordLogin.setSingleLine();
    }

    private void initListeners() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            private boolean doubleBackToExitPressedOnce = false;

            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    requireActivity().finish();
                } else {
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getContext(), "Press back to close", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 3000);
                }
            }
        });

       binding.tvLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onClickSignIn();
           }
       });

        binding.getRoot().setOnTouchListener((v, event) -> {
            hideKeyboard();
            return false;
        });

        binding.tvSignUp.setOnClickListener(view -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, new SignupFragment())
                    .commit();
        });

        binding.etPasswordLogin.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.etPasswordLogin.getRight() - binding.etPasswordLogin.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
    }

    private void onClickSignIn() {
        userViewModel.signIn(binding.etUserNameLogin.getText().toString(), binding.etPasswordLogin.getText().toString());

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Please wait to Sign in");
        progressDialog.show();

        userViewModel.getCurrentUserState().observe(getViewLifecycleOwner(), new Observer<Resource<FirebaseUser>>() {
            @Override
            public void onChanged(Resource<FirebaseUser> state) {

                if (state.getStatus() == Resource.Status.SUCCESS && state.getData() != null) {
                    progressDialog.dismiss();
                    userViewModel.navigateTo(fragmentManager, HomeFragment.getInstance());

                } else if (state.getStatus() == Resource.Status.ERROR) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), state.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void togglePasswordVisibility() {
        if (binding.etPasswordLogin.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            binding.etPasswordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.etPasswordLogin.setSelection(binding.etPasswordLogin.getText().length());
            binding.etPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeslash_white, 0);
        } else {
            binding.etPasswordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.etPasswordLogin.setSelection(binding.etPasswordLogin.getText().length());
            binding.etPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_white, 0);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
