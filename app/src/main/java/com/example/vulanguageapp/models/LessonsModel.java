package com.example.vulanguageapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class LessonsModel {
    @Exclude
    private String lessonId;
    private String audioLink;
    private String contentType;
    private String courseId;
    private List<String> exercises;
    private List<String> flashCards;
    private String imageLink;
    private List<Map<String, Object>> quiz;
    private String title;
    private String videoLink;

    @Exclude
    private Boolean isSelected;
    @Exclude
    private Boolean isCompleted;

    public LessonsModel() {
        // Default constructor required for calls to DataSnapshot.getValue(LessonsModel.class)
    }

    public LessonsModel(String lessonId, String audioLink, String contentType, String courseId, String imageLink, String title, String videoLink, List<String> flashCards, List<String> exercises) {

        this.lessonId = lessonId;
        this.audioLink = audioLink;
        this.contentType = contentType;
        this.courseId = courseId;
        this.imageLink = imageLink;
        this.title = title;
        this.videoLink = videoLink;
        this.flashCards = flashCards;
        this.exercises = exercises;
     }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    @Exclude
    public boolean isSelected() {
        return isSelected != null ? isSelected : false;
    }

    @Exclude
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public List<String> getExercises() {
        return exercises;
    }

    public void setExercises(List<String> exercises) {
        this.exercises = exercises;
    }

    public List<String> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(List<String> flashCards) {
        this.flashCards = flashCards;
    }

    @Exclude
    public Boolean getIsCompleted() {  // Add this getter
        return isCompleted;
    }

    @Exclude
    public void setIsCompleted(Boolean isCompleted) {  // Add this setter
        this.isCompleted = isCompleted;
    }
}