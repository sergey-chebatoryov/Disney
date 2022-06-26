package com.example.disney_time02;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

public class DataModel {

    private final Bitmap image;
    private final String name;

    public DataModel(String name, String imageUrl) {
        this.name = name;
        this.image = LoadImageFromUrl(imageUrl);
    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        return image;
    }

    private Bitmap LoadImageFromUrl(String url) {
        try {
            String imageUrl = "https://i.ytimg.com/vi/" + getId(url) + "/0.jpg";
            return BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
        } catch (Exception e) {
            return null;
        }
    }

    private String getId(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

}