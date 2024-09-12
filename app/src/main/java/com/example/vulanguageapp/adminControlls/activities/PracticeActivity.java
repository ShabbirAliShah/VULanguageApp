package com.example.vulanguageapp.adminControlls.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adminControlls.adminAdapters.LessonsAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

//    private EditText lessonInput;
//    private ChipGroup chipGroup;
//    private List<String> selectedLessons = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_practice);
//
//
//        lessonInput = findViewById(R.id.lessonInput);
//        chipGroup = findViewById(R.id.chipGroup);
//
//        lessonInput.setOnClickListener(v -> showBottomSheetDialog());
//    }
//
//    private void showBottomSheetDialog() {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout, null);
//        bottomSheetDialog.setContentView(sheetView);
//
//        RecyclerView lessonsRecyclerView = sheetView.findViewById(R.id.bottomSheetRecyclerView);
//        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        List<String> lessons = getLessons();
//        LessonsAdapter adapter = new LessonsAdapter(lessons, lesson -> {
//            if (selectedLessons.contains(lesson)) {
//                selectedLessons.remove(lesson);
//                removeChip(lesson);
//            } else {
//                selectedLessons.add(lesson);
//                addChip(lesson);
//            }
//            updateInputField();
//        });
//        lessonsRecyclerView.setAdapter(adapter);
//
//        bottomSheetDialog.show();
//    }
//
//    private List<String> getLessons() {
//        // Dummy data for demo purposes
//        List<String> lessons = new ArrayList<>();
//        lessons.add("Lesson 1");
//        lessons.add("Lesson 2");
//        lessons.add("Lesson 3");
//        lessons.add("Lesson 4");
//        lessons.add("Lesson 5");
//        return lessons;
//    }
//
//    private void addChip(String lesson) {
//        Chip chip = new Chip(this);
//        chip.setText(lesson);
//        chip.setCloseIconVisible(true);
//        chip.setOnCloseIconClickListener(v -> {
//            selectedLessons.remove(lesson);
//            chipGroup.removeView(chip);
//            updateInputField();
//        });
//        chipGroup.addView(chip);
//    }
//
//    private void removeChip(String lesson) {
//        for (int i = 0; i < chipGroup.getChildCount(); i++) {
//            Chip chip = (Chip) chipGroup.getChildAt(i);
//            if (chip.getText().toString().equals(lesson)) {
//                chipGroup.removeView(chip);
//                break;
//            }
//        }
//    }
//
//    private void updateInputField() {
//        lessonInput.setText(String.join(", ", selectedLessons));
//    }
}