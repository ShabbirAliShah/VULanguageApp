package com.example.vulanguageapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.databinding.FragmentLectureViewBinding;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.example.vulanguageapp.models.LessonsModel;
import com.example.vulanguageapp.services.VideoService;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.games.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.File;
import java.io.IOException;

public class LectureViewFragment extends Fragment {

    private FragmentLectureViewBinding binding;
    DatabaseReference enrollmentRef, lessonCompletionRef;
    private String enrollmentId;  // Enrollment ID will be retrieved dynamically
    private TextView statusTextView;
    private Bundle dataBundle;
    private String lessonId;
    private PlayerView playerView;
    private ExoPlayer player;
    private String videoUrl;
    private UserIdProvider userIdProvider;
    private String userId;

    public LectureViewFragment() {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLectureViewBinding.inflate(inflater, container, false);

        if (userIdProvider != null) {
            userId = userIdProvider.getUserId();
        }

        if (getArguments() != null) {

            videoUrl = getArguments().getString("link");
            String lessonTitle = getArguments().getString("lesson_title");
            lessonId = getArguments().getString("lesson_id");

            binding.lessonViewTitle.setText(lessonTitle);

            Log.d("LectureViewFragment", "lesson title: " + lessonTitle);
            Log.d("LectureViewFragment", "lesson Id: " + lessonId);

            dataBundle = new Bundle();

            binding.viewFlashCard.setOnClickListener(v -> {

                dataBundle.putString("lesson_id", lessonId);
                dataBundle.putString("language", "hindi");
                NavHostFragment.findNavController(this).navigate(R.id.action_lectureViewFragment_to_flashCardsFragment, dataBundle);
            });

            binding.pronunciation.setOnClickListener(v->{
                 dataBundle.putString("lesson_id", lessonId);
                NavHostFragment.findNavController(this).navigate(R.id.action_lectureViewFragment_to_pronunciationExecerciseFragment, dataBundle);
            });

            binding.quizBtn.setOnClickListener(v->{

                 dataBundle.putString("lesson_id", lessonId);
                NavHostFragment.findNavController(this).navigate(R.id.action_lectureViewFragment_to_quizFragment2, dataBundle);

            });
        }

        statusTextView = binding.markComplete;
        playerView = binding.playerview;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setInitialStatusText(lessonId);

        binding.markComplete.setOnClickListener(v -> {
            toggleLessonCompletionStatus();
        });
    }

    private void setInitialStatusText(String idForLesson) {
        enrollmentRef = FirebaseDatabase.getInstance().getReference("enrollments");

        // Assuming you have the current user's ID in a variable `userId`
        Query query = enrollmentRef.orderByChild("userId").equalTo(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Assuming you want to get the first matching enrollment (if there are multiple)
                    for (DataSnapshot enrollmentSnapshot : snapshot.getChildren()) {
                        enrollmentId = enrollmentSnapshot.getKey();  // Get the enrollment ID

                        // Build the reference to the selected lesson's completed status
                        lessonCompletionRef = FirebaseDatabase.getInstance().getReference("enrollments")
                                .child(enrollmentId)
                                .child("selectedLessons")
                                .child(idForLesson)
                                .child("completed");

                        // Now fetch the completed status
                        lessonCompletionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Object value = snapshot.getValue();
                                if (value instanceof Boolean) {
                                    Boolean completed = (Boolean) value;
                                    updateStatusText(completed); // Update the TextView here
                                } else {
                                    Log.e("UpdateLessonStatus", "Unexpected data type: " + value.getClass().getSimpleName());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("InitialStatusText", "Failed to fetch initial status.", error.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("lectureview", "error message" + error.getMessage());
            }
        });
    }
    private void toggleLessonCompletionStatus() {
        lessonCompletionRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue();

                if (value instanceof Boolean) {
                    // If the value is a Boolean, proceed with toggling it
                    Boolean completed = (Boolean) value;
                    lessonCompletionRef.setValue(!completed);  // Toggle the value
                    updateStatusText(!completed);  // Update the UI
                } else {
                    // If the value is not a Boolean (e.g., a HashMap), log the issue and set it to true
                    Log.e("UpdateLessonStatus", "Unexpected data type: " + value.getClass().getSimpleName());
                    lessonCompletionRef.setValue(true);  // Set as completed if it's not a Boolean
                    updateStatusText(true);  // Update the UI
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UpdateLessonStatus", "Failed to toggle completion status.", error.toException());
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

    private void initializePlayer() {
        // Create ExoPlayer instance
        player = new ExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);

        // Prepare the media item
        Uri uri = Uri.parse(videoUrl); // Replace with your video URL
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    public void handleBackPress() {
        if (player != null && player.isPlaying()) {
            player.pause();
            // Optionally, update UI or save state
        }
        requireActivity().onBackPressed(); // Call the activity's back press
    }

    private void startVideoService() {
        Intent serviceIntent = new Intent(getActivity(), VideoService.class);
        serviceIntent.putExtra("VIDEO_URL", videoUrl);
        ContextCompat.startForegroundService(getActivity(), serviceIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        } else if (player != null) {
            player.pause();
        }
    }
}