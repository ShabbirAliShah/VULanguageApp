package com.example.vulanguageapp.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.Enrollment;
import com.example.vulanguageapp.models.LessonsModel;

import java.io.Serializable;
import java.util.List;

public class LessonActivityAdapter extends RecyclerView.Adapter<LessonActivityAdapter.ViewHolder> {

    private final List<Enrollment> dataList;

    private final NavController navController;

    public LessonActivityAdapter(List<Enrollment> dataList, NavController navController) {
        this.dataList = dataList;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Adapter", "onCreateViewHolder called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_enrolled_courses, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Enrollment currentCourse = dataList.get(position);
        holder.courseTitle.setText(currentCourse.getCourseTitle());

        holder.cardView.setOnClickListener(v -> {

            Bundle dataBundle = new Bundle();
            dataBundle.putString("course_id", currentCourse.getCourseId());

            navController.navigate(R.id.action_enrolledCoursesFragment_to_topicsToStudyFragment, dataBundle);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseTitle;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            courseTitle = itemView.findViewById(R.id.course_title);
            cardView = itemView.findViewById(R.id.enrolledCourseCard);
        }
    }
}