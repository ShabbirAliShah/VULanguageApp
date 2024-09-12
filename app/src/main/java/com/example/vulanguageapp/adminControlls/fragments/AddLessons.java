package com.example.vulanguageapp.adminControlls.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vulanguageapp.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vulanguageapp.adminControlls.adminAdapters.LessonsAdapter;
import com.example.vulanguageapp.models.CourseModel;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddLessons extends Fragment {

    private ChipGroup chipGroup;
    private List<String> selectedLessons = new ArrayList<>();
    private FirebaseFirestore db;
    private RecyclerView lessonsRecyclerView;
    private String selectedCourseKey;
    private Spinner contentTypeEditText;
    private EditText exerciseEditText, flashCardEditText, audioLinkEditText,
            courseIdEditText, imageLinkEditText, titleEditText, videoLinkEditText;
    private ArrayList<CourseModel> dataList = new ArrayList<>();
    private List<LessonsModel> lessonArray = new ArrayList();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fetchCoursesFromFirebase();

        return inflater.inflate(R.layout.fragment_add__lessons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        chipGroup = view.findViewById(R.id.chipGroup);
        audioLinkEditText = view.findViewById(R.id.audioLinkEditText);
        contentTypeEditText = view.findViewById(R.id.contentTypeEditText);
        courseIdEditText = view.findViewById(R.id.courseIdEditText);
        imageLinkEditText = view.findViewById(R.id.imageLinkEditText);
        titleEditText = view.findViewById(R.id.titleEditText);
        videoLinkEditText = view.findViewById(R.id.videoLinkEditText);
        exerciseEditText = view.findViewById(R.id.exercise);
        flashCardEditText = view.findViewById(R.id.flashcard);

        courseIdEditText.setFocusable(false);

        courseIdEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCourseSelectionDialog();
            }
        });

        titleEditText.setOnClickListener(v -> showBottomSheetDialog());


        Button insertButton = view.findViewById(R.id.insertButton);
        insertButton.setOnClickListener(v -> insertLessonData());
    }

    private void insertLessonData() {
        // Get data from EditTexts
        String audioLink = audioLinkEditText.getText().toString();
        String contentType = contentTypeEditText.getSelectedItem().toString();
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
        // Reference to the Firebase Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("lessons");
        String lessonKey = database.push().getKey();

        // Create a LessonsModel object with your data
        LessonsModel newLesson = new LessonsModel(lessonKey, audioLink, contentType, courseId, imageLink, title, videoLink, false, exercises, flashcards, false);

        Log.d("AddLessons", "New lesson created with key: " + lessonKey);

        // Insert data into the 'lessons' node using the generated key
        database.child(lessonKey).setValue(newLesson).addOnSuccessListener(aVoid -> {
            // Handle success
            Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
            Log.d("AddLessons", "Data inserted successfully.");
        }).addOnFailureListener(e -> {
            // Handle failure
            Toast.makeText(requireContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
            Log.e("AddLessons", "Failed to insert data.", e);
        });
    }

    private void showCourseSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select a Course");

        // Convert course titles to a string array
        String[] courseTitles = new String[dataList.size()];
        for (int courses = 0; courses < dataList.size(); courses++) {
            courseTitles[courses] = dataList.get(courses).getTitle();
        }

        // Set up the dialog with a list of courses
        builder.setItems(courseTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When a course is selected, update the EditText with the selected course title
                courseIdEditText.setText(courseTitles[which]);
                selectedCourseKey = dataList.get(which).getCourseKey();
            }
        });

        builder.show();
    }

    private void fetchCoursesFromFirebase() {
        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference("courses");

        coursesRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CourseModel course = snapshot.getValue(CourseModel.class);
                    dataList.add(course);
                }

                if (dataSnapshot.exists()){

                    CourseModel courseModel = new CourseModel();
                    String courseKey = courseModel.getCourseKey();
                    Log.d("fetch course funcion","course key is " + courseKey);
                    Toast.makeText(getContext(), "course key is " + courseKey , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void queryLesson() {
        Toast.makeText(getContext(), "selectedCourseKey QueryLesson Function" + selectedCourseKey, Toast.LENGTH_SHORT).show();
        Log.d("selectedCourseKey query function", "selectedCourseKey is " + selectedCourseKey);

        Query lessonQuery = FirebaseDatabase.getInstance().getReference("lessons").orderByChild("courseId").equalTo(selectedCourseKey);

        lessonQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lessonArray.clear();

                LessonsAdapter lessonsAdapter = new LessonsAdapter(lessonArray, selectedLessons -> updateInputField(selectedLessons));
                lessonsRecyclerView.setAdapter(lessonsAdapter);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LessonsModel lessonsModel = dataSnapshot.getValue(LessonsModel.class);

                    if (lessonsModel != null) {
                        lessonArray.add(lessonsModel);
                    }
                }

                lessonsAdapter.notifyDataSetChanged();
                Log.d("Addlesson", "array size" + lessonArray.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }


    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(sheetView);

        lessonsRecyclerView = sheetView.findViewById(R.id.bottomSheetRecyclerView);
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        queryLesson();

        bottomSheetDialog.show();
    }

    private void updateInputField(List<LessonsModel> selectedLessons) {
        List<String> selectedLessonNames = new ArrayList<>();
        for (LessonsModel lesson : selectedLessons) {
            selectedLessonNames.add(lesson.getLessonId());
        }

        titleEditText.setText(TextUtils.join(", ", selectedLessonNames));
    }

}
