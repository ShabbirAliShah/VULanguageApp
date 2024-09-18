package com.example.vulanguageapp.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.EnrollmentModel;

import java.util.ArrayList;

public class EnrolledAdapter extends RecyclerView.Adapter<EnrolledAdapter.ViewHolder> {

    protected ArrayList<EnrollmentModel> enrolledDataList;
    NavController navController;

    public EnrolledAdapter(NavController navController, ArrayList<EnrollmentModel> enrolledDataList) {
        this.enrolledDataList = enrolledDataList;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_enrolled_courses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EnrollmentModel enrolledData = enrolledDataList.get(position);
        holder.courseTitle.setText(enrolledData.getCourseTitle());

        holder.cardClick.setOnClickListener(v -> {
            Bundle enrollmentBundle = new Bundle();
            enrollmentBundle.putSerializable("dataArray", enrolledDataList);

            navController.navigate(R.id.action_enrolledCourseFragment_to_topicsToStudyFragment, enrollmentBundle);
        });
    }

    @Override
    public int getItemCount() {
        return enrolledDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseTitle;
        CardView cardClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseTitle = itemView.findViewById(R.id.course_title);
            cardClick = itemView.findViewById(R.id.enrolledCourseCard);
        }
    }
}
