package com.example.vulanguageapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.QuizModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent(R.layout.content_chat);

    }

}