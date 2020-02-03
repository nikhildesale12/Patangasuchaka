
package com.gkvk.patangasuchaka.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryData implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("place_cap")
    @Expose
    private String placeCap;
    @SerializedName("date_cap")
    @Expose
    private String dateCap;
    @SerializedName("date_uploaded")
    @Expose
    private String dateUploaded;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("butt_category")
    @Expose
    private String buttCategory;
    @SerializedName("one_choice")
    @Expose
    private String oneChoice;
    @SerializedName("one_probability")
    @Expose
    private String oneProbability;
    @SerializedName("one_scientific_name")
    @Expose
    private String oneScientificName;
    @SerializedName("one_common_name")
    @Expose
    private String oneCommonName;
    @SerializedName("two_choice")
    @Expose
    private String twoChoice;
    @SerializedName("two_probability")
    @Expose
    private String twoProbability;
    @SerializedName("two_scientific_name")
    @Expose
    private String twoScientificName;
    @SerializedName("two_common_name")
    @Expose
    private String twoCommonName;
    @SerializedName("three_choice")
    @Expose
    private String threeChoice;
    @SerializedName("three_probability")
    @Expose
    private String threeProbability;
    @SerializedName("three_scientific_name")
    @Expose
    private String threeScientificName;
    @SerializedName("three_common_name")
    @Expose
    private String threeCommonName;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;

    public HistoryData(Parcel in) {
        this.id = in.readString();
        this.image = in.readString();
        this.username = in.readString();
        this.placeCap = in.readString();
        this.dateCap = in.readString();
        this.dateUploaded = in.readString();
        this.email = in.readString();
        this.oneChoice = in.readString();
        this.oneProbability = in.readString();
        this.oneScientificName = in.readString();
        this.oneCommonName = in.readString();
        this.twoChoice = in.readString();
        this.twoProbability = in.readString();
        this.twoScientificName = in.readString();
        this.twoCommonName = in.readString();
        this.threeChoice = in.readString();
        this.threeCommonName = in.readString();
        this.threeProbability = in.readString();
        this.threeScientificName = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlaceCap() {
        return placeCap;
    }

    public void setPlaceCap(String placeCap) {
        this.placeCap = placeCap;
    }

    public String getDateCap() {
        return dateCap;
    }

    public void setDateCap(String dateCap) {
        this.dateCap = dateCap;
    }

    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getButtCategory() {
        return buttCategory;
    }

    public void setButtCategory(String buttCategory) {
        this.buttCategory = buttCategory;
    }

    public String getOneChoice() {
        return oneChoice;
    }

    public void setOneChoice(String oneChoice) {
        this.oneChoice = oneChoice;
    }

    public String getOneProbability() {
        return oneProbability;
    }

    public void setOneProbability(String oneProbability) {
        this.oneProbability = oneProbability;
    }

    public String getOneScientificName() {
        return oneScientificName;
    }

    public void setOneScientificName(String oneScientificName) {
        this.oneScientificName = oneScientificName;
    }

    public String getOneCommonName() {
        return oneCommonName;
    }

    public void setOneCommonName(String oneCommonName) {
        this.oneCommonName = oneCommonName;
    }

    public String getTwoChoice() {
        return twoChoice;
    }

    public void setTwoChoice(String twoChoice) {
        this.twoChoice = twoChoice;
    }

    public String getTwoProbability() {
        return twoProbability;
    }

    public void setTwoProbability(String twoProbability) {
        this.twoProbability = twoProbability;
    }

    public String getTwoScientificName() {
        return twoScientificName;
    }

    public void setTwoScientificName(String twoScientificName) {
        this.twoScientificName = twoScientificName;
    }

    public String getTwoCommonName() {
        return twoCommonName;
    }

    public void setTwoCommonName(String twoCommonName) {
        this.twoCommonName = twoCommonName;
    }

    public String getThreeChoice() {
        return threeChoice;
    }

    public void setThreeChoice(String threeChoice) {
        this.threeChoice = threeChoice;
    }

    public String getThreeProbability() {
        return threeProbability;
    }

    public void setThreeProbability(String threeProbability) {
        this.threeProbability = threeProbability;
    }

    public String getThreeScientificName() {
        return threeScientificName;
    }

    public void setThreeScientificName(String threeScientificName) {
        this.threeScientificName = threeScientificName;
    }

    public String getThreeCommonName() {
        return threeCommonName;
    }

    public void setThreeCommonName(String threeCommonName) {
        this.threeCommonName = threeCommonName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.image);
        dest.writeString(this.username);
        dest.writeString(this.placeCap);
        dest.writeString(this.dateCap);
        dest.writeString(this.dateUploaded);
        dest.writeString(this.email);
        dest.writeString(this.oneChoice);
        dest.writeString(this.oneProbability);
        dest.writeString(this.oneScientificName);
        dest.writeString(this.oneCommonName);
        dest.writeString(this.twoChoice);
        dest.writeString(this.twoCommonName);
        dest.writeString(this.twoProbability);
        dest.writeString(this.twoScientificName);
        dest.writeString(this.threeChoice);
        dest.writeString(this.threeCommonName);
        dest.writeString(this.threeProbability);
        dest.writeString(this.threeScientificName);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public HistoryData createFromParcel(Parcel in) {
            return new HistoryData(in);
        }

        public HistoryData[] newArray(int size) {
            return new HistoryData[size];
        }
    };
}
