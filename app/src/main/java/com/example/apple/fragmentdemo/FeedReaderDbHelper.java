package com.example.apple.fragmentdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by apple on 11/7/17.
 * FeedReaderDbHelper
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "FeedReader3.db";


    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE " + FeedEntry.TABLE_NAME + "(" +
            FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FeedEntry.COLUMN_IMAGE_NAME + " TEXT, " +
            FeedEntry.COLUMN_NAME + " TEXT, " +
            FeedEntry.COLUMN_MOBILE_NO + " TEXT, " +
            FeedEntry.COLUMN_EMAIL + " TEXT, " +
            FeedEntry.COLUMN_ADDRESS + " TEXT)";


    FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL(SQL_DELETE_ENTRIES);
        //  onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onDowngrade(db, oldVersion, newVersion);
    }


}
