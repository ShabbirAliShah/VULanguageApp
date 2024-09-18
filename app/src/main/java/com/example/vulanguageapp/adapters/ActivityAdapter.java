package com.example.vulanguageapp.adapters;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.BaseActivity;
import com.example.vulanguageapp.models.CourseModel;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private ArrayList<CourseModel> dataList;
    private NavController navController;

    public ActivityAdapter(ArrayList<CourseModel> dataList, NavController navController) {

        this.dataList = dataList;
        this.navController = navController;
     }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_for_home_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseModel coursesDataModel = dataList.get(position);
        holder.courseTitle.setText(coursesDataModel.getTitle());
        holder.courseLevel.setText(coursesDataModel.getLevel());

        Log.d("Activity Adapter", "Course Id " + coursesDataModel.getCourseKey());

        holder.homeCard.setOnClickListener(v -> {

            Bundle dataBundle = new Bundle();

            dataBundle.putString("language_name", coursesDataModel.getLanguage());
            dataBundle.putString("course_title", coursesDataModel.getTitle());
            dataBundle.putString("course_level", coursesDataModel.getLevel());
            dataBundle.putString("course_id", coursesDataModel.getCourseKey());
            dataBundle.putString("courseDescription", coursesDataModel.getCourseDescription());
            // Use NavController to navigate to the next fragment
            navController.navigate(R.id.action_languageHomeFragment_to_languageDetailFragment, dataBundle);
        });
    }

    @Override
    public int getItemCount() {
        return  dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseLevel, courseTitle;
        RelativeLayout homeCard;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            courseTitle = itemView.findViewById(R.id.courseTitleView);
            courseLevel = itemView.findViewById(R.id.courseLevelView);
            homeCard = itemView.findViewById(R.id.cont);
        }
    }
}
