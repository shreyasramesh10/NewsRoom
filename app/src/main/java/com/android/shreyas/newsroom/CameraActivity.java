package com.android.shreyas.newsroom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.shreyas.newsroom.models.Stories;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class CameraActivity extends AppCompatActivity {

    Firebase mref = new Firebase("https://popping-inferno-9534.firebaseio.com");

    public final static int PICK_PHOTO_CODE = 1046;
    public final static int TAKE_PHOTO_CODE = 2046;
    public String photoFileName = "photo.jpg";
    public final String APP_TAG = "NewsRoom";

    protected Toolbar toolbar;
    EditText title;
    EditText story;
    Button upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mref = new Firebase("https://popping-inferno-9534.firebaseio.com").child("stories");

//        cameraButton = (Button) findViewById(R.id.cam);
        title = (EditText) findViewById(R.id.titleEdit);
        story = (EditText) findViewById(R.id.bodyEdit);
        upload = (Button) findViewById(R.id.upload);
//        galleryButton = (Button) findViewById(R.id.gallery);
        //displayAll = (Button) findViewById(R.id.displayAll);

//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onLaunchCamera(v);
//            }
//        });
//
//        galleryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onPickPhoto(v);
//            }
//        });

        com.github.clans.fab.FloatingActionButton cameraItem = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.cameraItem);
        if(cameraItem!=null) {
            cameraItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLaunchCamera(v);
                }
            });
        }

        com.github.clans.fab.FloatingActionButton galleryItem = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.galleryItem);
        if(galleryItem!=null) {
            galleryItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPickPhoto(v);
                }
            });
        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stories stories = new Stories(story.getText().toString(),title.getText().toString());
                mref.child(mref.getAuth().getUid()).push().setValue(stories);
                stories.setKey(mref.getKey());

                Intent intent= new Intent(CameraActivity.this,UserStoriesActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});


        if (chooserIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(chooserIntent, PICK_PHOTO_CODE);
    }


    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, TAKE_PHOTO_CODE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICK_PHOTO_CODE:
                if(resultCode== RESULT_OK) {
                    if (data != null) {
                        Uri photoUri = data.getData();
                        // Do something with the photo based on Uri
                        Bitmap selectedImage = null;
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Load the selected image into a preview
                        ImageView ivPreview = (ImageView) findViewById(R.id.pic);

                        ivPreview.setImageBitmap(selectedImage);
                        upload.setClickable(true);
                        final Bitmap finalSelectedImage = selectedImage;
                        upload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyImageToString myImageDownload = new MyImageToString(upload);
                                myImageDownload.execute(finalSelectedImage);
                            }
                        });
                    }
                    else { // Result was a failure
                        Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case TAKE_PHOTO_CODE:
                if (resultCode == RESULT_OK) {
                    Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                    // by this point we have the camera photo on disk
                    final Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                    // Load the taken image into a preview
                    ImageView ivPreview = (ImageView) findViewById(R.id.pic);
                    ivPreview.setImageBitmap(takenImage);
                    upload.setClickable(true);

                    final Bitmap photo = decodeSampledBitmapFromFile(takenPhotoUri.getPath(), 120,
                            120);
                    ivPreview.setImageBitmap(photo);

                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyImageToString myImageDownload = new MyImageToString(upload);
                            myImageDownload.execute(photo);
                        }
                    });
                } else { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                                     int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    private class MyImageToString extends AsyncTask<Bitmap,Void,String>{

        private WeakReference<Button> buttonWeakReference;
        public MyImageToString(Button upload) {
            buttonWeakReference = new WeakReference<Button>(upload);
        }


        @Override
        protected String doInBackground(Bitmap... params) {
            String imageFile = null;
            for(Bitmap url : params) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                url.compress(Bitmap.CompressFormat.PNG, 50, stream);

                byte[] byteArray = stream.toByteArray();
                imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
            return imageFile;
        }

        @Override
        protected void onPostExecute(final String s) {
            Stories stories = new Stories(story.getText().toString(), s, title.getText().toString());
            mref.child(mref.getAuth().getUid()).push().setValue(stories);
            stories.setKey(mref.getKey());

            Intent intent= new Intent(CameraActivity.this,UserStoriesActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            Intent intent = new Intent(this, UserStoriesActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
