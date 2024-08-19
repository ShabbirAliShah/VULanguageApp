package com.example.vulanguageapp.models;

public class UserAccountModel {

    private String userId;
    private String displayName;
    private String proficiencyLevel;
    private String description;

    public UserAccountModel(String userId, String userName, String description, String proficiencyLevel) {
        this.userId = userId;
        this.displayName = userName;
        this.description = description;
        this.proficiencyLevel = proficiencyLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserAccountModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return displayName;
    }

    public void setUserName(String userName) {
        this.displayName = userName;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }
}
