package com.example.vulanguageapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.BaseActivity;
import com.example.vulanguageapp.adapters.EnrollmentTopicListAdapter;
import com.example.vulanguageapp.databinding.FragmentLanguageDetailBinding;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LanguageDetailFragment extends Fragment {

    private FragmentLanguageDetailBinding binding;
    private RecyclerView lessonsRecyclerView;
    private List<LessonsModel> dataList = new ArrayList<>();
    private UserIdProvider userIdProvider;
    private String courseId;
    private String title;
    private Button wishList, enroll, viewWishlist;
    private NavController navController;
    private LinearLayout buttonContainer;
    private TextView courseTitle, languageName, courseLevel, courseDescripton, courseDescriptonLabel;

    public LanguageDetailFragment() {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLanguageDetailBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);
        lessonsRecyclerView = binding.lessonsRecyclerView;
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewWishlist = binding.viewWishList;
        wishList = binding.wishlist;
        enroll = binding.enroll;
        courseTitle = binding.courseTitle;
        languageName = binding.languageName;
        courseDescripton = binding.courseDescription;
        courseLevel = binding.courseLevel;
        courseDescriptonLabel = binding.courseDescriptonLabel;
        buttonContainer = binding.enrollWishButton;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            String name = getArguments().getString("language_name");
            title = getArguments().getString("course_title");
            String level = getArguments().getString("course_level");
            courseId = getArguments().getString("course_id");
            String course_Description = getArguments().getString("courseDescription");

            Log.d("LanguageDetail fragment", "course Id in arguments" + courseId);

            courseTitle.setText(title);
            courseLevel.setText(level);
            languageName.setText(name);
            courseDescripton.setText(course_Description);

        } else {
            courseTitle.setText(R.string.no_data_found);
            courseLevel.setText(R.string.no_data_found);
            languageName.setText(R.string.no_data_found);
        }

        fetchCourseLesson();
        checkCourseEnrollment();

        wishList.setOnClickListener(v-> toggleWishlist());

        enroll.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putSerializable("lessonsData", (Serializable) dataList);
            bundle.putString("course_Id", courseId);
            bundle.putString("course_title", title);
            bundle.putString("userId", ((BaseActivity) requireActivity()).getUserId());

            navController.navigate(R.id.action_languageDetailFragment_to_topicToEnrollFragment, bundle);
        });

        viewWishlist.setOnClickListener(v->{
            navController.navigate(R.id.action_languageDetailFragment_to_wishListFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fetchCourseLesson() {

        Query query = FirebaseDatabase.getInstance().getReference("lessons").orderByChild("courseId").equalTo(courseId);

        EnrollmentTopicListAdapter enrollmentTopicListAdapter = new EnrollmentTopicListAdapter(dataList, getContext(), false);
        lessonsRecyclerView.setAdapter(enrollmentTopicListAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    LessonsModel lessonsModel = dataSnapshot.getValue(LessonsModel.class);
                    String key = dataSnapshot.getKey();
                    Objects.requireNonNull(lessonsModel).setLessonId(key);
                    if (lessonsModel != null) {
                        dataList.add(lessonsModel);

                    } else {
                        courseDescriptonLabel.setText("No lessons available for this course");
                        wishList.setVisibility(View.GONE);
                        enroll.setVisibility(View.GONE);
                    }
                }

                enrollmentTopicListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log the error if the query is cancelled or fails
                Log.e("FirebaseDebug", "DatabaseError: " + error.getMessage());
            }
        });
    }

    public void checkCourseEnrollment() {

        Query databaseReference = FirebaseDatabase.getInstance().getReference("enrollments").orderByChild("courseId").equalTo(courseId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    buttonContainer.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Snapshot exists, course is enrolled", Toast.LENGTH_SHORT).show();
                } else {
                    buttonContainer.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "You are not enrolled in this course", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void toggleWishlist() {
        String userId = userIdProvider.getUserId();  // Ensure you're retrieving the user ID correctly
        Log.d("LanguageDetail", "user and course Id: " + userId + ", course Id: " + courseId);

        // Correct the database reference to point to the user's wishlist
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)  // Reference the specific user by userId
                .child("wishlist")
                .child(courseId);  // Reference the specific course by courseId

        wishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Course is already in wishlist, remove it
                    wishlistRef.removeValue();  // Remove the course from the wishlist

                    wishList.setText(R.string.wishlist);  // Update the button text
                    Toast.makeText(getContext(), "Removed from wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    // Course is not in wishlist, add it
                    wishlistRef.setValue(title);  // Add the course to the wishlist

                    wishList.setText(R.string.remove_from_wishlist);  // Update the button text
                    Toast.makeText(getContext(), "Added to wishlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
                Log.e("LanguageDetail", "Error toggling wishlist: " + error.getMessage());
            }
        });
    }
}