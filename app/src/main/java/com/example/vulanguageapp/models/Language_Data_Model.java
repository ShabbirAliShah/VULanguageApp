package com.example.vulanguageapp.models;

public class Language_Data_Model {

    private String key;
    private String languageName;
    private String countryName;
    private String languageDescription;

    public Language_Data_Model(){

    }

    public Language_Data_Model(String key, String languageName, String countryName){
        this.key = key;
        this.languageName = languageName;
        this.countryName = countryName;
    }

    public Language_Data_Model(String languageName, String languageDescription){
        this.languageName = languageName;
        this.languageDescription = languageDescription;}

    public Language_Data_Model(String languageName){
        this.languageName = languageName;
    }

    public String getKey() {
        return key;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getCountryName() {
        return countryName;
    }
    public String getLanguageDescription(){
        return languageDescription;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
