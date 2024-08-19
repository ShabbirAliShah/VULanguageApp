package com.example.vulanguageapp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LessonsModel {
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
    private boolean selected;

    public LessonsModel() {
        // Default constructor required for calls to DataSnapshot.getValue(LessonsModel.class)
    }

    public LessonsModel(String lessonId, String audioLink, String contentType, String courseId, String imageLink, String title, String videoLink, boolean selected, List<String> flashCards, List<String> exercises  ) {

        this.lessonId = lessonId;
        this.audioLink = audioLink;
        this.contentType = contentType;
        this.courseId = courseId;
        this.imageLink = imageLink;
        this.title = title;
        this.videoLink = videoLink;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
}