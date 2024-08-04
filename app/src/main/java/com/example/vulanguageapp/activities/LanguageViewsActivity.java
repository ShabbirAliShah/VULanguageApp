package com.example.vulanguageapp.activities;

import android.os.Bundle;

import com.example.vulanguageapp.databinding.ActivityLanguageViewsBinding;

public class LanguageViewsActivity extends BaseActivity {

    //Fragments for LanguageActivity
    //language detail, language view.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLanguageViewsBinding binding = ActivityLanguageViewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigationDrawer();
        
    }

}