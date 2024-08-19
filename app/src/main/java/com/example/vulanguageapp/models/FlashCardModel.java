package com.example.vulanguageapp.models;

public class FlashCardModel {
    private String courseId;
    private String cardText;
    private String cardBackText;
    private int cardImage;

    public FlashCardModel(){

    }

    public FlashCardModel(String courseId, String cardText, String cardBackText, int cardImage) {
        this.courseId = courseId;
        this.cardText = cardText;
        this.cardBackText = cardBackText;
        this.cardImage = cardImage;
    }

    public int getCardImage() {
        return cardImage;
    }

    public void setCardImage(int cardImage) {
        this.cardImage = cardImage;
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

    public String getCourseTitle() {
        return courseId;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseId = courseTitle;
    }
}
