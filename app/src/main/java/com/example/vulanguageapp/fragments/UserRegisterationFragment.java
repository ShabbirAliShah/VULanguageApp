package com.example.vulanguageapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.databinding.FragmentUserRegisterationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRegisterationFragment extends Fragment {

    private FragmentUserRegisterationBinding binding;
    private FirebaseAuth mAuth;
    private EditText email, password;
    private ProgressBar progressBar;
    private Button submit, goToLogin;


    public UserRegisterationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserRegisterationBinding.inflate(inflater, container, false);

        email = binding.email;
        password = binding.password;
        progressBar = binding.progressbar;
        goToLogin = binding.goToLogin;
        submit = binding.submit;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goToLogin.findViewById(R.id.goToLogin);
        goToLogin.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_userRegisterationFragment_to_userLoginFragment);
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                String usrEmail, usrPassword;

                usrEmail = email.getText().toString();
                usrPassword = password.getText().toString();

                if (TextUtils.isEmpty(usrEmail)) {
                    Toast.makeText(getContext(), "enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(usrPassword)) {
                    Toast.makeText(getContext(), "enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(usrEmail, usrPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUsr = mAuth.getCurrentUser();
        if (currentUsr != null) {
            currentUsr.reload();
        }
    }
}