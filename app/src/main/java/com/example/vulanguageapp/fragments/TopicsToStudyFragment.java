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

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.CourseContentAdapter;
import com.example.vulanguageapp.databinding.FragmentTopicsToStudyBinding;
import com.example.vulanguageapp.models.EnrollmentModel;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicsToStudyFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<LessonsModel> selectedLessonsList = new ArrayList<>();
    private CourseContentAdapter courseContentAdapter;
    private NavController navController;
    private ArrayList<EnrollmentModel> enrolledDataList;
    List<String> selectedLessonIds;
    private String courseId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        navController = NavHostFragment.findNavController(this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topics_to_study, container, false);
        recyclerView = view.findViewById(R.id.courseContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        enrolledDataList = (ArrayList<EnrollmentModel>) getArguments().getSerializable("dataArray");

        if (enrolledDataList != null) {
            fetchSelectedLessons(enrolledDataList);
        }

        return view;
    }

    private void fetchSelectedLessons(ArrayList<EnrollmentModel> enrolledDataList) {
        for (EnrollmentModel enrollment : enrolledDataList) {
            courseId = enrollment.getCourseId();
            Map<String, EnrollmentModel.Lesson> lessonsMap = enrollment.getSelectedLessons();

            // Create a list of lesson IDs where 'selected' is true
            selectedLessonIds = new ArrayList<>();
            for (Map.Entry<String, EnrollmentModel.Lesson> entry : lessonsMap.entrySet()) {
                EnrollmentModel.Lesson lesson = entry.getValue();
                if (lesson.isSelected()) {
                    selectedLessonIds.add(entry.getKey()); // Add only selected lesson IDs
                }
            }

            // Query Firebase with the list of selected lessons
            queryLessonFromFirebase(selectedLessonIds, courseId);
        }
    }

    private void queryLessonFromFirebase(List<String> lessonIds, String courseId ) {

        Map<String, Object> lessonIdMap = new HashMap<>();
        for (String lessonId : lessonIds) {
            lessonIdMap.put(lessonId, null);  // Mark the lessonId as true
        }

        DatabaseReference lessonsRef = FirebaseDatabase.getInstance().getReference("lessons");
        Query lessonQuery = lessonsRef.orderByChild("courseId").equalTo(courseId);

        lessonQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedLessonsList.clear();

                // Loop through Firebase snapshot
                for (DataSnapshot lessonSnapshot : snapshot.getChildren()) {
                    String lessonId = lessonSnapshot.getKey();
                    if (lessonIdMap.containsKey(lessonId)) {  // Check if the lessonId matches
                        LessonsModel lesson = lessonSnapshot.getValue(LessonsModel.class);
                        if (lesson != null) {
                            selectedLessonsList.add(lesson);  // Add the lesson to the list
                        }
                    }
                }

                // Set or update the adapter
                if (courseContentAdapter == null) {
                    courseContentAdapter = new CourseContentAdapter(selectedLessonsList, navController, enrolledDataList);
                    recyclerView.setAdapter(courseContentAdapter);
                } else {
                    courseContentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        // Just notify the adapter if it's already set
        if (courseContentAdapter != null) {
            courseContentAdapter.notifyDataSetChanged();
        } else {
            // Fetch the data again if the adapter is null
            queryLessonFromFirebase(selectedLessonIds, courseId);
        }
    }
}
