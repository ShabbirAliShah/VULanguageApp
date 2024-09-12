package com.example.vulanguageapp.activities;

import android.os.Bundle;

import com.example.vulanguageapp.R;


//Fragments contained in this activity;
//HomeFragment, LanguageDetailFragment, TopicsToEnrollFragment

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent(R.layout.content_main_activity);
    }
}