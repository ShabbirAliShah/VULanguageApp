package com.example.vulanguageapp.adminControlls.adminAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.LessonsModel;

import java.util.ArrayList;
import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonViewHolder> {

    private List<LessonsModel> lessonsList;
    private List<LessonsModel> selectedLessons = new ArrayList<>();
    private OnSelectionChangeListener selectionChangeListener;

    public LessonsAdapter(List<LessonsModel> lessonsList, OnSelectionChangeListener selectionChangeListener) {
        this.lessonsList = lessonsList;
        this.selectionChangeListener = selectionChangeListener;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        LessonsModel lesson = lessonsList.get(position);
        holder.titleText.setText(lesson.getTitle());
        holder.lessonCheckBox.setText(lesson.getLessonId());
        holder.lessonCheckBox.setChecked(selectedLessons.contains(lesson));

        holder.lessonCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedLessons.add(lesson);
            } else {
                selectedLessons.remove(lesson);
            }

            // Notify the fragment or activity about the change in selection
            selectionChangeListener.onSelectionChanged(selectedLessons);
        });
    }

    @Override
    public int getItemCount() {
        return lessonsList.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        CheckBox lessonCheckBox;
        TextView titleText;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonCheckBox = itemView.findViewById(R.id.lessonCheckbox);
            titleText = itemView.findViewById(R.id.lessonTitle);
        }
    }

    public interface OnSelectionChangeListener {
        void onSelectionChanged(List<LessonsModel> selectedLessons);
    }
}
