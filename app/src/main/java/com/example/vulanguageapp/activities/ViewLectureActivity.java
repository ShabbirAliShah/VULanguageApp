package com.example.vulanguageapp.activities;

import android.os.Bundle;

import com.example.vulanguageapp.databinding.ActivityViewLectureBinding;

public class ViewLectureActivity extends BaseActivity {


    private ActivityViewLectureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewLectureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigationDrawer();

    }
}