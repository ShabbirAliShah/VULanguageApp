package com.example.vulanguageapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.interfaces.LessonActivityFragmentClickListener;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class StudyTopicListAdapter extends RecyclerView.Adapter<StudyTopicListAdapter.ViewHolder> {

    private final ArrayList<Language_Data_Model> studyTopicListData;
    private final LessonActivityFragmentClickListener lessonClickListener;

    public StudyTopicListAdapter(ArrayList<Language_Data_Model> studyTopicListData,LessonActivityFragmentClickListener lessonClickListener) {
        this.studyTopicListData = studyTopicListData;
        this.lessonClickListener = lessonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_topic_to_study, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Language_Data_Model langDataPosition = studyTopicListData.get(position);
        holder.studyTopicName.setText(langDataPosition.getLanguageName());
        holder.cardViewClk.setOnClickListener(lessonClickListener::onCourseContentClick);
    }

    @Override
    public int getItemCount() {
        return studyTopicListData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView studyTopicName;
        CardView cardViewClk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studyTopicName = itemView.findViewById(R.id.studyTopic);
            cardViewClk = itemView.findViewById(R.id.courseContentCard);
        }
    }
}