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

class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.MainViewHolder> {

    private List<FeedEntryData> list;
    public List<String> imageUrl = new ArrayList<>();

    RecyclerViewCustomAdapter(List<FeedEntryData> items) {
        this.list = items;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 1:
                return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_viewlayout, parent, false));
            case 2:
                //return new Feed2ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_layout, parent,false));
        }
        return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_viewlayout, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        addUrls();
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    private class FeedViewHolder extends MainViewHolder {


        private TextView mTxtMobile;
        private TextView mTxtEmail;
        private TextView mTxtName;
        private TextView mTxtAddress;
        private TextView mTxtId;
        private ImageView mImageView;

        FeedViewHolder(View convertView) {
            super(convertView);
            mTxtId = ((TextView) convertView.findViewById(R.id.textViewId));
            mTxtName = ((TextView) convertView.findViewById(R.id.textViewName));
            mTxtMobile = ((TextView) convertView.findViewById(R.id.textViewMobileNo));
            mTxtEmail = ((TextView) convertView.findViewById(R.id.textViewEmail));
            mTxtAddress = (TextView) convertView.findViewById(R.id.textViewAddress);
            mImageView = (ImageView) convertView.findViewById(R.id.listViewImage);
        }

        void bind(FeedEntryData feedEntryData) {

            mTxtId.setText(feedEntryData.getItemId() + "");
            mTxtName.setText(feedEntryData.getName());
            mTxtEmail.setText(feedEntryData.getEmail());
            mTxtMobile.setText(feedEntryData.getMobile());
            mTxtAddress.setText(feedEntryData.getAddress());

            new LoadImageFromDatabase(mImageView).execute(getURL());
        }
    }

    private class Feed2ViewHolder extends MainViewHolder {


        private final TextView mTxtMobile;
        private final TextView mTxtEmail;
        private final TextView mTxtName;
        private final TextView mTxtAddress;
        private final TextView mTxtId;
        private final ImageView mImageView;

        Feed2ViewHolder(View convertView) {
            super(convertView);
            mTxtId = ((TextView) convertView.findViewById(R.id.textViewName));
            mTxtName = ((TextView) convertView.findViewById(R.id.textViewName));
            mTxtMobile = ((TextView) convertView.findViewById(R.id.textViewMobileNo));
            mTxtEmail = ((TextView) convertView.findViewById(R.id.textViewEmail));
            mTxtAddress = (TextView) convertView.findViewById(R.id.textViewAddress);
            mImageView = (ImageView) convertView.findViewById(R.id.listViewImage);
        }

        void bind(FeedEntryData feedEntryData) {
            mTxtId.setText(feedEntryData.getItemId() + "");
            mTxtName.setText(feedEntryData.getName());
            mTxtEmail.setText(feedEntryData.getEmail());
            mTxtMobile.setText(feedEntryData.getMobile());
            mTxtAddress.setText(feedEntryData.getAddress());
            new LoadImageFromDatabase(mImageView).execute("https://goo.gl/P1Jxrq");
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
        Log.d("random Number", "" + randomNum);
        return imageUrl.get(randomNum);
    }

    public void addURL() {
        imageUrl.add("https://goo.gl/P1Jxrq");
        imageUrl.add("https://goo.gl/QGCg8n");
        imageUrl.add("https://goo.gl/QGCg8n");
        imageUrl.add("https://goo.gl/E1iBwR");
    }
}




