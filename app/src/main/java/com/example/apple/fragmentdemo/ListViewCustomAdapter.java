package com.example.apple.fragmentdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Created by apple on 11/8/17.
 * ListViewCustomAdapter
 */

class ListViewCustomAdapter extends BaseAdapter {

    private Context context;
    private List<FeedEntryData> list;

    ListViewCustomAdapter(Context context, List<FeedEntryData> items) {
        this.context = context;
        this.list = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_viewlayout, parent, false);
        }

        FeedEntryData feedEntryData = list.get(position);

        ((TextView) convertView.findViewById(R.id.textViewId)).setText(feedEntryData.getItemId() + "");

        ImageView mImageView = (ImageView) convertView.findViewById(R.id.listViewImage);
        new LoadImageFromDatabase(mImageView).execute(feedEntryData.getImageName());

        ((TextView) convertView.findViewById(R.id.textViewName)).setText(feedEntryData.getName());
        ((TextView) convertView.findViewById(R.id.textViewMobileNo)).setText(feedEntryData.getMobile());
        ((TextView) convertView.findViewById(R.id.textViewEmail)).setText(feedEntryData.getEmail());
        ((TextView) convertView.findViewById(R.id.textViewAddress)).setText(feedEntryData.getAddress());
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}


