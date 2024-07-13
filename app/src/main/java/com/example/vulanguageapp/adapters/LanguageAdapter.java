package com.example.vulanguageapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.interfaces.LanguageActivityFragmentClickListener;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder>{
    private ArrayList<Language_Data_Model> dataList;
    private LanguageActivityFragmentClickListener listener;

    public LanguageAdapter(ArrayList<Language_Data_Model> dataList, LanguageActivityFragmentClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
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

        holder.languageCardClick.setOnClickListener(v -> listener.onLanguageCardClick(v));

    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView languageName,languageDescription;
        LinearLayout languageCardClick;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        languageName = itemView.findViewById(R.id.langName);
        languageDescription = itemView.findViewById(R.id.langDescription);
        languageCardClick = itemView.findViewById(R.id.language_Card);
    }
    }
}