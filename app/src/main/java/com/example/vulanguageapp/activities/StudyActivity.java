package com.example.vulanguageapp.activities;

import android.os.Bundle;

import com.example.vulanguageapp.R;

//Fragments contained in this activity;
// EnrolledCourses, TopicsToStudy, LectureViewFragment, FlashCardsFragment;

public class StudyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.content_lesson_view);

    }
}