package com.gkvk.bean;

import java.io.Serializable;

public class UploadToWebResponse implements Serializable {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
