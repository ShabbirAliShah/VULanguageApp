package com.example.vulanguageapp.models;

public class PronunciationModel {

    private String vocabText;
    private String vocabDescription;

    public PronunciationModel() {
    }

    public PronunciationModel(String vocabText, String vocabDescription) {
        this.vocabText = vocabText;
        this.vocabDescription = vocabDescription;
    }

    public String getVocabText() {
        return vocabText;
    }

    public void setVocabText(String vocabText) {
        this.vocabText = vocabText;
    }

    public String getVocabDescription() {
        return vocabDescription;
    }

    public void setVocabDescription(String vocabDescription) {
        this.vocabDescription = vocabDescription;
    }

}
