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

public class EnrollmentTopicListAdapter extends RecyclerView.Adapter<EnrollmentTopicListAdapter.ViewHolder>{

    private ArrayList<Language_Data_Model> dataList;
    private Context context;

    public EnrollmentTopicListAdapter(ArrayList<Language_Data_Model> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflating card view widget.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_for_topic_to_enroll, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Language_Data_Model langModel = dataList.get(position);
        holder.topicName.setText(langModel.getName());

    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView topicName;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        topicName = itemView.findViewById(R.id.topic_name);
    }
    }
}