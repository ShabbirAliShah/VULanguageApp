package com.example.vulanguageapp.adminControlls.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.BaseActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PracticeActivity extends BaseActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private Uri videoUri;  // To hold the selected video URI
    private Button pickVideo, btnUploadVideo;
    private String user_Id;
    private VideoView videoPreview;
    private ProgressBar progressBar;
    private TextView uploadStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        user_Id = getUserId();

        // Check and request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_VIDEO}, 101);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
        }
        // Initialize buttons
        pickVideo = findViewById(R.id.btn_select_video);
        btnUploadVideo = findViewById(R.id.btn_upload_video);
        videoPreview = findViewById(R.id.video_view);
        progressBar = findViewById(R.id.progress_bar);
        uploadStatusTextView = findViewById(R.id.tv_upload_status);

        // Set click listener for the "Select Video" button
        pickVideo.setOnClickListener(v -> {
            openVideoPicker();  // Only opens the video picker when the button is clicked
        });

        // Set click listener for the "Upload Video" button
        btnUploadVideo.setOnClickListener(v -> {

            if (user_Id != null){
                uploadVideo();
            }else {
                Toast.makeText(this, "user id is null", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    String videoUrl = uri.toString();
                    // Use this URL to store it in your database
                });

            }).addOnFailureListener(e -> {
                // Hide the ProgressBar and show failure message
                progressBar.setVisibility(View.GONE);
                uploadStatusTextView.setText("Upload failed: " + e.getMessage());
                Toast.makeText(PracticeActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show();
        }
    }
}
