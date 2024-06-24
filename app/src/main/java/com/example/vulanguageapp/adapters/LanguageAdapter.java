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

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder>{ private ArrayList<Language_Data_Model> dataList;
    private Context context;
    public LanguageAdapter(ArrayList<Language_Data_Model> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflating card view widget.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_for_languages_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Language_Data_Model langModel = dataList.get(position);
        holder.languageName.setText(langModel.getLanguageName());
        holder.languageDescription.setText(langModel.getLanguageDescription());
    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView languageName,languageDescription;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        languageName = itemView.findViewById(R.id.langName);
        languageDescription = itemView.findViewById(R.id.langDescription);
    }
    }
}