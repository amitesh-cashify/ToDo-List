package com.example.apple.fragmentdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class LoadImageFromDatabase extends AsyncTask<String, Void, Bitmap> {

    private ImageView mImageView;


    LoadImageFromDatabase(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    @Override
    protected void onCancelled() {

    }

    @Override
    protected Bitmap doInBackground(String[] params) {

        if (params == null || TextUtils.isEmpty(params[0])) {
            return null;
        }
        Bitmap bitmap;
        getStatus();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (mImageView == null) {
            return;
        }
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
            return;
        }
        mImageView.setImageResource(R.mipmap.ic_launcher);
    }
}