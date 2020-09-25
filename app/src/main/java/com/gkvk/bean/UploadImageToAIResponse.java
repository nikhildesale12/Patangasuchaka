
package com.gkvk.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImageToAIResponse {


    @SerializedName("choice")
    @Expose
    private Integer choice;
    @SerializedName("butterfly_sn")
    @Expose
    private String butterflySn;
    @SerializedName("butterfly_cn")
    @Expose
    private String butterflyCn;
    @SerializedName("probability")
    @Expose
    private String probability;
    @SerializedName("category")
    @Expose
    private String category;

    public Integer getChoice() {
        return choice;
    }

    public void setChoice(Integer choice) {
        this.choice = choice;
    }

    public String getButterflySn() {
        return butterflySn;
    }

    public void setButterflySn(String butterflySn) {
        this.butterflySn = butterflySn;
    }

    public String getButterflyCn() {
        return butterflyCn;
    }

    public void setButterflyCn(String butterflyCn) {
        this.butterflyCn = butterflyCn;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
