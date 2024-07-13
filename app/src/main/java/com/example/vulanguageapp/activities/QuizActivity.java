package com.example.vulanguageapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.QuizModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends BaseActivity {

    private List<QuizModel> questions; // List of Question objects
    private int currentQuestionIndex = 0;
    private Button mvToLanguageActivity, mvToLesson, mvToChat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize questions (replace with your actual data or data loading logic)
        questions = new ArrayList<>();
        questions.add(new QuizModel("What is the capital of France?", Arrays.asList("Paris", "London", "Berlin"), 0));
        questions.add(new QuizModel("What is the largest planet in our solar system?", Arrays.asList("Jupiter", "Mars", "Venus"), 0));

        // References to UI elements
        TextView questionText = findViewById(R.id.question_text);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        Button submitButton = findViewById(R.id.submit_button);
        Button nextButton = findViewById(R.id.next_button); // Add a next button

        // Update UI for the current question
        updateQuestion(currentQuestionIndex);

        // Submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                int answerIndex = -1; // Initialize to -1 (no answer selected)

                // Find the selected answer index
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.getId() == selectedId) {
                        answerIndex = i;
                        break;
                    }
                }

                // Check if an answer is selected and evaluate
                if (answerIndex != -1) {
                    if (answerIndex == questions.get(currentQuestionIndex).getAnswerIndex()) {
                        // Correct answer
                        Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Wrong answer
                        Toast.makeText(getApplicationContext(), "Incorrect.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // No answer selected
                    Toast.makeText(getApplicationContext(), "Please select an answer.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Next button click listener (optional)
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    updateQuestion(currentQuestionIndex);
                } else {
                    Toast.makeText(getApplicationContext(), "You've reached the last question.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        mvToLanguageActivity = findViewById(R.id.btnLanguageView);
//
//        mvToLanguageActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mvToLanguageActivity = new Intent(QuizActivity.this, LanguageViewsActivity.class);
//                startActivity(mvToLanguageActivity);
//            }
//        });
//
//        mvToLesson = findViewById(R.id.btnLesson);
//
//        mvToLesson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mvToLesson = new Intent(QuizActivity.this, ViewLectureActivity.class);
//                startActivity(mvToLesson);
//            }
//        });
//
//        mvToChat = findViewById(R.id.btnChat);
//
//        mvToChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mvToChat = new Intent(QuizActivity.this, ChatActivity.class);
//                startActivity(mvToChat);
//            }
//        });
    }

    private void updateQuestion(int index) {
        QuizModel question = questions.get(index);
        TextView questionText = findViewById(R.id.question_text);
        RadioGroup radioGroup = findViewById(R.id.radio_group);

        questionText.setText(question.getQuestion());
        radioGroup.clearCheck(); // Clear previous selection

        // Populate RadioGroup with options
        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioGroup.addView(radioButton);
        }
    }

}