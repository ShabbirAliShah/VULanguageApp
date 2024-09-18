package com.example.vulanguageapp.models;

public class FlashCardModel {
    private String cardText;
    private String forLanguage;
    private String inLanguageText;
    private String cardBackText;
    private String cardImageLink;

    public FlashCardModel(){

    }

    public FlashCardModel(String textForPronunciation,String forLanguage, String inLanguageText, String cardBackText, String cardImageLink) {
        this.cardText = textForPronunciation;
        this.cardBackText = cardBackText;
        this.cardImageLink = cardImageLink;
        this.forLanguage = forLanguage;
        this.inLanguageText = inLanguageText;
    }

    public String getForLanguage() {
        return forLanguage;
    }

    public void setForLanguage(String forLanguage) {

        this.forLanguage = forLanguage;
    }

    public String getInLanguageText() {
        return inLanguageText;
    }

    public void setInLanguageText(String inLanguageText) {

        this.inLanguageText = inLanguageText;
    }

    public String getCardImage() {
        return cardImageLink;
    }

    public void setCardImage(String cardImage) {
        this.cardImageLink = cardImage;
    }

    public void  setCardBackText(String cardBackText){
        this.cardBackText = cardBackText;
    }

    public String getCardBackText(){
        return cardBackText;
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }

}
