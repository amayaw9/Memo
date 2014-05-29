package com.sample.memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Date;


public class EditorActivity extends Activity {
    private FileOutputStream fileOutputStream = null;
    private FileInputStream fileInputStream = null;
    private TextView titleTextView = null;
    private EditText titleEditText = null;
    private ImageButton cameraButton = null;
    private TextView bodyTextView = null;
    private EditText bodyEditText = null;
    private Button saveButton = null;
    private ImageView pictureView = null;
    private String defaultTitle = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            
        defaultTitle = getIntent().getStringExtra("TITLE"); 
        if (defaultTitle != null && defaultTitle.length() >= 4) {
            int len = defaultTitle.length();
            if (defaultTitle.substring(len-4, len).equals(".jpg")) {
                /** 画像ファイルならば写真を表示 */
                setContentView(R.layout.picture);
                pictureView = (ImageView)findViewById(R.id.pictureView);
                try {
                    fileInputStream = openFileInput(defaultTitle);
                    Bitmap pictureImage = BitmapFactory.decodeStream(fileInputStream);
                    pictureView.setImageBitmap(pictureImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (defaultTitle != null) {
                /** 既存ファイルの編集ならばファイルを読み込む */
                setContentView(R.layout.editor);
                try {
                    FileInputStream fileInputStream = openFileInput(defaultTitle);
                    byte[] b = new byte[fileInputStream.available()];
                    fileInputStream.read(b);
                    titleEditText = (EditText)findViewById(R.id.titleEditText);
                    titleEditText.setText(defaultTitle);
                    bodyEditText = (EditText)findViewById(R.id.bodyEditText);
                    bodyEditText.setText(new String(b));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                /** 新規作成ならばカメラ付きのUIを生成 */
                setContentView(R.layout.camera);
                titleEditText = (EditText)findViewById(R.id.titleEditText);
                bodyEditText = (EditText)findViewById(R.id.bodyEditText);
                cameraButton = (ImageButton)findViewById(R.id.cameraButton);
                cameraButton.setOnClickListener(new OnClickListener() {
                        @Override
                        /** カメラの起動 */
                        public void onClick(View view) {
                            Intent takerIntent = new Intent(EditorActivity.this,
                                                        com.sample.memo.TakerActivity.class);
                            startActivity(takerIntent);
                            EditorActivity.this.finish();
                        }
                    });
            }
        
            titleTextView = (TextView)findViewById(R.id.titleTextView);
            bodyTextView = (TextView)findViewById(R.id.bodyTextView);
            saveButton = (Button)findViewById(R.id.saveButton);
            
            /** ファイル保存の処理 */
            saveButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String s = titleEditText.getText().toString();
                            if (Arrays.asList(fileList()).contains(s)) {
                                /** 上書き保存 */
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                                builder.setMessage("上書き保存するよ？");
                                builder.setCancelable(false);
                                builder.setPositiveButton("して", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                fileOutputStream = openFileOutput(titleEditText.getText().toString(), MODE_PRIVATE);
                                                fileOutputStream.write(bodyEditText.getText().toString().getBytes());
                                                fileOutputStream.close();
                                                Toast.makeText(EditorActivity.this, "保存した", Toast.LENGTH_LONG).show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                builder.setNegativeButton("ダメ", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                                builder.show();
                            } else {
                                /** 新規保存 or タイトルを変更,元のファイルは削除 */
                                if (defaultTitle != null) {
                                    deleteFile(defaultTitle);
                                }                                
                                fileOutputStream = openFileOutput(s, MODE_PRIVATE);
                                fileOutputStream.write(bodyEditText.getText().toString().getBytes());
                                fileOutputStream.close();
                                Toast.makeText(EditorActivity.this, "保存した", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (defaultTitle != null) {
            Intent listDataIntent = new Intent(EditorActivity.this,
                                               com.sample.memo.ListDataActivity.class);
            startActivity(listDataIntent);
            overridePendingTransition(0, 0);
        }
    }
}
