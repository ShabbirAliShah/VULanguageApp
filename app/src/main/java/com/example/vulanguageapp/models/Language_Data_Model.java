package com.example.vulanguageapp.models;

public class Language_Data_Model {

    private String key;
    private String name;
    private String country;
    private String languageDescription;

    public Language_Data_Model(){

    }


    public Language_Data_Model(String key, String languageDescription, String country, String name) {
        this.key = key;
        this.languageDescription = languageDescription;
        this.country = country;
        this.name = name;
    }

    public String getKey(){return key;}
    public String getName() {
        return name;
    }
    public String getCountry() {
        return country;
    }
    public String getLanguageDescription() {
        return languageDescription;
    }

    public void setKey(String languageKey) {
        this.key = languageKey;
    }
}
