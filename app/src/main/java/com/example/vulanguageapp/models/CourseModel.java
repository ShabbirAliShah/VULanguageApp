package com.example.vulanguageapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseModel implements Serializable {

    private String language;
    private String level;
    private String title;
    private String courseKey; // For storing course ID
    private String courseDescription;

    public CourseModel(String language, String level, String title, String courseKey, String courseDescription) {
        this.language = language;
        this.level = level;
        this.title = title;
        this.courseKey = courseKey;
        this.courseDescription = courseDescription;
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

    public String getCourseKey() {
        return courseKey;
    }


    public String getCourseDescription() {
        return courseDescription;
    }

    // Setters
    public void setCourseDescription(String courseDescription){
        this.courseDescription = courseDescription;
    }

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
        this.courseKey = key;
    }

}