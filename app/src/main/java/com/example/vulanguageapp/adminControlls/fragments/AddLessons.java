package com.example.vulanguageapp.adminControlls.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vulanguageapp.R;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vulanguageapp.fragments.ChatConversationFragment;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddLessons extends Fragment {

    private FirebaseFirestore db;
    private EditText exerciseEditText, flashCardEditText, audioLinkEditText, contentTypeEditText, courseIdEditText, imageLinkEditText, titleEditText, videoLinkEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_add__lessons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        audioLinkEditText = view.findViewById(R.id.audioLinkEditText);
        contentTypeEditText = view.findViewById(R.id.contentTypeEditText);
        courseIdEditText = view.findViewById(R.id.courseIdEditText);
        imageLinkEditText = view.findViewById(R.id.imageLinkEditText);
        titleEditText = view.findViewById(R.id.titleEditText);
        videoLinkEditText = view.findViewById(R.id.videoLinkEditText);
        exerciseEditText = view.findViewById(R.id.exercise);
        flashCardEditText = view.findViewById(R.id.flashcard);

        Button insertButton = view.findViewById(R.id.insertButton);
        insertButton.setOnClickListener(v -> insertLessonData());
    }

    private void insertLessonData() {
        // Get data from EditTexts
        String audioLink = audioLinkEditText.getText().toString();
        String contentType = contentTypeEditText.getText().toString();
        String courseId = courseIdEditText.getText().toString();
        String imageLink = imageLinkEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String videoLink = videoLinkEditText.getText().toString();

        Log.d("AddLessons", "audioLink: " + audioLink);
        Log.d("AddLessons", "contentType: " + contentType);
        Log.d("AddLessons", "courseId: " + courseId);
        Log.d("AddLessons", "imageLink: " + imageLink);
        Log.d("AddLessons", "title: " + title);
        Log.d("AddLessons", "videoLink: " + videoLink);

        // Get the text from EditTexts
        String exerciseText = exerciseEditText.getText().toString().trim();
        String flashcardText = flashCardEditText.getText().toString().trim();

        Log.d("AddLessons", "Raw exercise text: " + exerciseText);
        Log.d("AddLessons", "Raw flashcard text: " + flashcardText);

        // Split the text into a list if it's comma-separated
        List<String> exercises = Arrays.asList(exerciseText.split("\\s*,\\s*"));
        List<String> flashcards = Arrays.asList(flashcardText.split("\\s*,\\s*"));

        Log.d("AddLessons", "Parsed exercises: " + exercises);
        Log.d("AddLessons", "Parsed flashcards: " + flashcards);

        // Check if the lists are empty or not
        if (exercises.isEmpty()) {
            Log.d("AddLessons", "Exercises list is empty.");
        } else {
            Log.d("AddLessons", "Exercises list size: " + exercises.size());
        }

        if (flashcards.isEmpty()) {
            Log.d("AddLessons", "Flashcards list is empty.");
        } else {
            Log.d("AddLessons", "Flashcards list size: " + flashcards.size());
        }

//        // Reference to the Firebase Realtime Database
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("lessons");
//        String lessonKey = database.push().getKey();
//
//        // Create a LessonsModel object with your data
//        LessonsModel newLesson = new LessonsModel(lessonKey, audioLink, contentType, courseId, imageLink, title, videoLink, false, exercises, flashcards);
//
//        Log.d("AddLessons", "New lesson created with key: " + lessonKey);
//
//        // Insert data into the 'lessons' node using the generated key
//        database.child(lessonKey).setValue(newLesson).addOnSuccessListener(aVoid -> {
//            // Handle success
//            Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
//            Log.d("AddLessons", "Data inserted successfully.");
//        }).addOnFailureListener(e -> {
//            // Handle failure
//            Toast.makeText(requireContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
//            Log.e("AddLessons", "Failed to insert data.", e);
//        });

        // Get Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Generate a new document ID
        String lessonKey = db.collection("lessons").document().getId();

// Create a LessonsModel object with your data
        LessonsModel newLesson = new LessonsModel(lessonKey, audioLink, contentType, courseId, imageLink, title, videoLink, false, exercises, flashcards);

        Log.d("AddLessons", "New lesson created with key: " + lessonKey);

// Insert data into the 'lessons' collection using the generated key
        db.collection("lessons").document(lessonKey).set(newLesson)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    Log.d("AddLessons", "Data inserted successfully.");
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
                    Log.e("AddLessons", "Failed to insert data.", e);
                });

    }
}
