package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.databinding.FragmentLectureViewBinding;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class LectureViewFragment extends Fragment {

    private FragmentLectureViewBinding binding;
    private DatabaseReference enrollmentRef;
    private String enrollmentId;  // Enrollment ID will be retrieved dynamically
    private TextView statusTextView;
    private Bundle dataBundle;
    private String lessonId;

    public LectureViewFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLectureViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statusTextView = view.findViewById(R.id.markComplete);

        if (getArguments() != null) {

            String videoUrl = getArguments().getString("link");
            String lessonTitle = getArguments().getString("lesson_title");
            lessonId = getArguments().getString("lesson_id");

            binding.lessonViewTitle.setText(lessonTitle);

            Log.d("LectureViewFragment", "lesson title: " + lessonTitle);
            Log.d("LectureViewFragment", "lesson Id: " + lessonId);

            WebView youtubeWebView = view.findViewById(R.id.youtubeWebView);
            WebSettings webSettings = youtubeWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            youtubeWebView.loadUrl(videoUrl);
            youtubeWebView.setWebViewClient(new WebViewClient());

            binding.viewFlashCard.setOnClickListener(v -> {
                dataBundle = new Bundle();
                dataBundle.putString("lesson_id", lessonId);
                NavHostFragment.findNavController(this).navigate(R.id.action_lectureViewFragment_to_flashCardsFragment, dataBundle);
            });

            fetchEnrollmentIdAndSetup(lessonId);

            binding.markComplete.setOnClickListener(v -> {
                toggleLessonCompletionStatus();
            });

            binding.pronunciation.setOnClickListener(v->{
                dataBundle = new Bundle();
                dataBundle.putString("lesson_id", lessonId);
                NavHostFragment.findNavController(this).navigate(R.id.action_lectureViewFragment_to_pronunciationExecerciseFragment, dataBundle);
            });
        }
    }

    private void fetchEnrollmentIdAndSetup(String lessonId) {
        DatabaseReference enrollmentsRef = FirebaseDatabase.getInstance().getReference("enrollments");

        enrollmentsRef.orderByChild("selectedLessons/" + lessonId + "/isCompleted").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot enrollmentSnapshot : snapshot.getChildren()) {
                    enrollmentId = enrollmentSnapshot.getKey();

                    if (enrollmentId != null) {
                        enrollmentRef = enrollmentsRef.child(enrollmentId).child("selectedLessons").child(lessonId).child("isCompleted");

                        setInitialStatusText();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LectureViewFragment", "Failed to find enrollment ID.", error.toException());
            }
        });
    }

    private void toggleLessonCompletionStatus() {
        enrollmentRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isCompleted = snapshot.getValue(Boolean.class);
                if (isCompleted != null) {
                    enrollmentRef.setValue(!isCompleted);
                    updateStatusText(!isCompleted);
                } else {
                    enrollmentRef.setValue(true);
                    updateStatusText(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UpdateLessonStatus", "Failed to toggle completion status.", error.toException());
            }
        });
    }

    private void setInitialStatusText() {
        enrollmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isCompleted = snapshot.getValue(Boolean.class);
                updateStatusText(isCompleted != null && isCompleted);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("InitialStatusText", "Failed to fetch initial status.", error.toException());
            }
        });
    }

    private void updateStatusText(boolean isCompleted) {
        if (isCompleted) {
            statusTextView.setText("Unmark as Complete");
        } else {
            statusTextView.setText("Mark as Complete");
        }
    }
}