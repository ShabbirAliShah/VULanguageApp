package com.example.vulanguageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class StudyTopicListAdapter extends RecyclerView.Adapter<StudyTopicListAdapter.ViewHolder> {

    private ArrayList<Language_Data_Model> studyTopicListData;
    private Context context;

    public StudyTopicListAdapter(ArrayList<Language_Data_Model> studyTopicListData, Context context) {
        this.studyTopicListData = studyTopicListData;
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return studyTopicListData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView studyTopicName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studyTopicName = itemView.findViewById(R.id.studyTopic);
        }
    }
}
