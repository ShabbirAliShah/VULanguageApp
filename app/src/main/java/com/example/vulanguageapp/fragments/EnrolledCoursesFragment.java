package com.example.vulanguageapp.fragments;

import android.annotation.SuppressLint;
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
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        navController = NavHostFragment.findNavController(this);
        //fetchLessonIds();

        fetchCourses();

        return binding.getRoot();
    }

//    private void fetchLessonIds() {
//        DatabaseReference enrollmentsRef = FirebaseDatabase.getInstance().getReference("enrollments");
//
//        enrollmentsRef.orderByChild("courseId").equalTo(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                lessonIds.clear();
//                for (DataSnapshot enrollmentSnapshot : snapshot.getChildren()) {
//                    Enrollment enrollment = enrollmentSnapshot.getValue(Enrollment.class);
//                    if (enrollment != null && enrollment.getSelectedLessons() != null) {
//                        lessonIds.addAll(enrollment.getSelectedLessons());
//                    }
//                }
//                // Fetch lessons data after retrieving lesson IDs
//                fetchLessonsData();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("EnrolledCoursesFragment", "Failed to read lesson IDs.", error.toException());
//            }
//        });
//    }

    private void fetchCourses() {
        DatabaseReference lessonsRef = FirebaseDatabase.getInstance().getReference("enrollments");

        lessonsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {

                        Enrollment enrolledCourses = courseSnapshot.getValue(Enrollment.class);
                        if (enrolledCourses != null) {
                            dataList.add(enrolledCourses);
                        }
                }

                LessonActivityAdapter adapter = new LessonActivityAdapter(dataList, navController);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EnrolledCoursesFragment", "Failed to read lesson data.", error.toException());
            }
        });
    }
}
