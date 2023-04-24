package com.asem.photomangment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    FloatingActionButton camera;
    private RecyclerViewAdapter imageRVAdapter;
    private BroadcastReceiver photosChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            imagePaths = new ArrayList<>();
            getImagePath();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        camera = findViewById(R.id.fab);
        imagePaths = new ArrayList<>();
        imagesRV = findViewById(R.id.idRVImages);

        prepareRecyclerView();

        requestPermissions();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , ImageCaputureClass.class);
                startActivity(intent);

            }
        });


    }

    private boolean checkPermissions (){
        int result = ActivityCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions()
    {
        if (checkPermissions())
        {
            //Toast.makeText(this, "Permission granted.!!!!!! ", Toast.LENGTH_SHORT).show();
            getImagePath();
        }
        else
            requestPermission();
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void prepareRecyclerView(){
        imageRVAdapter = new RecyclerViewAdapter(MainActivity.this , imagePaths);
        GridLayoutManager manager= new GridLayoutManager(MainActivity.this , 4);
        imagesRV.setLayoutManager(manager);
        imageRVAdapter.notifyDataSetChanged();
        imagesRV.setAdapter(imageRVAdapter);

    }

    private void getImagePath()
    {
        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent)
        {
            final String[] columns = {MediaStore.Images.Media.DATA , MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns , null , null , orderBy);

            int count = cursor.getCount();

            for (int i = 0 ; i < count ; i++)
            {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imagePaths.add(cursor.getString(dataColumnIndex));

            }
            imageRVAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the BroadcastReceiver to listen for changes to the photos
        IntentFilter intentFilter = new IntentFilter("com.example.gallery.PHOTOS_CHANGED");
        registerReceiver(photosChangedReceiver, intentFilter);
        imagePaths = new ArrayList<>();
        // Reload the photos in case they have changed
        getImagePath();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the BroadcastReceiver to avoid memory leaks
        unregisterReceiver(photosChangedReceiver);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccept) {
                        Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                        getImagePath();
                    } else
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
                break;
        }

    }

}