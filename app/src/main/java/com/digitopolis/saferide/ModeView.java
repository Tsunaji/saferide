package com.digitopolis.saferide;

import android.graphics.Bitmap;

/**
 * Created by Bank on 9/3/2015.
 */
public class ModeView  {
    Bitmap bitmap;
    String name;

    public void ModeView(Bitmap bitmap,String name){
        this.bitmap = bitmap;
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
