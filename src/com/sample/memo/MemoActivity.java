package com.sample.memo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.Intent;


public class MemoActivity extends Activity {
    private AlertDialog.Builder dialog = null;
    private TextView memoView = null;
    private Button editorButton = null;
    private Button listButton = null;
    private Button twitterButton = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        dialog = new AlertDialog.Builder(this);
        memoView = (TextView)findViewById(R.id.memoView);
        editorButton = (Button)findViewById(R.id.editorButton);
        editorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editorIntent = new Intent(MemoActivity.this, 
                                                     com.sample.memo.EditorActivity.class);
                    startActivity(editorIntent);
                }
            });
        listButton = (Button)findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent listIntent = new Intent(MemoActivity.this, 
                                                   com.sample.memo.ListActivity.class);
                    startActivity(listIntent);
                }
            });         
        twitterButton = (Button)findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ////////////////////////
                    dialog.setTitle("test");
                    dialog.setMessage("twitter");
                    dialog.show();
                }    
            });
    }
}