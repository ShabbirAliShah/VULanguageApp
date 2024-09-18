package com.example.vulanguageapp.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.EnrollmentModel;
import com.example.vulanguageapp.models.LessonsModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.ViewHolder> {

    private final ArrayList<LessonsModel> lessonList;
    private final NavController navController;
    private final ArrayList<EnrollmentModel> enrollmentDataList;

    public CourseContentAdapter(ArrayList<LessonsModel> lessonList, NavController navController, ArrayList<EnrollmentModel> enrollmentDataList) {
        this.lessonList = lessonList;
        this.navController = navController;
        this.enrollmentDataList = enrollmentDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_course_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LessonsModel lesson = lessonList.get(position);
        holder.lessonTitle.setText(lesson.getTitle());

        boolean isLessonCompleted = isLessonCompleted(lesson.getLessonId());
        holder.checkBoxStatus.setChecked(isLessonCompleted);

        holder.lessonClick.setOnClickListener(v -> {

            Bundle dataBundle = new Bundle();
            dataBundle.putString("link", lesson.getVideoLink());
            dataBundle.putString("lesson_title", lesson.getTitle());
            dataBundle.putString("lesson_id", lesson.getLessonId());
            dataBundle.putString("courseId", lesson.getCourseId());

            ArrayList<String> flashCardIds = lesson.getFlashCards();
            if (flashCardIds != null && !flashCardIds.isEmpty()) {
                dataBundle.putStringArrayList("flashcard_id_list", flashCardIds);
            } else {
                Log.e("CourseContentAdapter", "Flashcard IDs are null or empty for lesson: " + lesson.getLessonId());
            }

            Log.d("CourseContentAdapter", "Lesson_id " + lesson.getLessonId());

            navController.navigate(R.id.action_topicsToStudyFragment_to_lectureViewFragment, dataBundle);
        });
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    private boolean isLessonCompleted(String lessonId) {
        for (EnrollmentModel enrollment : enrollmentDataList) {
            Map<String, EnrollmentModel.Lesson> lessonsMap = enrollment.getSelectedLessons();
            if (lessonsMap.containsKey(lessonId)) {
                return lessonsMap.get(lessonId).isCompleted();
            }
        }
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lessonTitle;
        CardView lessonClick;
        CheckBox checkBoxStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonTitle = itemView.findViewById(R.id.lesson_title);
            lessonClick = itemView.findViewById(R.id.card_for_course_content);
            checkBoxStatus = itemView.findViewById(R.id.statustext);
        }
    }
}
