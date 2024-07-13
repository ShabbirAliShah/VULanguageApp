package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.databinding.FragmentLectureViewBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class LectureViewFragment extends Fragment {

    private FragmentLectureViewBinding binding;
    private WebView webView;
    private YouTubePlayerView youTubePlayerView;

    public LectureViewFragment() {

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){

        binding = FragmentLectureViewBinding.inflate(inflater, container, false);

        //getLifecycle().addObserver(youTubePlayerView);

//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "Umjvt_Vkn1Q";
//                youTubePlayer.loadVideo(videoId, 0);
//            }
       // });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        webView = view.findViewById(R.id.webview_youtube);

        webView.findViewById(R.id.webview_youtube);
        webView.getSettings().setJavaScriptEnabled(true);


        String videoUrl = "https://www.youtube.com/embed/KO-wBv7Phwo";

        String htmlData = "<iframe type=\"text/html\" width=\"100%\" height=\"100%\" " +
                "src=\"" + videoUrl + "\" frameborder=\"0\"></iframe>";
        String mimeType = "text/html";
        String encoding = "UTF-8";
        String historyUrl = ""; // Optional

        webView.loadDataWithBaseURL("", htmlData, mimeType, encoding, historyUrl);

    }
}