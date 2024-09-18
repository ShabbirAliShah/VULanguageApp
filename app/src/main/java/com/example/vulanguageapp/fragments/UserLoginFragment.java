package com.example.vulanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.MainActivity;
import com.example.vulanguageapp.activities.StudyActivity;
import com.example.vulanguageapp.adminControlls.activities.AdminDashboard;
import com.example.vulanguageapp.databinding.FragmentUserLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.loginemail);
        password = view.findViewById(R.id.loginpassword);
        progressBar = view.findViewById(R.id.progressbar);

        Button gotoReg = view.findViewById(R.id.goToRegister);
        Button loginButton = view.findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        NavController navController = NavHostFragment.findNavController(this);
        gotoReg.setOnClickListener(v -> navController.navigate(R.id.action_userLoginFragment_to_userRegisterationFragment));

        loginButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String usrEmail = email.getText().toString();
            String usrPassword = password.getText().toString();

            if (TextUtils.isEmpty(usrEmail)) {
                Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (TextUtils.isEmpty(usrPassword)) {
                Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            mAuth.signInWithEmailAndPassword(usrEmail, usrPassword).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        checkAdmin(userId, isAdmin -> {
                            if (isAdmin) {
                                Intent intent = new Intent(getContext(), AdminDashboard.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getContext(), StudyActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Login Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void checkAdmin(String userId, AdminCheckCallback callback) {
        FirebaseDatabase.getInstance().getReference("roles").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("isAdmin")) {
                    boolean isAdmin = Boolean.TRUE.equals(snapshot.child("isAdmin").getValue(Boolean.class));
                    Log.d("CheckAdmin", "isAdmin: " + isAdmin);
                    callback.onResult(isAdmin);
                } else {
                    Log.d("CheckAdmin", "isAdmin key does not exist for user.");
                    callback.onResult(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CheckAdmin", "Database error: " + error.getMessage());
                callback.onResult(false);
            }
        });
    }

    interface AdminCheckCallback {
        void onResult(boolean isAdmin);
    }
}