package com.example.vulanguageapp.models;

import java.util.ArrayList;

public class UserAccountModel {

    private String userId;
    private String status;
    private String displayName;
    private String proficiencyLevel;
    private String description;
    private ArrayList<String> wishlist;

    public UserAccountModel(ArrayList<String> wishlist) {
        this.wishlist = wishlist;
    }

    public UserAccountModel(String userId, String userName, String description, String proficiencyLevel, String status) {
        this.userId = userId;
        this.displayName = userName;
        this.description = description;
        this.proficiencyLevel = proficiencyLevel;
        this.status = status;
    }

    public ArrayList<String> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<String> wishlist) {
        this.wishlist = wishlist;
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

    public String getStatus() {
        return status;
    }
}
