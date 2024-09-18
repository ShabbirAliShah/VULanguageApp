package com.example.vulanguageapp.adminControlls.fragments;

import static android.app.Activity.RESULT_OK;
import static com.example.vulanguageapp.R.id.language_Name1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.BaseActivity;
import com.example.vulanguageapp.activities.MainActivity;
import com.example.vulanguageapp.interfaces.BaseActivityInterface;
import com.example.vulanguageapp.models.CourseModel;
import com.example.vulanguageapp.models.FlashCardModel;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Create_Course extends Fragment {

    private EditText lessonTitle, inLanguge, pronunciation, cardBackText, description;
    private Spinner level, languageSpinner, languageSpinner1;
    private static final int PICK_IMAGE = 1;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Uri imageUri;
    private String imageLink;
    private StorageReference storageReference;

    public Create_Course() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fetchLanguageDataAndSetSpinner();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create__course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        languageSpinner = view.findViewById(R.id.language_Name);
        lessonTitle = view.findViewById(R.id.addlessonTitle);
        description = view.findViewById(R.id.course_Descripton);
        level = view.findViewById(R.id.level);
        progressBar = view.findViewById(R.id.progressBar);
        inLanguge = view.findViewById(R.id.inLanguage_Text);
        pronunciation = view.findViewById(R.id.pronunciationText);
        cardBackText = view.findViewById(R.id.textCardBack);
        languageSpinner1 = view.findViewById(language_Name1);

        Button save = view.findViewById(R.id.save);
        Button saveCard = view.findViewById(R.id.saveFlashCard);
        save.setOnClickListener(v -> insertCourse());
        //saveCard.setOnClickListener(v -> insertCard());

        imageView = view.findViewById(R.id.flashCardImage);
        storageReference = FirebaseStorage.getInstance().getReference("flashCardImages");

        saveCard.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImage(); // This will now set imageLink when successful
            } else {
                Toast.makeText(getContext(), "Select an image first", Toast.LENGTH_SHORT).show();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // openGallery();
                openImagePicker();
            }
        });
    }

    private void insertCourse() {
        // Get data from EditTexts
        String language = languageSpinner.getSelectedItem().toString();
        String lesson_title = lessonTitle.getText().toString();
        String courseDescription = description.getText().toString();
        String proficiency = level.getSelectedItem().toString();

        Log.d("Create Course", "language name: " + language);
        Log.d("Create Course", "lesson title: " + lesson_title);
        Log.d("Create Course", "proficiency: " + proficiency);


        // Reference to the Firebase Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("courses");
        String courseKey = database.push().getKey();

        // Create a LessonsModel object with your data
        CourseModel newCourse = new CourseModel(language,proficiency, lesson_title,courseKey, courseDescription);

        Log.d("Create course", "New course created with key: " + courseKey);

        // Insert data into the 'lessons' node using the generated key
        database.child(courseKey).setValue(newCourse).addOnSuccessListener(aVoid -> {
            // Handle success
            Toast.makeText(requireContext(), "Data inserted successfully", Toast.LENGTH_SHORT).show();
            Log.d("AddLessons", "Data inserted successfully.");
        }).addOnFailureListener(e -> {
            // Handle failure
            Toast.makeText(requireContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
            Log.e("AddLessons", "Failed to insert data.", e);
        });
    }

    private void insertCard() {

        String forLanguage = languageSpinner1.getSelectedItem().toString();
        String textPronunciation = pronunciation.getText().toString();
        String textInLanguage = inLanguge.getText().toString();
        String flashCardBackText = cardBackText.getText().toString();
        //int cardImage = image_flashcard;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("flashcards");
        String cardKey = database.push().getKey();

        FlashCardModel newFlashCard = new FlashCardModel(textPronunciation, forLanguage, textInLanguage,flashCardBackText, imageLink);

        database.child(cardKey).setValue(newFlashCard).addOnSuccessListener(aVoid -> {
            // Handle success
            Toast.makeText(requireContext(), "Flashcard created successfully", Toast.LENGTH_SHORT).show();
            Log.d("Creae Course", "Flashard data inserted successfully.");
        }).addOnFailureListener(e -> {
            // Handle failure
            Toast.makeText(requireContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
            Log.e("AddLessons", "Failed to insert data.", e);
        });
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            progressBar.setVisibility(View.VISIBLE); // Show the progress bar

            UploadTask uploadTask = fileReference.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                progressBar.setVisibility(View.GONE); // Hide the progress bar
                Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();

                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageLink = uri.toString();
                    Log.d("TAG", "Download URL: " + imageLink);
                    insertCard(); // Call insertCard here after getting the image link
                });
            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE); // Hide the progress bar
                Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });

            // Update the progress bar during upload
            uploadTask.addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
            });
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Restrict to images only
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                // Show image in ImageView
                showImage(uri);
            }
        }
    }

    private void showImage(Uri uri) {
        try {
            // Load the image into the ImageView
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
            imageUri = uri; // Set the imageUri here
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchLanguageDataAndSetSpinner() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("languageData");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> languageList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String language = snapshot.child("language").getValue(String.class);
                    if (language != null) {
                        languageList.add(language);
                    }
                }

                // Now set the adapter after the data is fetched
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        languageList
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                languageSpinner.setAdapter(adapter);
                languageSpinner1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }
}