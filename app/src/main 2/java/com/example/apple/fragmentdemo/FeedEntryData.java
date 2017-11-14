package com.example.apple.fragmentdemo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by apple on 11/7/17.
 * FeedEntryData
 */

public class FeedEntryData {
    private long itemId;
    private String imageName;
    private String name;
    private String mobile;
    private String email;
    private String address;

    public FeedEntryData(long itemId, String imageName, String name, String mobile, String email, String address) {

        this.itemId = itemId;
        this.imageName = imageName;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
    }

    public long getItemId() {
        return itemId;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }



}

