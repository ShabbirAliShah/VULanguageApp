package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.LearnersAdapter;
import com.example.vulanguageapp.databinding.FragmentLearnersOnlineBinding;
import com.example.vulanguageapp.models.UserAccountModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LearnersOnlineFragment extends Fragment {

        private List<UserAccountModel> dataList = new ArrayList<>();  // Store UserAccountModel objects
        private List<UserAccountModel> filteredList = new ArrayList<>();  // For search functionality

        private FragmentLearnersOnlineBinding binding;
        private NavController navController;
        private RecyclerView recyclerView;
        private EditText searchInput;
        private ImageButton searchButton;
        private LearnersAdapter learnersAdapter;

        @Override
        public View onCreateView(
                @NonNull LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState
        ) {
            binding = FragmentLearnersOnlineBinding.inflate(inflater, container, false);
            recyclerView = binding.usersListView;
            searchInput = binding.searchText;
            searchButton = binding.searchbtn;

            searchInput.requestFocus();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            return binding.getRoot();
        }

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            navController = NavHostFragment.findNavController(this);
            usersOnline();

            searchButton.setOnClickListener(v -> {
                String searchTerm = searchInput.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length() < 5) {
                    searchInput.setError("Enter a minimum of 5 letters");
                    return;
                }
                filterUsers(searchTerm);
            });
        }

        // Method to filter the users based on the search term
        private void filterUsers(String searchTerm) {
            filteredList.clear();
            for (UserAccountModel user : dataList) {
                if (user.getUserName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(user);
                }
            }

            if (learnersAdapter != null) {
                learnersAdapter.updateData(filteredList);  // Update adapter with filtered data
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

        public void usersOnline() {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            Query onlineUsersQuery = usersRef.orderByChild("status").equalTo("online");

            onlineUsersQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            UserAccountModel user = dataSnapshot.getValue(UserAccountModel.class);

                            if (user != null) {
                                dataList.add(user);
                            }
                        } catch (DatabaseException e) {
                            Log.e("FirebaseError", "Error parsing data: " + e.getMessage());
                        }
                    }

                    // Set the full dataList for the initial load
                    learnersAdapter = new LearnersAdapter(dataList, navController);
                    recyclerView.setAdapter(learnersAdapter);

                    learnersAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Log.e("FirebaseError", "Error: " + error.getMessage());
                }
            });
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onResume() {
            super.onResume();
        }
    }
