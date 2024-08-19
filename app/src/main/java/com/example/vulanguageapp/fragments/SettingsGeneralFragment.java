package com.example.vulanguageapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.BaseActivity;
import com.example.vulanguageapp.databinding.FragmentSettingsProfileBinding;
import com.example.vulanguageapp.databinding.FragmentSettingsThemeBinding;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.example.vulanguageapp.models.UserAccountModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class SettingsGeneralFragment extends Fragment {

    private List<String> groupList; // Initialize or pass these from another fragment/activity
    private List<List<String>> childList;
    private EditText displayName, userDescription;
    private Spinner experienceLevel;
    private Button upDateAccount;
    private UserIdProvider userIdProvider;
    private String userId;
    private String selectedChild;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    public SettingsGeneralFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserIdProvider) {
            userIdProvider = (UserIdProvider) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement UserIdProvider");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        userId = userIdProvider.getUserId();

        // Handle passed arguments
        Bundle bundle = getArguments();
        View rootView = null; // Initialize a View variable

        if (bundle != null) {
            selectedChild = bundle.getString("selected_child");

            // Check if the selected child is "profile" or "theme"
            if ("Profile".equals(selectedChild)) {
                // Inflate profile layout
                FragmentSettingsProfileBinding binding = FragmentSettingsProfileBinding.inflate(inflater, container, false);
                TextView settingsText = binding.settingsText;
                settingsText.setText("Selected Setting: Profile");

                rootView = binding.getRoot(); // Assign to rootView

            } else if ("Theme".equals(selectedChild)) {
                // Inflate theme layout
                FragmentSettingsThemeBinding binding = FragmentSettingsThemeBinding.inflate(inflater, container, false);
                TextView settingsText = binding.settingsText;
                settingsText.setText("Selected Setting: Theme");

                rootView = binding.getRoot(); // Assign to rootView
            }
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        displayName = view.findViewById(R.id.displayName);
        userDescription = view.findViewById(R.id.userDescription);
        experienceLevel = view.findViewById(R.id.experienceLevel);
        upDateAccount = view.findViewById(R.id.upDateAccount);

        if ("Profile".equals(selectedChild)) {
            upDateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Trigger saveOrUpdateUser() method when the button is clicked
                    saveOrUpdateUser();
                }
            });
        }

            if ("Theme".equals(selectedChild)) {
                RadioGroup themeRadioGroup = view.findViewById(R.id.themeRadioGroup);
                Button saveThemeButton = view.findViewById(R.id.saveThemeButton);

                if (themeRadioGroup != null && saveThemeButton != null) {
                    saveThemeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedId = themeRadioGroup.getCheckedRadioButtonId();

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if (selectedId == R.id.lightThemeRadioButton) {
                                editor.putString("theme", "light");
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            } else if (selectedId == R.id.darkThemeRadioButton) {
                                editor.putString("theme", "dark");
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            } else if (selectedId == R.id.systemDefaultThemeRadioButton) {
                                editor.putString("theme", "system");
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            }

                            editor.apply();
                        }
                    });
                }
            }
    }

    private void saveOrUpdateUser() {

        // Get the input data from the EditText and Spinner fields
        String name = displayName.getText().toString().trim();
        String description = userDescription.getText().toString().trim();
        String proficiency = experienceLevel.getSelectedItem().toString();

        // Create a UserAccountModel object with the data
        UserAccountModel userAccountModel = new UserAccountModel(userId, name, description, proficiency);

        // **Check if the user exists in Firebase:**
        // Here, we're checking if a child node with the user's UID exists in the "users" node.
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // **This part checks if the user exists:**
                // dataSnapshot.exists() returns true if the user ID node already exists.
                if (dataSnapshot.exists()) {
                    // **This part updates the user data if the user exists:**
                    // We update the existing data with the new values from userAccountModel.
                    databaseReference.child(userId).setValue(userAccountModel)
                            .addOnSuccessListener(aVoid -> {
                                // Data updated successfully
                                Toast.makeText(getContext(), "Account updated successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Failed to update data
                                Toast.makeText(getContext(), "Failed to update account", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // **This part inserts new user data if the user does not exist:**
                    // We create a new node with the user's UID and set its value to the new userAccountModel.
                    databaseReference.child(userId).setValue(userAccountModel)
                            .addOnSuccessListener(aVoid -> {
                                // Data inserted successfully
                                Toast.makeText(getContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Failed to insert data
                                Toast.makeText(getContext(), "Failed to create account", Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        userIdProvider = null; // Clean up the reference
    }
}
