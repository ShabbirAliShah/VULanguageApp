package com.example.vulanguageapp.adminControlls.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.CourseModel;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class Create_Course extends Fragment {

    private FirebaseFirestore db;
    private EditText languageName, lessonTitle, lessonList, level, description;


    public Create_Course() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create__course, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        languageName = view.findViewById(R.id.languageName);
        lessonTitle = view.findViewById(R.id.addlessonTitle);
        lessonList = view.findViewById(R.id.addLessonList);
        description = view.findViewById(R.id.course_Descripton);

        level = view.findViewById(R.id.level);

        Button save = view.findViewById(R.id.save);
        save.setOnClickListener(v -> insertCourse());
    }

    private void insertCourse() {
        // Get data from EditTexts
        String language = languageName.getText().toString();
        String lesson_title = lessonTitle.getText().toString();
        String courseDescription = description.getText().toString();
        String proficiency = level.getText().toString();
        String lesson_list = lessonList.getText().toString().trim();

        Log.d("Create Course", "language name: " + language);
        Log.d("Create Course", "lesson title: " + lesson_title);
        Log.d("Create Course", "lesson_list Raw: " + lesson_list);
        Log.d("Create Course", "proficiency: " + proficiency);


        // Split the text into a list if it's comma-separated
        List<String> lessonlist = Arrays.asList(lesson_list.split("\\s*,\\s*"));

        Log.d("Create Course", "Parsed lessonlist: " + lessonlist);

        // Check if the lists are empty or not
        if (lessonlist.isEmpty()) {
            Log.d("Create Course", "lesson list is empty.");
        } else {
            Log.d("Create Course", "lesson list size: " + lessonlist.size());
        }


        // Reference to the Firebase Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("courses");
        String lessonKey = database.push().getKey();

        // Create a LessonsModel object with your data
        CourseModel newCourse = new CourseModel(language,proficiency, lesson_title,lessonKey,lessonlist, courseDescription);

        Log.d("AddLessons", "New lesson created with key: " + lessonKey);

        // Insert data into the 'lessons' node using the generated key
        database.child(lessonKey).setValue(newCourse).addOnSuccessListener(aVoid -> {
            // Handle success
            Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
            Log.d("AddLessons", "Data inserted successfully.");
        }).addOnFailureListener(e -> {
            // Handle failure
            Toast.makeText(requireContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
            Log.e("AddLessons", "Failed to insert data.", e);
        });

        // Get Firestore instance
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//// Generate a new document ID
//        String lessonKey = db.collection("lessons").document().getId();
//
//// Create a LessonsModel object with your data
//        LessonsModel newLesson = new LessonsModel(lessonKey, audioLink, contentType, courseId, imageLink, title, videoLink, false, exercises, flashcards);
//
//        Log.d("AddLessons", "New lesson created with key: " + lessonKey);
//
//// Insert data into the 'lessons' collection using the generated key
//        db.collection("lessons").document(lessonKey).set(newLesson)
//                .addOnSuccessListener(aVoid -> {
//                    // Handle success
//                    Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
//                    Log.d("AddLessons", "Data inserted successfully.");
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure
//                    Toast.makeText(requireContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
//                    Log.e("AddLessons", "Failed to insert data.", e);
//                });
    }
}