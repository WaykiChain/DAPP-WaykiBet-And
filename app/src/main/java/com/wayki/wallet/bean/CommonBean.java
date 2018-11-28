package com.wayki.wallet.bean;

import android.graphics.drawable.Drawable;

public class CommonBean {
    private Drawable image;
    private String name;
    private int id;

    public CommonBean(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public CommonBean(Drawable image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
