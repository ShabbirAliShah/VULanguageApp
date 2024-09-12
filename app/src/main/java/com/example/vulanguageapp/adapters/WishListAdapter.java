package com.example.vulanguageapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.CourseModel;
import com.example.vulanguageapp.models.UserAccountModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private ArrayList<String> wishlist;
    private String userId; // Add userId to track the user
    private NavController navController;

    public WishListAdapter(ArrayList<String> wishlist, String userId, NavController navController) {
        this.wishlist = wishlist;
        this.userId = userId; // Initialize userId
        this.navController = navController;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_for_wish_list, parent, false);
        return new WishListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        String wishlistItem = wishlist.get(position);
        holder.courseTitle.setText(wishlistItem);

        // Handle the click on the remove TextView
        holder.removeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Get the updated position
                if (adapterPosition != RecyclerView.NO_POSITION) { // Ensure the position is valid
                    removeItemFromWishlist(adapterPosition);
                }
            }
        });

        holder.enroll.setOnClickListener(v-> {

            Bundle dataBundle = new Bundle();
            dataBundle.putString("course_id", wishlistItem);
            navController.navigate(R.id.action_wishListFragment_to_languageDetailFragment, dataBundle);
        });
    }
    
    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    // Remove item from wishlist
    private void removeItemFromWishlist(int position) {
        String courseToRemove = wishlist.get(position);

        // Remove from local list
        wishlist.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, wishlist.size());

        // Remove from Firebase
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("wishlist");

        userWishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> updatedWishlist = new ArrayList<>();

                    // Loop through the wishlist and add items that are not the removed one
                    for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                        String course = courseSnapshot.getValue(String.class);
                        if (!course.equals(courseToRemove)) {
                            updatedWishlist.add(course);  // Add back the items except the removed one
                        }
                    }

                    // Update Firebase with the new wishlist
                    userWishlistRef.setValue(updatedWishlist).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //Toast.makeText(requireContext, "Course removed from wishlist", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(context, "Failed to update Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("WishListAdapter", "Database error: " + databaseError.getMessage());
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle, removeTextView, enroll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.wishListCourseTitle);
            enroll = itemView.findViewById(R.id.enrollNow);
            removeTextView = itemView.findViewById(R.id.removeWishList); // Find the TextView with id "remove"
        }
    }
}

