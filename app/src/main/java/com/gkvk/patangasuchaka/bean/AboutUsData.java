
package com.gkvk.patangasuchaka.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutUsData {

    @SerializedName("content_id")
    @Expose
    private String contentId;
    @SerializedName("intro")
    @Expose
    private String intro;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("howItworks")
    @Expose
    private String howItworks;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getHowItworks() {
        return howItworks;
    }

    public void setHowItworks(String howItworks) {
        this.howItworks = howItworks;
    }

}
