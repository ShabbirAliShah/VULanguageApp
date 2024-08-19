package com.example.vulanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import com.example.vulanguageapp.activities.MainActivity;
import com.example.vulanguageapp.databinding.FragmentUserLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserLoginFragment extends Fragment {

    private FragmentUserLoginBinding binding;
    private FirebaseAuth mAuth;
    private EditText email, password;
    private ProgressBar progressBar;

    public UserLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUsr = mAuth.getCurrentUser();
        if (currentUsr != null) {

            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserLoginBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.loginemail);
        password = view.findViewById(R.id.loginpassword);
        progressBar = view.findViewById(R.id.progressbar);

        Button gotoReg = view.findViewById(R.id.register);
        Button loginButton = view.findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        NavController navController = NavHostFragment.findNavController(this);
        gotoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_userLoginFragment_to_userRegisterationFragment);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String usrEmail, usrPassword;

                usrEmail = email.getText().toString();
                usrPassword = password.getText().toString();

                if (TextUtils.isEmpty(usrEmail)) {

                    Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(usrPassword)) {
                    Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(usrEmail, usrPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Login Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}