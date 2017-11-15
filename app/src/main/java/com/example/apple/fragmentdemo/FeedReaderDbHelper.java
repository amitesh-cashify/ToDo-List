package com.example.apple.fragmentdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by apple on 11/7/17.
 * FeedReaderDbHelper
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "FeedReader3.db";
    public static final String TABLE_NAME = "entryTable";
    public static final String ID = "id";
    public static final String COLUMN_IMAGE_NAME = "imageName";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MOBILE_NO = "mobileNo";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ADDRESS = "address";

    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE " + TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_IMAGE_NAME + " TEXT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_MOBILE_NO + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_ADDRESS + " TEXT)";

   public FeedReaderDbHelper(Context context) {
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


    public List<FeedEntryData> getFeedList() {

        List<FeedEntryData> items = new ArrayList<>();
        try {


            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ID));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_NAME));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE_NO));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));

                items.add(new FeedEntryData(itemId, imageName, name, mobile, email, address));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
