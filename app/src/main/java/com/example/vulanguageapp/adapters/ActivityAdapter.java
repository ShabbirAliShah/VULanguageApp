package com.example.vulanguageapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.interfaces.MainActivityClickListener;
import com.example.vulanguageapp.interfaces.MainActivityFragmentClickListener;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private ArrayList<Language_Data_Model> dataList;
    private MainActivityFragmentClickListener fragmentlistener;
    private MainActivityClickListener mainActivityListener;

    public ActivityAdapter(ArrayList<Language_Data_Model> dataList,
                           MainActivityFragmentClickListener fragmentlistener) {

        this.dataList = dataList;
        this.fragmentlistener = fragmentlistener;
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
        //holder.keyText.setText(langModel.getKey());
        holder.countryText.setText(langModel.getCountryName());
        holder.languageText.setText(langModel.getLanguageName());

        holder.langCard.setOnClickListener(fragmentlistener::onLangCardClick);

    }

    @Override
    public int getItemCount() {
        return  dataList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView keyText, languageText, countryText;
        LinearLayout langCard;
        Button btnLanguageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            //keyText = itemView.findViewById(R.id.keyTextView);
            countryText = itemView.findViewById(R.id.countryTextView);
            languageText = itemView.findViewById(R.id.langTextView);
            langCard = itemView.findViewById(R.id.langCard);
           // btnLanguageView = itemView.findViewById(R.id.btnLanguageView);
        }
    }
}
