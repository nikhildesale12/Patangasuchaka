package com.gkvk.bean;

import java.io.Serializable;

public class History implements Serializable {

    private String imageViewSpecies;
    private String textViewSpeciesName;
    private String textViewCommonName;
    private String textViewPlace;
    private String textViewDate;

    public History(String imageViewspecies, String textViewSpeciesName, String textViewCommonName, String textViewPlace, String textViewDate) {
        this.imageViewSpecies = imageViewspecies;
        this.textViewSpeciesName = textViewSpeciesName;
        this.textViewCommonName = textViewCommonName;
        this.textViewPlace = textViewPlace;
        this.textViewDate = textViewDate;
    }

    public String getImageViewSpecies() {
        return imageViewSpecies;
    }

    public void setImageViewSpecies(String imageViewSpecies) {
        this.imageViewSpecies = imageViewSpecies;
    }

    public String getTextViewSpeciesName() {
        return textViewSpeciesName;
    }

    public void setTextViewSpeciesName(String textViewSpeciesName) {
        this.textViewSpeciesName = textViewSpeciesName;
    }

    public String getTextViewCommonName() {
        return textViewCommonName;
    }

    public void setTextViewCommonName(String textViewCommonName) {
        this.textViewCommonName = textViewCommonName;
    }

    public String getTextViewPlace() {
        return textViewPlace;
    }

    public void setTextViewPlace(String textViewPlace) {
        this.textViewPlace = textViewPlace;
    }

    public String getTextViewDate() {
        return textViewDate;
    }

    public void setTextViewDate(String textViewDate) {
        this.textViewDate = textViewDate;
    }


}
