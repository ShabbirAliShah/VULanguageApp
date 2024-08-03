package com.example.vulanguageapp.models;

public class Language_Data_Model {

    private String name;
    private String country;
    private String languageDescription;

    public Language_Data_Model(){

    }


    public Language_Data_Model(String languageDescription, String country, String name) {
        this.languageDescription = languageDescription;
        this.country = country;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getCountry() {
        return country;
    }
    public String getLanguageDescription() {
        return languageDescription;
    }

}
