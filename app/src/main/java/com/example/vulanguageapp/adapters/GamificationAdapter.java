package com.example.vulanguageapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.FlashCardModel;

import java.util.ArrayList;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class GamificationAdapter extends RecyclerView.Adapter<GamificationAdapter.ViewHolder> {

    private ArrayList<FlashCardModel> dataList;
    private Context context;
    private TextToSpeech tts;
    private Locale userSelectedLanguage;
    private String languageCode;

    public GamificationAdapter(ArrayList<FlashCardModel> dataList, Context context, String userLanguageCode) {
        this.dataList = dataList;
        this.context = context;
        this.languageCode = userLanguageCode;

        // Initialize Text-to-Speech
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Set the language after initialization
                    setLanguageForTTS(languageCode);
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
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
        holder.inLanguageText.setText(flashCard.getInLanguageText());
        holder.cardBackText.setText(flashCard.getCardBackText());

        // Set up click listener for the pronunciation button
        holder.speakWord.setOnClickListener(v -> {
            pronunciationFunction(flashCard.getInLanguageText());  // Pass the word to the pronunciation function
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardText, inLanguageText, cardBackText;
        ImageButton speakWord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardText = itemView.findViewById(R.id.cardText);
            inLanguageText = itemView.findViewById(R.id.inLanguageText); // Text for pronunciation
            cardBackText = itemView.findViewById(R.id.cardBackText);
            speakWord = itemView.findViewById(R.id.speakWord);
        }
    }

    // Pronunciation function
    private void pronunciationFunction(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // Method to set the language for TTS based on the user's selection
    private void setLanguageForTTS(String languageName) {
        // Determine the Locale based on the full language name
        switch (languageName.toLowerCase()) {
            case "english":
                userSelectedLanguage = Locale.ENGLISH;
                break;
            case "french":
                userSelectedLanguage = Locale.FRENCH;
                break;
            case "spanish":
                userSelectedLanguage = new Locale("es", "ES"); // Spanish
                break;
            case "pashto":
                userSelectedLanguage = new Locale("ps", "AF"); // Pashto
                break;
            case "urdu":
                userSelectedLanguage = new Locale("ur", "PK"); // Urdu
                break;
            case "hindi":
                userSelectedLanguage = new Locale("hi", "IN"); // Urdu
                break;
            default:
                userSelectedLanguage = Locale.ENGLISH;  // Default to English
                break;
        }

        // Check if the selected language is available in TTS engine
        int result = tts.setLanguage(userSelectedLanguage);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Prompt user to download the required language data
            Toast.makeText(context, "Required language data is missing or not supported. Please download the language data.", Toast.LENGTH_LONG).show();

            // Redirect user to download language data
            Intent installTTSIntent = new Intent();
            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            context.startActivity(installTTSIntent);
        } else {
            Toast.makeText(context, userSelectedLanguage.getDisplayLanguage() + " language set", Toast.LENGTH_SHORT).show();
        }
    }


    // Clean up the Text-to-Speech engine
    public void shutdownTTS() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}

