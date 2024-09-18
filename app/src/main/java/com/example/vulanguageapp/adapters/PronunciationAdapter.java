package com.example.vulanguageapp.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.PronunciationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PronunciationAdapter extends RecyclerView.Adapter<PronunciationAdapter.ViewHolder> {

    private final List<PronunciationModel> vocabData;
    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private Locale userSelectedLanguage;
    private final String languageCode;
    private final Context context;

    public PronunciationAdapter(List<PronunciationModel> vocabData,Context context, String userLanguageCode) {
        this.vocabData = vocabData;
        this.languageCode = userLanguageCode;
        this.context = context;
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

        // Dynamically set the locale based on the language code passed to the adapter
        Locale vocabLocale = getLocaleFromLanguageCode(languageCode);

        holder.vocabText.setText(pronunciateVocab.getVocabText());
        holder.vocabDescription.setText(pronunciateVocab.getVocabDescription());

        // Initialize TextToSpeech for pronunciation
        textToSpeech = new TextToSpeech(holder.itemView.getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(vocabLocale); // Set the dynamic locale for pronunciation
            }
        });

        // Set OnClickListener for speakerIcon
        holder.speakerIcon.setOnClickListener(v -> {
            textToSpeech.speak(pronunciateVocab.getVocabText(), TextToSpeech.QUEUE_FLUSH, null, null);
        });

        // Initialize SpeechRecognizer if not already initialized
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(holder.itemView.getContext());
        }

        // Set OnClickListener for microPhoneIcon
        holder.microPhoneIcon.setOnClickListener(v -> {
            startListeningForPronunciation(holder, pronunciateVocab.getVocabText(), vocabLocale); // Pass the dynamic locale
        });
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

    // Helper method to convert language code to Locale
    private Locale getLocaleFromLanguageCode(String languageName) {
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
        return userSelectedLanguage;
    }

    // Helper method to map error codes to readable messages
    private String getErrorText(int errorCode) {
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                return "Audio recording error";
            case SpeechRecognizer.ERROR_CLIENT:
                return "Client side error";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                return "Insufficient permissions";
            case SpeechRecognizer.ERROR_NETWORK:
                return "Network error";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                return "Network timeout";
            case SpeechRecognizer.ERROR_NO_MATCH:
                return "No match found";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                return "RecognitionService busy";
            case SpeechRecognizer.ERROR_SERVER:
                return "Error from server";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                return "No speech input";
            default:
                return "Unknown error";
        }
    }

    // Method to start listening for user pronunciation
    private void startListeningForPronunciation(ViewHolder holder, String correctWord, Locale vocabLocale) {
        if (vocabLocale == null) {
            Toast.makeText(holder.itemView.getContext(), "Language not supported.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted; proceed with speech recognition
                startListeningForPronunciation(holder, correctWord, userSelectedLanguage);
            } else {
                Toast.makeText(activity, "Permission to record audio is required.", Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Set the speech recognizer to listen in the appropriate language
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, vocabLocale.toString());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedWord = matches.get(0);
                    if (recognizedWord.equalsIgnoreCase(correctWord)) {
                        Toast.makeText(holder.itemView.getContext(), "Correct pronunciation!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // Implement other required methods for RecognitionListener
            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}

            @Override public void onError(int error) {
                // Call the getErrorText method to get the error message
                String errorMessage = getErrorText(error);
                Toast.makeText(holder.itemView.getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }


            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        speechRecognizer.startListening(intent);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

}
