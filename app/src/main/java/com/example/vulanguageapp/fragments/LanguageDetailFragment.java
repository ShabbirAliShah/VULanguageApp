package com.example.vulanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.ViewLectureActivity;
import com.example.vulanguageapp.databinding.FragmentLanguageDetailBinding;

public class LanguageDetailFragment extends Fragment {

    private FragmentLanguageDetailBinding binding;

    public LanguageDetailFragment() {
    }

    private TextView courseTitle;
    private TextView courseDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLanguageDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        courseTitle = view.findViewById(R.id.courseTitle);
        courseDescription = view.findViewById(R.id.courseDescription);

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
    public void onResume(){
        super.onResume();

        if (getArguments() !=null ){
            String languageName = getArguments().getString("language_name");
            String description = getArguments().getString("description");

            courseTitle.setText(languageName);
            courseDescription.setText(description);
        }else {
            courseTitle.setText("No data available");
            courseDescription.setText("No data found, your data model is null.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
