package com.example.vulanguageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.FlashCardModel;

import java.util.ArrayList;

public class GamificationAdapter extends RecyclerView.Adapter<GamificationAdapter.ViewHolder>{

    private ArrayList<FlashCardModel> dataList;
    private Context context;

    public GamificationAdapter(ArrayList<FlashCardModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_flashcards, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FlashCardModel flashCard = dataList.get(position);

        holder.cardText.setText(flashCard.getCardText());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView flashCardIcon;
        TextView cardText;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            flashCardIcon = itemView.findViewById(R.id.cardIcon);
            cardText = itemView.findViewById(R.id.cardText);
        }
    }
}
