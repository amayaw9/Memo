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
import android.widget.Toast;
import android.content.Intent;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Date;


public class EditorActivity extends Activity {
    private FileOutputStream fileOutputStream = null;
    private TextView titleTextView = null;
    private EditText titleEditText = null;
    private ImageButton cameraButton = null;
    private TextView bodyTextView = null;
    private EditText bodyEditText = null;
    private Button saveButton = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            
        String title = getIntent().getStringExtra("TITLE");
        if (title != null) {
            /** 既存ファイルの編集ならばファイルを読み込む */
            setContentView(R.layout.editor);
            try {
                FileInputStream fileInputStream = openFileInput(title);
                byte[] b = new byte[fileInputStream.available()];
                fileInputStream.read(b);
                titleEditText = (EditText)findViewById(R.id.titleEditText);
                titleEditText.setText(title);
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
                        
                        Intent cameraIntent = new Intent();
                        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                              Uri.fromFile(new File(Environment.getExternalStorageDirectory(), 
                                                                    new Date().toString())));
                        //startActivityForResult(cameraIntent, 1);
                        finish();
                    }
                    
                    /** startActivityForResult(カメラアプリ)から
                        呼び出し元へ戻る前に呼ばれるメソッド 
                    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                        if (resultCode == RESULT_CANCELED && data.getData() == null) {
                                return;
                        }
                        fileOutputStream = openFileOutput(s, MODE_PRIVATE);
                        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                    }
                    */
                });
        }

        titleTextView = (TextView)findViewById(R.id.titleTextView);
        bodyTextView = (TextView)findViewById(R.id.bodyTextView);
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String s = titleEditText.getText().toString();
                        if (Arrays.asList(fileList()).contains(s)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                            builder.setMessage("上書き保存するよ？");
                            builder.setCancelable(false);
                            builder.setPositiveButton("して", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        EditorActivity.this.finish();
                                    }
                                });
                            builder.setNegativeButton("ダメ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        }
                                });
                            builder.show();
                        } else {
                            fileOutputStream = openFileOutput(s, MODE_PRIVATE);
                            fileOutputStream.write(bodyEditText.getText().toString().getBytes());
                            Toast.makeText(EditorActivity.this, "保存した", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //finish();
                }
            });
    }
}
