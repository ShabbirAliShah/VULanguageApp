 package com.example.vulanguageapp.activities;

 import android.os.Bundle;

 import com.example.vulanguageapp.databinding.ActivityMainBinding;

 public class MainActivity extends BaseActivity {

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigationDrawer();
    }
 }