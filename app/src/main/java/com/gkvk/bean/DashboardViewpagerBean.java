package com.gkvk.bean;

public class DashboardViewpagerBean {

    private int image;
    private String title;
    private String module;

    public DashboardViewpagerBean(int image, String title,String module) {
        this.image = image;
        this.title = title;
        this.module=module;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
