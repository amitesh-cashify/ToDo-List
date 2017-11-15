package com.example.apple.fragmentdemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
        FeedReaderDbHelper l_dbHelper = new FeedReaderDbHelper(this);
        List<FeedEntryData> l_items = l_dbHelper.getFeedList();
        initListView(l_items);
    }

    private void initListView(List<FeedEntryData> items) {
//        ListViewCustomAdapter myCustomAdapter = new ListViewCustomAdapter(this, items);
//        ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setAdapter(myCustomAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view0);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        //GridLayoutManager layoutManager1 = new GridLayoutManager(this,4);
//        recyclerView.setLayoutManager(layoutManager1);
//        layoutManager1.setOrientation(GridLayoutManager.VERTICAL);

        RecyclerViewCustomAdapter recyclerViewAdapter = new RecyclerViewCustomAdapter(items);
        recyclerView.setAdapter(recyclerViewAdapter);


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.my_recycler_view1);
        recyclerView1.setLayoutManager(layoutManager1);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerViewCustomAdapter recyclerViewCustomAdapter1 = new RecyclerViewCustomAdapter(items);
        recyclerView1.setAdapter(recyclerViewCustomAdapter1);
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
