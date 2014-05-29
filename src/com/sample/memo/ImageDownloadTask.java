package com.sample.memo;  
  
import android.content.Context;
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.os.Handler;  
import android.widget.ImageView;  

import java.io.BufferedInputStream;  
import java.io.FileInputStream;  


public class ImageDownloadTask {  
    private ImageView imageView;  
    private String fileName;
    private Bitmap bitmap;  

    public ImageDownloadTask(ImageView imageView, String fileName){  
        this.imageView = imageView;
        this.fileName = fileName;
    }  
  
    public void execute(){  
        final Handler handler = new Handler();  
        new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FileInputStream in = new FileInputStream("data/data/com.sample.memo/" + fileName);
                        bitmap = BitmapFactory.decodeStream(in);  
                        in.close();     
                        if(bitmap == null) 
                            return;   
                        handler.post(new Runnable() {
                                @Override
                                public void run() {  
                                    imageView.setImageBitmap(bitmap);  
                                }  
                            });  
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }  
            }).start();  
    }  
}  
