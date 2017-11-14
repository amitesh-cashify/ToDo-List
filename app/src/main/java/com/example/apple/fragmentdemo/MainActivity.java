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
    private EditText enteredName;
    private EditText enteredMobileNo;
    private EditText enteredEmailId;
    private EditText enteredAddress;
    private String name;
    private String mobileNo;
    private String email;
    private String address;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    private static final int REQUEST_TAKE_PHOTO = 1;
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
                        // clearEnteredText();
                        Intent intent = new Intent(this, DisplayActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Please capture image also", Toast.LENGTH_SHORT).show();
                    }
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
        values.put(FeedEntry.COLUMN_NAME, name);
        values.put(FeedEntry.COLUMN_IMAGE_NAME, photoFile + "");
        values.put(FeedEntry.COLUMN_MOBILE_NO, mobileNo);
        values.put(FeedEntry.COLUMN_EMAIL, email);
        values.put(FeedEntry.COLUMN_ADDRESS, address);
        long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
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
                //  Log.d(LOG, "photoUri = " + photoUri + "");
                //  Log.d(LOG, "photoFile = " + photoFile + "");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            mImageView = (ImageView) findViewById(R.id.imageCaptureButton);

            int width = mImageView.getWidth();
            int height = mImageView.getHeight();
            // Log.d(LOG, "String width=" + width + "");
            // Log.d(LOG, "String height=" + height + "");

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            //Log.d(LOG, "photoW width=" + photoW + "");
            //Log.d(LOG, "photoH height=" + photoH + "");

            int scaleFactor = Math.min(photoW / width, photoH / height);
            //Log.d(LOG, "scaleFactor =" + scaleFactor + "");
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
            try {
                OutputStream imageFile = new FileOutputStream(photoFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, imageFile);

                mImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File sd = Environment.getExternalStorageDirectory();
        File file = getExternalFilesDir("Pictures");
        //  Log.d(LOG, "file = " + file + "");
        //  Log.d(LOG, "sd = " + sd + "");
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //   Log.d(LOG, "StorageDir = " + storageDir + "");
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        // Log.d(LOG, "imagePole = " + image + "");
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
