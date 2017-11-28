package com.example.apple.fragmentdemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private EditText mEnteredName;
    private EditText mEnteredMobileNo;
    private EditText mEnteredEmailId;
    private EditText mEnteredAddress;
    private String mName;
    private String mMobileNo;
    private String mEmail;
    private String mAddress;
    private ImageView mImageView;
    private String mCurrentPhotoPath;
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mEnteredName = (EditText) findViewById(R.id.et_name);
        mEnteredMobileNo = (EditText) findViewById(R.id.et_mobile_no);
        mEnteredEmailId = (EditText) findViewById(R.id.et_email);
        mEnteredAddress = (EditText) findViewById(R.id.et_address);
        mImageView = (ImageView) findViewById(R.id.round_image);

        findViewById(R.id.submitButton).setOnClickListener(this);
        findViewById(R.id.getSavedDetailsButton).setOnClickListener(this);
        findViewById(R.id.round_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submitButton:
                if (isValidData()) {
                    isValidData();
                    if (mImageView != null) {
                        insertFeedData();
                        Intent intent = new Intent(this, DisplayActivity.class);
                        startActivity(intent);
                        return;
                    }
                    Toast.makeText(this, "Please capture image also", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.getSavedDetailsButton:
                Intent intent1 = new Intent(this, DisplayActivity.class);
                startActivity(intent1);
                break;

            case R.id.round_image:
                dispatchTakePictureIntent();
                break;
        }

    }

    private void clearEnteredText() {
        mEnteredName.setText("");
        mEnteredMobileNo.setText("");
        mEnteredEmailId.setText("");
        mEnteredAddress.setText("");
        mImageView.setImageResource(R.mipmap.ic_launcher);
    }

    private void insertFeedData() {

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.COLUMN_NAME, mName);
        values.put(FeedReaderDbHelper.COLUMN_IMAGE_NAME, mPhotoFile + "");
        values.put(FeedReaderDbHelper.COLUMN_MOBILE_NO, mMobileNo);
        values.put(FeedReaderDbHelper.COLUMN_EMAIL, mEmail);
        values.put(FeedReaderDbHelper.COLUMN_ADDRESS, mAddress);
        long newRowId = db.insert(FeedReaderDbHelper.TABLE_NAME, null, values);
        db.close();
        String LOG = "MainActivity";
        Log.v(LOG, "isInserted = " + "" + newRowId);
    }

    private boolean isValidData() {
        mName = mEnteredName.getText().toString().trim();
        mMobileNo = mEnteredMobileNo.getText().toString().trim();
        mEmail = mEnteredEmailId.getText().toString().trim();
        mAddress = mEnteredAddress.getText().toString().trim();

        if (!Pattern.matches(getString(R.string.NAME_VALIDATION), mName)) {
            Toast.makeText(this, R.string.error_valid_name, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Pattern.matches(getString(R.string.MOBILE_VALIDATION), mMobileNo)) {
            Toast.makeText(this, R.string.alert_valid_mobileNo, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Pattern.matches(String.valueOf(Patterns.EMAIL_ADDRESS), mEmail)) {
            Toast.makeText(this, R.string.alert_valid_email, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Pattern.matches(getString(R.string.ADDRESS_VALIDATION), mAddress)) {
            Toast.makeText(this, R.string.alert_valid_address, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mPhotoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.apple.fragmentdemo", mPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setCapturedImage();
        }
    }

    private void setCapturedImage() {
        int width = mImageView.getWidth();
        int height = mImageView.getHeight();
        Bitmap bitmap = getCompressedBitmap(width, height);
        try {
            OutputStream imageFile = new FileOutputStream(mPhotoFile);
            Matrix matrix = new Matrix();
          //  Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
          //  mImageView.setScaleType(ImageView.ScaleType.MATRIX);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 5, imageFile);
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getCompressedBitmap(int width, int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW / width, photoH / height);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
