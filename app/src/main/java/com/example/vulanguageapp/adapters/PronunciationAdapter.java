package com.example.vulanguageapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.PronunciationModel;

import java.util.ArrayList;
import java.util.List;

public class PronunciationAdapter extends RecyclerView.Adapter<PronunciationAdapter.ViewHolder> {

    private List<PronunciationModel> vocabData;
    private NavController navController;

    public PronunciationAdapter(List<PronunciationModel> vocabData, NavController navController) {
        this.vocabData = vocabData;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_pronunciation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PronunciationModel pronunciateVocab = vocabData.get(position);
        holder.vocabText.setText(pronunciateVocab.getVocabText());
        holder.vocabDescription.setText(pronunciateVocab.getVocabDescription());

    }

    @Override
    public int getItemCount() {

        return vocabData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView microPhoneIcon, speakerIcon;
        TextView vocabText, vocabDescription;

        public ViewHolder(View itemView){
            super(itemView);

            microPhoneIcon = itemView.findViewById(R.id.microphoneIcon);
            speakerIcon = itemView.findViewById(R.id.speakerIcon);
            vocabText = itemView.findViewById(R.id.vocabularyText);
            vocabDescription = itemView.findViewById(R.id.vocabDescription);

        }
    }
}
