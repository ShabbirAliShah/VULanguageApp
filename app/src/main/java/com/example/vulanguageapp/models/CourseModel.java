package com.example.vulanguageapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseModel implements Serializable {

    private String language;
    private String level;
    private String title;
    private String key; // For storing course ID
    private ArrayList<String> lessons; // For storing lessons

    public CourseModel(String language, String level, String title, String key, ArrayList<String> lessons) {
        this.language = language;
        this.level = level;
        this.title = title;
        this.key = key;
        this.lessons = lessons;
    }
    public CourseModel(){

    }

    // Getters
    public String getLanguage() {
        return language;
    }

    public String getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }

    public ArrayList<String> getLessons() {
        return lessons;
    }

    // Setters
    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLessons(ArrayList<String> lessons) {
        this.lessons = lessons;
    }
}