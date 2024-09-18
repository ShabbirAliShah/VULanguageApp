package com.example.vulanguageapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.activities.StudyActivity;
import com.example.vulanguageapp.adapters.EnrollmentTopicListAdapter;
import com.example.vulanguageapp.databinding.FragmentEnrollmentBinding;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.example.vulanguageapp.models.EnrollmentModel;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EnrollmentFragment extends Fragment {

    private List<LessonsModel> lessonsData = new ArrayList<>();
    private String courseId;
    private String courseTitle;
    private String userId;
    private UserIdProvider userIdProvider;
    private Map<String, EnrollmentModel.Lesson> selectedLessons = new HashMap<>();
    private RecyclerView lessonsRecyclerView;
    private Button backToDetail, enrollNow;
    private TextView enrollmentStatusMessage;
    private Boolean isEnrolled = false;

    public EnrollmentFragment(){

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = userIdProvider.getUserId();

        Bundle args = getArguments();
        if (args != null) {

            courseTitle = args.getString("course_title");
            courseId = args.getString("course_Id");

            Toast.makeText(getContext(), "courseid before use is " + courseId, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        FragmentEnrollmentBinding binding = FragmentEnrollmentBinding.inflate(inflater, container, false);

        lessonsRecyclerView = binding.topicToEnroll;
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        enrollNow = binding.setEnroll;
        backToDetail = binding.goBack;
        enrollmentStatusMessage = binding.enrollmentStatusMessage;

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);

        if (checkSetEnrollment() != true){

            fetchLessonList();

            if (courseId == null || courseId.isEmpty()) {

               Toast.makeText(getContext(), "Invalid course ID", Toast.LENGTH_SHORT).show();
            }else {

                enrollNow.setOnClickListener(v-> setEnrollment());
            }

        }
    }

    private Boolean checkSetEnrollment() {
        Query courseReferenceQuery = FirebaseDatabase.getInstance().getReference("enrollments").orderByKey().equalTo(courseId);
        courseReferenceQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Course is enrolled
                    Toast.makeText(getContext(), "Course is already enrolled", Toast.LENGTH_SHORT).show();
                    backToDetail.setVisibility(View.VISIBLE);
                    enrollmentStatusMessage.setText("You are already enrolled in this course.");
                    enrollmentStatusMessage.setVisibility(View.VISIBLE);
                    isEnrolled = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EnrollmentFragment", "Database query cancelled", error.toException());
            }
        });

        return isEnrolled;
    }

    private void fetchLessonList(){

        Query query = FirebaseDatabase.getInstance().getReference("lessons").orderByChild("courseId").equalTo(courseId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                lessonsData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    LessonsModel lessonsModel = dataSnapshot.getValue(LessonsModel.class);
                    String key = dataSnapshot.getKey();
                    Objects.requireNonNull(lessonsModel).setLessonId(key);
                    if (lessonsModel != null) {
                        lessonsData.add(lessonsModel);
                    }
                }

                EnrollmentTopicListAdapter enrollmentTopicListAdapter = new EnrollmentTopicListAdapter(lessonsData, getContext(), true);
                lessonsRecyclerView.setAdapter(enrollmentTopicListAdapter);

                enrollmentTopicListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log the error if the query is cancelled or fails
                Log.e("FirebaseDebug", "DatabaseError: " + error.getMessage());
            }
        });
    }

    private void setEnrollment() {
        selectedLessons.clear();

        // Collect selected lessons

        for (LessonsModel lesson : lessonsData) {
            if (lesson.isSelected()) {
                selectedLessons.put(lesson.getLessonId(), new EnrollmentModel.Lesson(false, true));
            } else {
                selectedLessons.put(lesson.getLessonId(), new EnrollmentModel.Lesson(false, false));
            }
        }

        // Save the enrollment
        EnrollmentModel newEnrollment = new EnrollmentModel(courseId, courseTitle, userId, selectedLessons);
        DatabaseReference enrollmentRef = FirebaseDatabase.getInstance().getReference("enrollments");

        String enrollmentId = enrollmentRef.child("enrollments").push().getKey();

        assert enrollmentId != null;
        enrollmentRef.child(enrollmentId).setValue(newEnrollment).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Enrollment saved successfully", Toast.LENGTH_SHORT).show();
                    selectedLessons.clear(); // Clear the selected lessons
                    enrollNow.setVisibility(View.GONE); // Hide enroll button
                    lessonsRecyclerView.setVisibility(View.GONE); // Hide lessons
                    backToDetail.setVisibility(View.VISIBLE); // Show continue button
                    enrollmentStatusMessage.setText("Now you are enrolled in this course.");
                    enrollmentStatusMessage.setVisibility(View.VISIBLE); // Show enrollment status message

                    backToDetail.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), StudyActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    });
                } else {
                    Toast.makeText(getContext(), "Failed to save enrollment", Toast.LENGTH_SHORT).show();
                    Log.e("EnrollmentFragment", "Failed to save enrollment", task.getException());
                }
            }
        });
    }
}