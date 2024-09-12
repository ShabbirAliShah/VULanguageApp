package com.example.vulanguageapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.MainActivity;
import com.example.vulanguageapp.adapters.LessonActivityAdapter;
import com.example.vulanguageapp.databinding.FragmentEnrolledCoursesBinding;
import com.example.vulanguageapp.models.Enrollment;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class EnrolledCoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private final List<Enrollment> dataList = new ArrayList<>();
    private NavController navController;
    private View noCoursesContainer;  // Container for the message and button
    private TextView noCoursesMessage;
    private Button goToCoursesButton;

    public EnrolledCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEnrolledCoursesBinding binding = FragmentEnrolledCoursesBinding.inflate(inflater, container, false);
        recyclerView = binding.enrolledCoursesRv;
        noCoursesContainer = binding.noCoursesContainer; // Assuming you have this container in your layout
        noCoursesMessage = binding.noCoursesMessage; // TextView for the message
        goToCoursesButton = binding.goToCoursesButton; // Button to go to courses list

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        navController = NavHostFragment.findNavController(this);

        // Setup button click listener
        goToCoursesButton.setOnClickListener(v -> {
            // Navigate to the courses list
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            // Optionally, finish the current fragment's activity if you don't want users to go back to it
            getActivity().finish();
        });

        fetchCourses();

        return binding.getRoot();
    }

    private void fetchCourses() {

        DatabaseReference lessonsRef = FirebaseDatabase.getInstance().getReference("enrollments");

        lessonsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();

                for (DataSnapshot enrollmentSnapshot : snapshot.getChildren()) {
                    Enrollment enrollment = new Enrollment();

                    enrollment.setCourseId(enrollmentSnapshot.child("courseId").getValue(String.class));
                    enrollment.setCourseTitle(enrollmentSnapshot.child("courseTitle").getValue(String.class));
                    enrollment.setUserId(enrollmentSnapshot.child("userId").getValue(String.class));

                    DataSnapshot selectedLessonsSnapshot = enrollmentSnapshot.child("selectedLessons");
                    HashMap<String, Boolean> selectedLessons = new HashMap<>();

                    for (DataSnapshot lessonSnapshot : selectedLessonsSnapshot.getChildren()) {
                        String lessonId = lessonSnapshot.getKey();
                        Boolean isCompleted = lessonSnapshot.child("isCompleted").getValue(Boolean.class);

                        if (isCompleted != null) {
                            selectedLessons.put(lessonId, isCompleted);
                        }
                    }

                    enrollment.setSelectedLessons(selectedLessons);

                    // Add the enrollment to your list
                    dataList.add(enrollment);
                }

                if (dataList.isEmpty()) {
                    // No enrolled courses, show message and button
                    noCoursesContainer.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    noCoursesMessage.setText("You are not enrolled in any course yet.");
                } else {
                    // Enrolled courses exist, show the RecyclerView
                    noCoursesContainer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    // Set up the adapter and notify data changes
                    LessonActivityAdapter adapter = new LessonActivityAdapter(dataList, navController);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EnrolledCoursesFragment", "Failed to read lesson data.", error.toException());
            }
        });
    }
}
