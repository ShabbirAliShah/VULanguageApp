package com.example.vulanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.ViewLectureActivity;
import com.example.vulanguageapp.models.Language_Data_Model;

public class LanguageDetailFragment extends Fragment {

    private Language_Data_Model data;

    public LanguageDetailFragment() {
    }

    public LanguageDetailFragment(Language_Data_Model data) {
        this.data = data;
    }

    private TextView courseTitle;
    private TextView courseDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        data = new Language_Data_Model("Basic Urdu Course", "Urdu is the " +
                "national language of Pakistan, " +
                "lear Urdu easily chating with other learner.");

        // Handle case where data is null (optional)
        if (data == null) {
            Toast.makeText( getContext(), "Data model is null", Toast.LENGTH_SHORT).show();
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language_detail, container, false);

        // Find TextViews for display
        courseTitle = view.findViewById(R.id.courseTitle); // Replace with your ID
        courseDescription = view.findViewById(R.id.courseDescription); // Replace with your ID

        // Bind data directly to views
        if (data != null) {
            courseTitle.setText(data.getLanguageName());
            courseDescription.setText(data.getLanguageDescription());
        } else {
            // Handle case where data is null (optional)
            courseTitle.setText("No data available");
            courseDescription.setText("No data found, your data model is null.");
        }

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonSecond = view.findViewById(R.id.button_second);

        buttonSecond.setOnClickListener(v ->
                NavHostFragment.findNavController(LanguageDetailFragment.this)
                        .navigate(R.id.action_languageDetailFragment_to_languageViewFragment)
        );

        Button buttonEnroll = view.findViewById(R.id.button_enroll);

        buttonEnroll.setOnClickListener(v ->
                NavHostFragment.findNavController(LanguageDetailFragment.this)
                        .navigate(R.id.action_languageDetailFragment_to_topicToEnrollFragment)
        );

        Button btnLectureActivity = view.findViewById(R.id.btnLectureActivity);

        btnLectureActivity.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ViewLectureActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        data = null;
    }}
