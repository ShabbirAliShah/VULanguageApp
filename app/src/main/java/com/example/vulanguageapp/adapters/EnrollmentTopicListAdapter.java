package com.example.vulanguageapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.LessonsModel;

import java.util.List;

public class EnrollmentTopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LessonsModel> dataList;
    private static final int VIEW_TYPE_ENROLL = 0;
    private static final int VIEW_TYPE_DISPLAY = 1;
    private final Context context;
    private final boolean isEnrollLayout;

    public EnrollmentTopicListAdapter(List<LessonsModel> dataList, Context context, boolean isEnrollLayout) {
        this.dataList = dataList;
        this.context = context;
        this.isEnrollLayout = isEnrollLayout;
    }

    @Override
    public int getItemViewType(int position) {

        return isEnrollLayout ? VIEW_TYPE_ENROLL : VIEW_TYPE_DISPLAY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Adapter", "onCreateViewHolder called");

        if (viewType == VIEW_TYPE_ENROLL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_topic_to_enroll, parent, false);
            return new EnrollViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_topics_to_display, parent, false);
            return new DisplayViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LessonsModel currentLesson = dataList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_ENROLL) {
            EnrollViewHolder enrollHolder = (EnrollViewHolder) holder;
            // Bind data for card_view_topic_to_enroll.xml
            enrollHolder.topicName.setText(currentLesson.getTitle());
            enrollHolder.checkBox.setChecked(currentLesson.isSelected());

            // Remove previous listener to avoid multiple callbacks
            enrollHolder.checkBox.setOnCheckedChangeListener(null);

            enrollHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                currentLesson.setSelected(isChecked);
            });

        } else {
            DisplayViewHolder displayHolder = (DisplayViewHolder) holder;
            // Bind data for card_view_topics_to_display.xml
            displayHolder.topicName.setText(currentLesson.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        Log.d("Adapter", "getItemCount: " + dataList.size());
        return dataList.size();
    }

    public static class EnrollViewHolder extends RecyclerView.ViewHolder {
        TextView topicName;
        CheckBox checkBox;
        public EnrollViewHolder(View itemView) {
            super(itemView);
            topicName = itemView.findViewById(R.id.lessonTitle);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public static class DisplayViewHolder extends RecyclerView.ViewHolder {
        TextView topicName;
        public DisplayViewHolder(View itemView) {
            super(itemView);
            topicName = itemView.findViewById(R.id.lessonList);
        }
    }
}
