package com.example.vulanguageapp.activities;

import android.os.Bundle;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.databinding.ActivityLanguageViewsBinding;

public class LanguageViewsActivity extends BaseActivity {

    //Fragments for LanguageActivity
    //language detail, language view.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_language_view);

    }

}