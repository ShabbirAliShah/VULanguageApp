package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.databinding.FragmentLectureViewBinding;
import com.example.vulanguageapp.models.LessonsModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class LectureViewFragment extends Fragment {

    private FragmentLectureViewBinding binding;

    public LectureViewFragment() {

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){

        binding = FragmentLectureViewBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){

            String videoUrl = getArguments().getString("link");
            String lessonTitle = getArguments().getString("lesson_title");
            String lessonId = getArguments().getString("lesson_id");

            binding.lessonViewTitle.setText(lessonTitle);

            Log.d("LectureViewFragment", "lesson title" + lessonTitle);
            Log.d("LectureViewFragment", "lesson Id " + lessonId);

            WebView youtubeWebView = view.findViewById(R.id.youtubeWebView);

            // Enable JavaScript for the WebView
            WebSettings webSettings = youtubeWebView.getSettings();

            webSettings.setJavaScriptEnabled(true);

            // Load the YouTube video URL
            //String videoUrl = "https://www.youtube.com/embed/3BpRfGativ4";
            youtubeWebView.loadUrl(videoUrl);

            // Ensure links open in the WebView instead of an external browser
            youtubeWebView.setWebViewClient(new WebViewClient());

            binding.viewFlashCard.setOnClickListener(v->{
                Bundle dataBundle = new Bundle();
                dataBundle.putString("lesson_id", lessonId);
                NavHostFragment.findNavController(this).navigate(R.id.action_lectureViewFragment_to_flashCardsFragment, dataBundle);
            });
        }

    }

}