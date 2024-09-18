package com.example.vulanguageapp.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import java.util.HashMap;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EnrollmentModel implements Serializable {

    private String enrollmentId;
    private String courseId;
    private String courseTitle;
    private String userId;
    private Map<String, Lesson> selectedLessons;  // Map for storing lessons

    // Default constructor required for calls to DataSnapshot.getValue(EnrollmentModel.class)
    public EnrollmentModel() {
    }

    // Partial constructor to pass limited arguments from a fragment
    public EnrollmentModel(String courseId, String courseTitle, String enrollmentId) {
        this.courseId = courseId;
        this.enrollmentId = enrollmentId;
        this.courseTitle = courseTitle;
        this.selectedLessons = new HashMap<>();  // Initialize the map
    }

    // Full constructor to pass all arguments
    public EnrollmentModel(String courseId, String courseTitle, String userId, Map<String, Lesson> selectedLessons) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.userId = userId;
        this.selectedLessons = selectedLessons;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    // Getters and Setters
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Lesson> getSelectedLessons() {
        return selectedLessons;
    }
    public void setSelectedLessons(Map<String, Lesson> selectedLessons) {
        this.selectedLessons = selectedLessons;
    }

    // Convert the object to a Map for Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("courseId", courseId);
        result.put("courseTitle", courseTitle);
        result.put("userId", userId);
        result.put("selectedLessons", selectedLessons);  // Include selectedLessons as a map
        return result;
    }

    // Inner class to represent each lesson
    @IgnoreExtraProperties  // Ignore extra fields if present in Firebase data
    public static class Lesson implements Serializable {
        private boolean completed;
        private boolean selected;

        // Default constructor
        public Lesson() {
        }

        // Constructor for creating a lesson
        public Lesson(boolean completed, boolean selected) {
            this.completed = completed;
            this.selected = selected;
        }

        // Getters and Setters
        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        // Convert the Lesson object to a Map for Firebase
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("completed", completed);
            result.put("selected", selected);
            return result;
        }
    }
}
