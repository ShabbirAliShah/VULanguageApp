package com.example.vulanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.QuizModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private List<QuizModel> questions; // List of Question objects
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setupNavigationDrawer();

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

    public boolean setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer); // Find DrawerLayout by ID
        navigationView = findViewById(R.id.navView); // Find NavigationView by ID

        // Setup action bar toggle (optional, for opening drawer from action bar)
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.navHome) {

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.languages){
                Intent intent = new Intent(this, LanguageViewsActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.quiz){
                Intent intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.progress){
                Intent intent = new Intent(this, GamificationActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.lecture){
                Intent intent = new Intent(this, ViewLectureActivity.class);
                startActivity(intent);
            }

            drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after handling selection
            return true;
        });
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle actions from navigation drawer (if up button is pressed)
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}