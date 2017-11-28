package com.example.apple.fragmentdemo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by apple on 11/13/17.
 * RecyclerViewCustomAdapter
 */

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.MainViewHolder> {

    private static final int INT_2 = 2;
    private static final int INT_1 = 1;
    private List<FeedEntryData> list;
    private List<String> imageUrl;
    private static final String TAG = "RecyclerView";

    RecyclerViewCustomAdapter(List<FeedEntryData> items) {
        this.list = items;
        this.imageUrl = new ArrayList<>();
        addUrls();
    }


    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_viewlayout, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % INT_2 == 0) {
            return INT_1;
        } else {
            return INT_2;
        }
    }

    private class FeedViewHolder extends MainViewHolder {


        private TextView _mTxtMobile;
        private TextView _mTxtEmail;
        private TextView _mTxtName;
        private TextView _mTxtAddress;
        private TextView _mTxtId;
        private ImageView _mImageView;

        FeedViewHolder(View convertView) {
            super(convertView);
            _mTxtId = ((TextView) convertView.findViewById(R.id.textViewId));
            _mTxtName = ((TextView) convertView.findViewById(R.id.textViewName));
            _mTxtMobile = ((TextView) convertView.findViewById(R.id.textViewMobileNo));
            _mTxtEmail = ((TextView) convertView.findViewById(R.id.textViewEmail));
            _mTxtAddress = (TextView) convertView.findViewById(R.id.textViewAddress);
            _mImageView = (ImageView) convertView.findViewById(R.id.listViewImage);
        }

        void bind(FeedEntryData p_feedEntryData) {

            _mTxtId.setText(p_feedEntryData.getItemId() + "");
            _mTxtName.setText(p_feedEntryData.getName());
            _mTxtEmail.setText(p_feedEntryData.getEmail());
            _mTxtMobile.setText(p_feedEntryData.getMobile());
            _mTxtAddress.setText(p_feedEntryData.getAddress());

            new LoadImageFromDatabase(_mImageView).execute(getURL());
        }
    }

    abstract class MainViewHolder extends RecyclerView.ViewHolder {

        MainViewHolder(View convertView) {
            super(convertView);
        }

        abstract void bind(FeedEntryData feedEntryData);
    }

    public String getURL() {
        Random rn = new Random();
        int max = 3;
        int min = 0;

        int randomNum = rn.nextInt(max - min + 1) + min;
        Log.d(TAG, "Random Number: " + randomNum);
        return imageUrl.get(randomNum);
    }

    private void addUrls() {
        imageUrl.add("https://goo.gl/P1Jxrq");
        imageUrl.add("https://goo.gl/QGCg8n");
        imageUrl.add("https://goo.gl/QGCg8n");
        imageUrl.add("https://goo.gl/E1iBwR");
    }
}




