package com.example.vulanguageapp.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Enrollment implements Serializable {

    private String courseId;
    private String courseTitle;
    private HashMap<String, Boolean> selectedLessons; // Using HashMap<String, Boolean> to store lessonId and isCompleted status
    private String userId;

    public Enrollment() {
        // Default constructor required for calls to DataSnapshot.getValue(Enrollment.class)
    }

    public Enrollment(String courseId, String courseTitle, HashMap<String, Boolean> selectedLessons, String userId) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.selectedLessons = selectedLessons;
        this.userId = userId;
    }

    // Getters and setters for each field

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public HashMap<String, Boolean> getSelectedLessons() {
        return selectedLessons;
    }

    public void setSelectedLessons(HashMap<String, Boolean> selectedLessons) {
        this.selectedLessons = selectedLessons;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
