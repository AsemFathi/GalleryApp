package com.asem.photomangment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {
    String imgPath;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

       imgPath = getIntent().getStringExtra("imagePath");
        imageView = findViewById(R.id.idIVImage);
        scaleGestureDetector = new ScaleGestureDetector(this , new ScaleListner());
        File imgFile = new File(imgPath);
        if (imgFile.exists())
            Picasso.get().load(imgFile)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);

    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        // inside on touch event method we are calling on
        // touch event method and passing our motion event to it.
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListner implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(@NonNull ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor , 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }

        @Override
        public boolean onScaleBegin(@NonNull ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override
        public void onScaleEnd(@NonNull ScaleGestureDetector scaleGestureDetector) {

        }
    }
}