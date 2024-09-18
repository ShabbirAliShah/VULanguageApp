package com.example.vulanguageapp.adminControlls.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vulanguageapp.adminControlls.activities.PracticeActivity;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddLessons extends Fragment {

    private RecyclerView lessonsRecyclerView;
    private String selectedCourseKey, videoUrl;
    private Spinner contentTypeEditText;
    private AutoCompleteTextView courseTitle;
    private EditText courseIdHidden, exerciseEditText, flashCardEditText, audioLinkEditText, imageLinkEditText, titleEditText, videoLinkEditText;
    private ArrayList<CourseModel> dataList = new ArrayList<>();
    //private List<LessonsModel> lessonArray = new ArrayList();
    private static final int PICK_VIDEO_REQUEST = 1;
    private Uri videoUri;  // To hold the selected video URI
    private Button pickVideo, btnUploadVideo;
    private VideoView videoPreview;
    private ProgressBar progressBar;
    private TextView uploadStatusTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Check and request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_MEDIA_VIDEO}, 101);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
        }

        return inflater.inflate(R.layout.fragment_add__lessons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pickVideo = view.findViewById(R.id.btn_select_video);
        btnUploadVideo = view.findViewById(R.id.btn_upload_video);
        videoPreview = view.findViewById(R.id.video_view);
        progressBar = view.findViewById(R.id.progress_bar);
        uploadStatusTextView = view.findViewById(R.id.tv_upload_status);

        audioLinkEditText = view.findViewById(R.id.audioLinkEditText);
        contentTypeEditText = view.findViewById(R.id.contentTypeEditText);
        courseIdHidden = view.findViewById(R.id.courseIdHidden);
        courseTitle = view.findViewById(R.id.addLessonCourseTitle);
        imageLinkEditText = view.findViewById(R.id.imageLinkEditText);
        titleEditText = view.findViewById(R.id.titleEditText);
        exerciseEditText = view.findViewById(R.id.exercise);
        flashCardEditText = view.findViewById(R.id.flashcard);

        pickVideo.setOnClickListener(v -> {
            openVideoPicker();  // Only opens the video picker when the button is clicked
        });

        // Set click listener for the "Upload Video" button
        btnUploadVideo.setOnClickListener(v -> {

                uploadVideo();

        });


        fetchCoursesFromFirebase();

        courseTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseNameAutoComplete();
            }
        });

        //titleEditText.setOnClickListener(v -> showBottomSheetDialog());

        Button insertButton = view.findViewById(R.id.insertButton);
        insertButton.setOnClickListener(v -> insertLessonData());
    }

    private void insertLessonData() {
        // Get data from EditTexts
        String audioLink = audioLinkEditText.getText().toString();
        String contentType = contentTypeEditText.getSelectedItem().toString();
        String courseId = courseIdHidden.getText().toString();
        String imageLink = imageLinkEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String videoLink = videoUrl;

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
        LessonsModel newLesson = new LessonsModel(lessonKey, audioLink, contentType, courseId, imageLink, title, videoLink, (ArrayList<String>) exercises, flashcards);

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

    private void courseNameAutoComplete() {
        // Create a list of course titles and keys
        String[] courseTitles = new String[dataList.size()];
        final String[] courseKeys = new String[dataList.size()];

        for (int courses = 0; courses < dataList.size(); courses++) {
            courseTitles[courses] = dataList.get(courses).getTitle();
            courseKeys[courses] = dataList.get(courses).getCourseKey();
        }

        // Set up the autocomplete EditText
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, courseTitles);
        courseTitle.setAdapter(adapter);
        courseTitle.setThreshold(1); // Start suggesting after 1 character

        // Listen for item selection
        courseTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected course title and key
                String selectedCourseTitle = (String) parent.getItemAtPosition(position);
                int selectedIndex = Arrays.asList(courseTitles).indexOf(selectedCourseTitle);
                selectedCourseKey = courseKeys[selectedIndex];

                // Optionally, filter the list to show only the selected item
                courseIdHidden.setText(selectedCourseKey);
            }
        });
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

