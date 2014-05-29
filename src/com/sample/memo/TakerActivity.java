package com.sample.memo;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class TakerActivity extends Activity {
    private Button takeButton = null;
    private ImageView captureView = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taker);
        takeButton = (Button)findViewById(R.id.takeButton);
        captureView = (ImageView)findViewById(R.id.captureView);

        takeButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {          
        if(requestCode == 0 && resultCode == Activity.RESULT_OK ){
            try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日(E)HH時mm分ss秒");
                String s = simpleDateFormat.format(calendar.getTime());
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                FileOutputStream fileOutputStream = openFileOutput(s + ".jpg", MODE_PRIVATE);
                captureView.setImageBitmap(capturedImage);
                capturedImage.compress(CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}