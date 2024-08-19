package com.example.vulanguageapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.adapters.CourseContentAdapter;
import com.example.vulanguageapp.databinding.FragmentTopicsToStudyBinding;
import com.example.vulanguageapp.models.Enrollment;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicsToStudyFragment extends Fragment{

    private FragmentTopicsToStudyBinding binding;
    private List<LessonsModel> dataList = new ArrayList<>();
    private final List<String> lessonIds = new ArrayList<>();
    private RecyclerView recyclerView;
    private String courseId;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            courseId = getArguments().getString("course_id");

        }

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentTopicsToStudyBinding.inflate(inflater, container, false);

        recyclerView = binding.courseContent;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        navController = NavHostFragment.findNavController(this);

        fetchLessonIds();
        return binding.getRoot();
    }

    private void fetchLessonIds() {
        DatabaseReference enrollmentsRef = FirebaseDatabase.getInstance().getReference("enrollments");

        enrollmentsRef.orderByChild("courseId").equalTo(courseId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lessonIds.clear();
                for (DataSnapshot enrollmentSnapshot : snapshot.getChildren()) {
                    Enrollment enrollment = enrollmentSnapshot.getValue(Enrollment.class);
                    if (enrollment != null && enrollment.getSelectedLessons() != null) {
                        lessonIds.addAll(enrollment.getSelectedLessons());
                    }
                }
                // Fetch lessons data after retrieving lesson IDs
                fetchLessonsData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EnrolledCoursesFragment", "Failed to read lesson IDs.", error.toException());
            }
        });
    }

    private void fetchLessonsData() {
        DatabaseReference lessonsRef = FirebaseDatabase.getInstance().getReference("lessons");

        // Create a map to hold lesson IDs
        Map<String, Object> lessonIdMap = new HashMap<>();
        for (String lessonId : lessonIds) {
            lessonIdMap.put(lessonId, true);
        }

        lessonsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot lessonSnapshot : snapshot.getChildren()) {
                    String lessonId = lessonSnapshot.getKey();
                    if (lessonIdMap.containsKey(lessonId)) {
                        LessonsModel lesson = lessonSnapshot.getValue(LessonsModel.class);
                        if (lesson != null) {
                            dataList.add(lesson);
                        }
                    }
                }

                CourseContentAdapter adapter = new CourseContentAdapter(dataList, navController);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EnrolledCoursesFragment", "Failed to read lesson data.", error.toException());
            }
        });
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}