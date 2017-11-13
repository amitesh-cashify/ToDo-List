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
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * Created by apple on 11/8/17.
 * FeedCustomAdapter
 */

class FeedCustomAdapter extends BaseAdapter {

    private Context context;
    private List<FeedEntryData> list;

    FeedCustomAdapter(Context context, List<FeedEntryData> items) {
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

//
//        if (bitmap != null) {
//            ((ImageView) convertView.findViewById(R.id.listViewImage)).setImageBitmap(bitmap);
//        } else {
//            ((ImageView) convertView.findViewById(R.id.listViewImage)).setImageResource(R.mipmap.ic_launcher);
//        }

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

class LoadImageFromDatabase extends AsyncTask<String, Void, Bitmap> {

    private ImageView mImageView;
    private String path;

    LoadImageFromDatabase(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        this.path = params[0];
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path);
        //Log.d("file  =", file + "");
      //  Log.d("path=", this.path);
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null && mImageView != null) {
            mImageView.setImageBitmap(bitmap);
        } else {
            mImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
