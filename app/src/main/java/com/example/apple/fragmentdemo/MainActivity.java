package com.example.apple.fragmentdemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private EditText enteredName;
    private EditText enteredMobileNo;
    private EditText enteredEmailId;
    private EditText enteredAddress;
    private String name;
    private String mobileNo;
    private String email;
    private String address;
    private ImageView mImageView;
    private String mCurrentPhotoPath;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        enteredName = (EditText) findViewById(R.id.editText1);
        enteredMobileNo = (EditText) findViewById(R.id.editText2);
        enteredEmailId = (EditText) findViewById(R.id.editText3);
        enteredAddress = (EditText) findViewById(R.id.editText4);
        mImageView = (ImageView) findViewById(R.id.imageCaptureButton);
        findViewById(R.id.submitButton).setOnClickListener(this);
        findViewById(R.id.getSavedDetailsButton).setOnClickListener(this);
        findViewById(R.id.imageCaptureButton).setOnClickListener(this);
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

            case R.id.imageCaptureButton:
                dispatchTakePictureIntent();
                break;
        }

    }

    private void clearEnteredText() {
        enteredName.setText("");
        enteredMobileNo.setText("");
        enteredEmailId.setText("");
        enteredAddress.setText("");
        mImageView.setImageResource(R.mipmap.ic_launcher);
    }

    private void insertFeedData() {

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.COLUMN_NAME, name);
        values.put(FeedReaderDbHelper.COLUMN_IMAGE_NAME, photoFile + "");
        values.put(FeedReaderDbHelper.COLUMN_MOBILE_NO, mobileNo);
        values.put(FeedReaderDbHelper.COLUMN_EMAIL, email);
        values.put(FeedReaderDbHelper.COLUMN_ADDRESS, address);
        long newRowId = db.insert(FeedReaderDbHelper.TABLE_NAME, null, values);
        db.close();
        String LOG = "MainActivity";
        Log.v(LOG, "isInserted = " + "" + newRowId);
    }

    private boolean isValidData() {
        name = enteredName.getText().toString().trim();
        mobileNo = enteredMobileNo.getText().toString().trim();
        email = enteredEmailId.getText().toString().trim();
        address = enteredAddress.getText().toString().trim();

        if (!Pattern.matches(getString(R.string.NAME_VALIDATION), name)) {
            Toast.makeText(this, R.string.error_valid_name, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Pattern.matches(getString(R.string.MOBILE_VALIDATION), mobileNo)) {
            Toast.makeText(this, R.string.alert_valid_mobileNo, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Pattern.matches(String.valueOf(Patterns.EMAIL_ADDRESS), email)) {
            Toast.makeText(this, R.string.alert_valid_email, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Pattern.matches(getString(R.string.ADDRESS_VALIDATION), address)) {
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
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.apple.fragmentdemo", photoFile);
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
            OutputStream imageFile = new FileOutputStream(photoFile);
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
