package com.example.vulanguageapp.fragments;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.vulanguageapp.databinding.FragmentQuizBinding;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.example.vulanguageapp.models.QuizQuestion;
import com.example.vulanguageapp.models.QuizResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private List<QuizQuestion> questionList;
    private int currentQuestionIndex = 0;
    private String userId;
    private String lessonId;
    private int score = 0;
    private DatabaseReference databaseReference;
    private UserIdProvider userIdProvider;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserIdProvider) {
            userIdProvider = (UserIdProvider) context;
            userId = userIdProvider.getUserId();
        } else {
            throw new RuntimeException(context + " must implement UserIdProvider");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            // Retrieve lessonId from arguments
            lessonId = getArguments().getString("lesson_id");
            Toast.makeText(getContext(), "Lesson Id check this id is " + lessonId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Use View Binding
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("quizQuestions");

        // Load questions from Firebase for the given lessonId
        loadQuizQuestions(lessonId);

        // Set next button click listener
        binding.nextButton.setOnClickListener(v -> {
            checkAnswer();  // Check answer of the current question
            currentQuestionIndex++;  // Move to the next question
            if (currentQuestionIndex < questionList.size()) {
                showQuestion(currentQuestionIndex);
            } else {
                storeResultInFirebase();
                // Hide the "Next" button when the quiz is finished
                binding.nextButton.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Quiz Completed! Your score: " + score, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showQuestion(int index) {
        if (questionList == null || questionList.isEmpty()) {
            return;  // No questions available
        }
        QuizQuestion question = questionList.get(index);
        binding.questionText.setText(question.getQuestion());
        binding.option1.setText(question.getOption1());
        binding.option2.setText(question.getOption2());
        binding.option3.setText(question.getOption3());
        binding.option4.setText(question.getOption4());
        binding.optionsGroup.clearCheck();  // Clear selected options
    }

    private void checkAnswer() {
        int selectedOptionId = binding.optionsGroup.getCheckedRadioButtonId();
        RadioButton selectedOption = getView().findViewById(selectedOptionId);
        if (selectedOption != null) {
            String selectedAnswer = selectedOption.getText().toString();
            String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
            if (selectedAnswer.equals(correctAnswer)) {
                score++;
            }
        }
    }

    private void storeResultInFirebase() {

        // Initialize Firebase database
        DatabaseReference resultRef = FirebaseDatabase.getInstance().getReference("quizResults");

        // Create a unique key for this result
        String resultId = resultRef.push().getKey();

        // Create a result object
        QuizResult result = new QuizResult(userId, lessonId, score);

        // Store result in Firebase
        resultRef.child(resultId).setValue(result);
    }

    private void loadQuizQuestions(String lessonId) {
        questionList = new ArrayList<>();  // Initialize question list

        // Query questions by lessonId
        databaseReference.orderByChild("lessonId").equalTo(lessonId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();  // Clear list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get QuizQuestion object from snapshot
                    QuizQuestion question = snapshot.getValue(QuizQuestion.class);
                    questionList.add(question);
                }

                // Show the first question after loading
                if (!questionList.isEmpty()) {
                    showQuestion(currentQuestionIndex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(getContext(), "Failed to load quiz questions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}