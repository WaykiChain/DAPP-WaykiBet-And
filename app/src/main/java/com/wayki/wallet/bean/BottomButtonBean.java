package com.wayki.wallet.bean;

import android.graphics.Bitmap;

public class BottomButtonBean {
    private Bitmap bitmap;
    private  int id;

    public BottomButtonBean(Bitmap bitmap, int id) {
        this.bitmap = bitmap;
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
