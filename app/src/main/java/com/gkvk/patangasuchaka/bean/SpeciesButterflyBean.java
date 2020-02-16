package com.gkvk.patangasuchaka.bean;

import java.io.Serializable;

public class SpeciesButterflyBean implements Serializable {

    private String common_name;
    private String species_list;

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getSpecies_list() {
        return species_list;
    }

    public void setSpecies_list(String species_list) {
        this.species_list = species_list;
    }
}
