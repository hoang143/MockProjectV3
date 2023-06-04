package com.example.mockprojectv3.ui.main;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mockprojectv3.databinding.FragmentSignupBinding;
import com.example.mockprojectv3.repositories.Resource;
import com.example.mockprojectv3.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    FragmentSignupBinding mFragmentSignupBinding;
    FragmentManager fragmentManager;
    UserViewModel userViewModel;
    View mView;
    private ProgressDialog progressDialog;
    TextView tvSignUp;
    EditText etMail, etPassword, etUserName, etConfirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSignupBinding = FragmentSignupBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mView = mFragmentSignupBinding.getRoot();
        InitUI();
        InitListener();
        return mView;
    }

    private void InitListener() {
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                userViewModel.navigateTo(fragmentManager, new LoginFragment());
                return true;
            }
            return false;
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClick();
            }
        });

        mFragmentSignupBinding.getRoot().setOnTouchListener((v, event) -> {
            hideKeyboard();
            return false;
        });
        mFragmentSignupBinding.tvToLogIn.setOnClickListener(view -> userViewModel.navigateTo(fragmentManager, new LoginFragment()));
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void InitUI() {
        fragmentManager = requireActivity().getSupportFragmentManager();
        tvSignUp = mFragmentSignupBinding.tvSignUpSignUp;
        etMail = mFragmentSignupBinding.etMailSignUp;
        etPassword = mFragmentSignupBinding.etPasswordSignUp;
        etConfirmPassword = mFragmentSignupBinding.etCofirmPasswordSignUp;
        etUserName = mFragmentSignupBinding.etUserNameSignUp;
        progressDialog = new ProgressDialog(getContext());

        etUserName.setSingleLine();
        etPassword.setSingleLine();
        etConfirmPassword.setSingleLine();
        etMail.setSingleLine();
    }

    private void onSignUpClick() {
        String email = etMail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String username = etUserName.getText().toString().trim();

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.signUp(email, password);

        progressDialog.setTitle("Please wait to Sign in");
        progressDialog.show();
        userViewModel.getCurrentUserState().observe(getViewLifecycleOwner(), new Observer<Resource<FirebaseUser>>() {
            @Override
            public void onChanged(Resource<FirebaseUser> state) {

                if (state.getStatus() == Resource.Status.SUCCESS && state.getData() != null) {
                    progressDialog.dismiss();
                    userViewModel.navigateTo(fragmentManager, new HomeFragment());

                } else if (state.getStatus() == Resource.Status.ERROR) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), state.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}