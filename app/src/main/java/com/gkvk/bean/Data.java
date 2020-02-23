
package com.gkvk.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("verified")
    @Expose
    private String verified;
    @SerializedName("Is_admin")
    @Expose
    private String isAdmin;
    @SerializedName("Is_blocked")
    @Expose
    private String isBlocked;
    @SerializedName("mod_timestamp")
    @Expose
    private String modTimestamp;
    @SerializedName("total_upload")
    @Expose
    private String totalUpload;
    @SerializedName("profile_img")
    @Expose
    private Object profileImg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getModTimestamp() {
        return modTimestamp;
    }

    public void setModTimestamp(String modTimestamp) {
        this.modTimestamp = modTimestamp;
    }

    public String getTotalUpload() {
        return totalUpload;
    }

    public void setTotalUpload(String totalUpload) {
        this.totalUpload = totalUpload;
    }

    public Object getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(Object profileImg) {
        this.profileImg = profileImg;
    }

}
