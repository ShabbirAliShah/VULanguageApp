package com.example.vulanguageapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
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

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.ViewLectureActivity;
import com.example.vulanguageapp.adapters.EnrollmentTopicListAdapter;
import com.example.vulanguageapp.models.Enrollment;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopicToEnrollFragment extends Fragment {

    private List<LessonsModel> dataList2 = new ArrayList<>();
    private String userId;
    private String courseId;
    private String courseTitle;
    private RecyclerView lessonsRecyclerView;
    private boolean isAlreadyEnrolled = false;
    private List<String> selectedLessons = new ArrayList<>();
    private Button continueToCourse;

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

        View view = inflater.inflate(R.layout.fragment_topic_to_enroll, container, false);

        lessonsRecyclerView = view.findViewById(R.id.topic_list_to_enroll);
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Button enrollNow = view.findViewById(R.id.setEnroll);
        continueToCourse = view.findViewById(R.id.continueToCourse);
        TextView enrollmentStatusMessage = view.findViewById(R.id.enrollmentStatusMessage);

        continueToCourse.setVisibility(View.GONE); // Hide by default
        enrollmentStatusMessage.setVisibility(View.GONE); // Hide by default

        continueToCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getActivity(), ViewLectureActivity.class);
                startActivity(intent);
            }
        });

        // Query to check if the user is already enrolled in the course
        databaseReference.child("enrollments").orderByChild("courseId").equalTo(courseId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Enrollment existingEnrollment = snapshot.getValue(Enrollment.class);
                    Log.d("EnrollmentFragment", "Existing Enrollment: " + existingEnrollment);

                    if (existingEnrollment != null && existingEnrollment.getUserId().equals(userId)) {

                        isAlreadyEnrolled = true;
                        break;
                    }
                }

                if (isAlreadyEnrolled) {
                    // User is already enrolled
                    enrollNow.setVisibility(View.GONE);
                    lessonsRecyclerView.setVisibility(View.GONE);
                    continueToCourse.setVisibility(View.VISIBLE);
                    enrollmentStatusMessage.setText("You are already enrolled in this course.");
                    enrollmentStatusMessage.setVisibility(View.VISIBLE);

                    Log.d("EnrollmentFragment", "User is already enrolled.");
                } else {
                    // User is not enrolled, proceed with enrollment
                    Log.d("EnrollmentFragment", "User is not enrolled. Proceeding with enrollment.");
                    enrollNow.setVisibility(View.VISIBLE);
                    continueToCourse.setVisibility(View.GONE);
                    enrollmentStatusMessage.setVisibility(View.GONE);

                    enrollNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (LessonsModel lesson : dataList2) {
                                if (lesson.isSelected()) {
                                    selectedLessons.add(lesson.getLessonId());
                                    Log.d("EnrollmentFragment", "Selected Lesson id: " + lesson.getLessonId());
                                }
                            }

                            Log.d("EnrollmentFragment", "Selected Lessons: " + selectedLessons);

                            Enrollment newEnrollment = new Enrollment(courseId, courseTitle, selectedLessons, userId);

                            DatabaseReference enrollmentRef = FirebaseDatabase.getInstance().getReference().child("enrollments");

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
                                            enrollmentStatusMessage.setText("You are now enrolled in this course.");
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
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(getContext(), "Error checking enrollment status", Toast.LENGTH_SHORT).show();
                Log.e("EnrollmentFragment", "Error checking enrollment status", databaseError.toException());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isAlreadyEnrolled) {
            if (dataList2 != null && !dataList2.isEmpty()) {
                Log.d("EnrollmentFragment", "Data list size: " + dataList2.size());

                // Set adapter
                EnrollmentTopicListAdapter lessonsAdapter = new EnrollmentTopicListAdapter(dataList2, requireContext(), true);
                lessonsRecyclerView.setAdapter(lessonsAdapter);

                // Notify adapter of data changes if needed
                lessonsAdapter.notifyDataSetChanged();
            } else {
                Log.e("EnrollmentFragment", "Data list is empty or null.");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("EnrollmentFragment", "onDestroyView called");
        lessonsRecyclerView = null;
    }
}
