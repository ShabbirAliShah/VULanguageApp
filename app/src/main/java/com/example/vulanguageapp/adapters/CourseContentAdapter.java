package com.example.vulanguageapp.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.LessonsModel;

import java.util.List;

public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.ViewHolder> {
    public List<LessonsModel> dataList;
    public final NavController navController;

    public CourseContentAdapter(List<LessonsModel> dataList, NavController navController) {
        this.navController = navController;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CourseContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_for_course_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseContentAdapter.ViewHolder holder, int position) {

        LessonsModel lessonsModel = dataList.get(position);
        holder.lessonTitle.setText(lessonsModel.getTitle());

        if (lessonsModel.getIsCompleted() != null && lessonsModel.getIsCompleted()) {
            holder.statustext.setText("Complete");
        } else {
            holder.statustext.setText("upcomming");
        }

        holder.card_for_course_content.setOnClickListener(view -> {

            Bundle dataBundle = new Bundle();
            dataBundle.putString("link", lessonsModel.getVideoLink());
            dataBundle.putString("lesson_title", lessonsModel.getTitle());
            dataBundle.putString("lesson_id", lessonsModel.getLessonId());

            Log.d("CourseContentAdapter", "Lesson_id " + lessonsModel.getLessonId());

            navController.navigate(R.id.action_topicsToStudyFragment_to_lectureViewFragment, dataBundle);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView lessonTitle, statustext;
        CardView card_for_course_content;

        public ViewHolder(View itemView){
            super(itemView);

            lessonTitle = itemView.findViewById(R.id.lessonsToStudy);
            card_for_course_content = itemView.findViewById(R.id.card_for_course_content);
            statustext = itemView.findViewById(R.id.statustext);
        }
    }
}
