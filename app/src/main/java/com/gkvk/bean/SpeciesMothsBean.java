package com.gkvk.bean;

import java.io.Serializable;

public class SpeciesMothsBean implements Serializable {

    private String CN;
    private String SPS_list;

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public String getSPS_list() {
        return SPS_list;
    }

    public void setSPS_list(String SPS_list) {
        this.SPS_list = SPS_list;
    }
}
