package com.example.vulanguageapp.activities;

import android.os.Bundle;

import com.example.vulanguageapp.databinding.ActivityChatBinding;

public class ChatActivity extends BaseActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigationDrawer();

    }
}