package com.example.vulanguageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private ArrayList<Language_Data_Model> dataList;
    private Context context;
    public ActivityAdapter(ArrayList<Language_Data_Model> dataList, Context context) {

        this.dataList = dataList;
        this.context = context;
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
        Language_Data_Model langModel = dataList.get(position);
        holder.countryText.setText(langModel.getName());
        holder.languageText.setText(langModel.getCountry());
        holder.languageDescription.setText(langModel.getLanguageDescription());
        //holder.crdVwClick.setOnClickListener(v -> listener.onLangCardClick(v));
    }

    @Override
    public int getItemCount() {
        return  dataList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView languageText, languageDescription, countryText;
        LinearLayout crdVwClick;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            countryText = itemView.findViewById(R.id.countryTextView);
            languageText = itemView.findViewById(R.id.langTextView);
            languageDescription = itemView.findViewById(R.id.description);
            //crdVwClick = itemView.findViewById(R.id.cardHm);
        }
    }
}
