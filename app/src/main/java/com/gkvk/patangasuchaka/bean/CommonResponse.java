
package com.gkvk.patangasuchaka.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("full_name")
    @Expose
    private String full_name;
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
    private String Is_admin;
    @SerializedName("Is_blocked")
    @Expose
    private String Is_blocked;
    @SerializedName("mod_timestamp")
    @Expose
    private String mod_timestamp;
    @SerializedName("total_upload")
    @Expose
    private String total_upload;
    @SerializedName("profile_img")
    @Expose
    private String profile_img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public String getIs_admin() {
        return Is_admin;
    }

    public void setIs_admin(String is_admin) {
        Is_admin = is_admin;
    }

    public String getIs_blocked() {
        return Is_blocked;
    }

    public void setIs_blocked(String is_blocked) {
        Is_blocked = is_blocked;
    }

    public String getMod_timestamp() {
        return mod_timestamp;
    }

    public void setMod_timestamp(String mod_timestamp) {
        this.mod_timestamp = mod_timestamp;
    }

    public String getTotal_upload() {
        return total_upload;
    }

    public void setTotal_upload(String total_upload) {
        this.total_upload = total_upload;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }
}
