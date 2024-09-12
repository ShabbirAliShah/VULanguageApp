package com.example.vulanguageapp.fragments;

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

import com.example.vulanguageapp.databinding.FragmentTopicToEnrollBinding;
import com.example.vulanguageapp.models.Enrollment;
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

public class TopicToEnrollFragment extends Fragment {

    private List<LessonsModel> dataList2 = new ArrayList<>();
    private String userId;
    private String courseId;
    private String courseTitle;
    private RecyclerView lessonsRecyclerView;
    private boolean isAlreadyEnrolled = false;
    private HashMap<String, Boolean> selectedLessons = new HashMap<>();
    private Button continueToCourse, enrollNow;
    private FragmentTopicToEnrollBinding binding;
    private TextView enrollmentStatusMessage;

    public TopicToEnrollFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            dataList2 = (List<LessonsModel>) getArguments().getSerializable("lessonsData");
            userId = getArguments().getString("userId");
            courseId = getArguments().getString("courseId");
            courseTitle = getArguments().getString("course_title");

            Log.d("EnrollmentFragment", "DataList2: " + dataList2);
            Log.d("EnrollmentFragment", "UserId: " + userId);
            Log.d("EnrollmentFragment", "CourseId: " + courseId);
            Log.d("EnrollmentFragment", "course Title: " + courseTitle);
        } else {
            Log.w("EnrollmentFragment", "Arguments are null.");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("EnrollmentFragment", "onCreateView called");

        binding = FragmentTopicToEnrollBinding.inflate(inflater, container, false);

        lessonsRecyclerView = binding.topicToEnroll;
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        enrollNow = binding.setEnroll;
        continueToCourse = binding.continueToCourse;
        enrollmentStatusMessage = binding.enrollmentStatusMessage;

        continueToCourse.setVisibility(View.GONE); // Hide by default
        enrollmentStatusMessage.setVisibility(View.GONE); // Hide by default

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkAndSetCourseEnrollment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("EnrollmentFragment", "onDestroyView called");
        lessonsRecyclerView = null;
    }

    public void checkAndSetCourseEnrollment() {

        Query databaseReference = FirebaseDatabase.getInstance().getReference("enrollments").orderByChild("courseId").equalTo(courseId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    isAlreadyEnrolled = true;
                    enrollNow.setVisibility(View.GONE);
                    lessonsRecyclerView.setVisibility(View.GONE);
                    continueToCourse.setVisibility(View.VISIBLE);
                    enrollmentStatusMessage.setVisibility(View.VISIBLE);
                    enrollmentStatusMessage.setText("You are already enrolled in this course.");

                    Toast.makeText(getContext(), "Snapshot exists, course is enrolled", Toast.LENGTH_SHORT).show();
                } else {

                    // User is not enrolled, proceed with enrollment
                    Log.d("EnrollmentFragment", "User is not enrolled. Proceeding with enrollment.");
                    enrollNow.setVisibility(View.VISIBLE);
                    continueToCourse.setVisibility(View.GONE);
                    enrollmentStatusMessage.setVisibility(View.GONE);

                    for (LessonsModel lesson : dataList2) {
                        if (lesson.isSelected()) {
                            selectedLessons.put(lesson.getLessonId(), true);  // Use 'put' to add lessonId and its completion status
                            Log.d("EnrollmentFragment", "Selected Lesson id: " + lesson.getLessonId());
                        } else {
                            selectedLessons.put(lesson.getLessonId(), false);  // If not selected, you can mark it as false
                        }
                    }

                    Log.d("EnrollmentFragment", "Selected Lessons: " + selectedLessons);

                    Enrollment newEnrollment = new Enrollment(courseId, courseTitle, selectedLessons, userId);

                    DatabaseReference enrollmentRef = FirebaseDatabase.getInstance().getReference("enrollments");

                    String key = enrollmentRef.push().getKey();
                    Log.d("EnrollmentFragment", "Generated Key: " + key);

                    if (key != null) {
                        enrollmentRef.child(key).setValue(newEnrollment).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Data insertion successful
                                    Toast.makeText(getContext(), "Enrollment saved successfully", Toast.LENGTH_SHORT).show();
                                    Log.d("EnrollmentFragment", "Enrollment saved successfully.");

                                    // Clear the data and update UI
                                    selectedLessons.clear();
                                    enrollNow.setVisibility(View.GONE);
                                    lessonsRecyclerView.setVisibility(View.GONE);
                                    continueToCourse.setVisibility(View.VISIBLE);
                                    enrollmentStatusMessage.setText("Now You are enrolled in this course.");
                                    enrollmentStatusMessage.setVisibility(View.VISIBLE);

                                } else {
                                    // Data insertion failed
                                    Toast.makeText(getContext(), "Failed to save enrollment", Toast.LENGTH_SHORT).show();
                                    Log.e("EnrollmentFragment", "Failed to save enrollment", task.getException());
                                }
                            }
                        });
                    } else {
                        Log.e("EnrollmentFragment", "Key generation failed.");
                    }

                    Toast.makeText(getContext(), "You are not enrolled in this course", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
