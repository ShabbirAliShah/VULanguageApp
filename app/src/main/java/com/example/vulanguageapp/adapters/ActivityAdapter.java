package com.example.vulanguageapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.LanguageViewsActivity;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private final ArrayList<Language_Data_Model> dataList;

    private final Context context;

    public ActivityAdapter(ArrayList<Language_Data_Model> dataList, Context context){
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_for_home_list,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Language_Data_Model langModel = dataList.get(position);
        holder.keyText.setText(langModel.getKey());
        holder.countryText.setText(langModel.getCountryName());
        holder.languageText.setText(langModel.getLanguageName());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LanguageViewsActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return  dataList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView keyText, languageText, countryText;
        CardView cardView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            keyText = itemView.findViewById(R.id.keyTextView);
            countryText = itemView.findViewById(R.id.countryTextView);
            languageText = itemView.findViewById(R.id.langTextView);
            cardView = itemView.findViewById(R.id.langCard);
        }
    }
}
