package com.example.apple.fragmentdemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 11/7/17.
 * DisplayActivity
 */

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        findViewById(R.id.addNewDetailsButton).setOnClickListener(this);

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(FeedEntry.TABLE_NAME, null, null, null, null, null, null);

        List<FeedEntryData> items = new ArrayList<>();

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID));
            String imageName = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_IMAGE_NAME));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME));
            String mobile = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_MOBILE_NO));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_EMAIL));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_ADDRESS));

            items.add(new FeedEntryData(itemId, imageName, name, mobile, email, address));
        }
        cursor.close();
        db.close();
        initListView(items);
    }

    private void initListView(List<FeedEntryData> items) {
//        ListViewCustomAdapter myCustomAdapter = new ListViewCustomAdapter(this, items);
//        ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setAdapter(myCustomAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);

       // GridView gridView = (GridView) findViewById(R.id.gridView);


        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerViewCustomAdapter recyclerViewAdapter = new RecyclerViewCustomAdapter(items);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewDetailsButton:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }
    }
}
