package com.example.vulanguageapp.models;

public class QuizResult {
    private String userId;
    private String lessonId;
    private int score;

    public QuizResult(String userId, String lessonId, int score) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public int getScore() {
        return score;
    }
}
