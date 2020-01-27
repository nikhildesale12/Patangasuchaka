
package com.gkvk.patangasuchaka.bean;

import java.io.Serializable;

public class UploadDataToWebRequest implements Serializable {

    private String image;

    private String username;

    private String place_cap;

    private String date_cap;

    private String butt_category;

    private String one_probability;

    private String one_scientific_name;

    private String one_common_name;

    private String two_probability;

    private String two_scientific_name;

    private String two_common_name;

    private String three_probability;

    private String three_scientific_name;

    private String three_common_name;

    private String lat;

    private String lng;

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

    public String getPlace_cap() {
        return place_cap;
    }

    public void setPlace_cap(String place_cap) {
        this.place_cap = place_cap;
    }

    public String getDate_cap() {
        return date_cap;
    }

    public void setDate_cap(String date_cap) {
        this.date_cap = date_cap;
    }

    public String getButt_category() {
        return butt_category;
    }

    public void setButt_category(String butt_category) {
        this.butt_category = butt_category;
    }

    public String getOne_probability() {
        return one_probability;
    }

    public void setOne_probability(String one_probability) {
        this.one_probability = one_probability;
    }

    public String getOne_scientific_name() {
        return one_scientific_name;
    }

    public void setOne_scientific_name(String one_scientific_name) {
        this.one_scientific_name = one_scientific_name;
    }

    public String getOne_common_name() {
        return one_common_name;
    }

    public void setOne_common_name(String one_common_name) {
        this.one_common_name = one_common_name;
    }

    public String getTwo_probability() {
        return two_probability;
    }

    public void setTwo_probability(String two_probability) {
        this.two_probability = two_probability;
    }

    public String getTwo_scientific_name() {
        return two_scientific_name;
    }

    public void setTwo_scientific_name(String two_scientific_name) {
        this.two_scientific_name = two_scientific_name;
    }

    public String getTwo_common_name() {
        return two_common_name;
    }

    public void setTwo_common_name(String two_common_name) {
        this.two_common_name = two_common_name;
    }

    public String getThree_probability() {
        return three_probability;
    }

    public void setThree_probability(String three_probability) {
        this.three_probability = three_probability;
    }

    public String getThree_scientific_name() {
        return three_scientific_name;
    }

    public void setThree_scientific_name(String three_scientific_name) {
        this.three_scientific_name = three_scientific_name;
    }

    public String getThree_common_name() {
        return three_common_name;
    }

    public void setThree_common_name(String three_common_name) {
        this.three_common_name = three_common_name;
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
}
