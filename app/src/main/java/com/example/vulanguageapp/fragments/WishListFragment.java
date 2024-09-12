package com.example.vulanguageapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.WishListAdapter;
import com.example.vulanguageapp.databinding.FragmentWishListBinding;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.example.vulanguageapp.models.UserAccountModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends Fragment {

    private UserIdProvider userIdProvider;
    private RecyclerView recyclerView;
    ArrayList<UserAccountModel> dataList = new ArrayList<>();


    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserIdProvider) {
            userIdProvider = (UserIdProvider) context;
        } else {
            throw new RuntimeException(context + " must implement UserIdProvider");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentWishListBinding binding = FragmentWishListBinding.inflate(inflater, container, false);

        recyclerView = binding.wishLIstRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchAllWishlists();

    }

    public void fetchAllWishlists() {

        NavController navController = NavHostFragment.findNavController(this);
        String userId = userIdProvider.getUserId();
        // Reference to the "users" node under "wishlist"
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("wishlist");

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    ArrayList<String> dataList = new ArrayList<>();

                    // Loop through each user in the "wishlist" node
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Get the wishlist for each user, ensuring the value isn't null
                        String course = userSnapshot.getValue(String.class);
                        if (course != null) {
                            dataList.add(course);
                        }
                    }

                    // Set the adapter for the wishlist items if the list is not empty
                    if (!dataList.isEmpty()) {
                        WishListAdapter adapter = new WishListAdapter(dataList, userId, navController);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "No wishlist items found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "User not found or no wishlists available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("WishlistFragment", "Database error: " + databaseError.getMessage());
            }
        });
    }

}