//    private void queryLesson() {
//        Toast.makeText(getContext(), "selectedCourseKey QueryLesson Function" + selectedCourseKey, Toast.LENGTH_SHORT).show();
//        Log.d("selectedCourseKey query function", "selectedCourseKey is " + selectedCourseKey);
//
//        Query lessonQuery = FirebaseDatabase.getInstance().getReference("lessons").orderByChild("courseId").equalTo(selectedCourseKey);
//
//        lessonQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                lessonArray.clear();
//
//                LessonsAdapter lessonsAdapter = new LessonsAdapter(lessonArray, selectedLessons -> updateInputField(selectedLessons));
//                lessonsRecyclerView.setAdapter(lessonsAdapter);
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    LessonsModel lessonsModel = dataSnapshot.getValue(LessonsModel.class);
//
//                    if (lessonsModel != null) {
//                        lessonArray.add(lessonsModel);
//                    }
//                }
//
//                lessonsAdapter.notifyDataSetChanged();
//                Log.d("Addlesson", "array size" + lessonArray.size());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle possible errors
//            }
//        });
//    }


//    private void showBottomSheetDialog() {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
//        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, null);
//        bottomSheetDialog.setContentView(sheetView);
//
//        lessonsRecyclerView = sheetView.findViewById(R.id.bottomSheetRecyclerView);
//        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        queryLesson();
//
//        bottomSheetDialog.show();
//    }

//    private void updateInputField(List<LessonsModel> selectedLessons) {
//        List<String> selectedLessonNames = new ArrayList<>();
//        for (LessonsModel lesson : selectedLessons) {
//            selectedLessonNames.add(lesson.getLessonId());
//        }
//
//        titleEditText.setText(TextUtils.join(", ", selectedLessonNames));
//    }


    // Function to open the gallery for selecting a video
    private void openVideoPicker() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
        videoPreview.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    // Function to handle the result of the video picker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();  // Get the selected video URI
            btnUploadVideo.setEnabled(true);  // Enable the upload button after a video is selected
            videoPreview.setVideoURI(videoUri);
        }
    }

    // Function to upload the selected video
    private void uploadVideo() {
        if (videoUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference videoRef = storageRef.child("videos/" + System.currentTimeMillis() + ".mp4");

            // Show the ProgressBar and TextView when the upload starts
            progressBar.setVisibility(View.VISIBLE);
            uploadStatusTextView.setVisibility(View.VISIBLE);

            // Start the upload and track progress
            UploadTask uploadTask = videoRef.putFile(videoUri);

            // Listen for upload progress
            uploadTask.addOnProgressListener(taskSnapshot -> {
                // Get the current upload progress in bytes
                long bytesTransferred = taskSnapshot.getBytesTransferred();
                long totalBytes = taskSnapshot.getTotalByteCount();

                // Calculate the upload percentage
                int progress = (int) ((bytesTransferred * 100) / totalBytes);

                // Update the ProgressBar and TextView
                progressBar.setProgress(progress);
                uploadStatusTextView.setText(progress + "% (" + bytesTransferred / 1024 + "KB/" + totalBytes / 1024 + "KB)");

            }).addOnSuccessListener(taskSnapshot -> {
                // Hide the ProgressBar after success
                progressBar.setVisibility(View.GONE);
                uploadStatusTextView.setText("Upload complete!");

                // Get the download URL
                videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    videoUrl = uri.toString();
                    // Use this URL to store it in your database
                });

            }).addOnFailureListener(e -> {
                // Hide the ProgressBar and show failure message
                progressBar.setVisibility(View.GONE);
                uploadStatusTextView.setText("Upload failed: " + e.getMessage());
                Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(getContext(), "No video selected", Toast.LENGTH_SHORT).show();
        }
    }
}
