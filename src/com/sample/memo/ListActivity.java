package com.sample.memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;

import java.io.File;
import java.util.Arrays;


public class ListActivity extends Activity {
    private ListView listView = null;
    private AlertDialog.Builder dialog = null;
    private File file = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dialog = new AlertDialog.Builder(this);
        listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                                                                android.R.layout.simple_list_item_1,
                                                                fileList());
        listView.setAdapter(adapter);
        /** 何の設定値？？？ */
        //listView.setSelection(1);
 
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView listView = (ListView)parent;
                    String s = (String)listView.getItemAtPosition(position);                    
                    Intent editorIntent = new Intent(ListActivity.this, 
                                                     com.sample.memo.EditorActivity.class);
                    editorIntent.putExtra("TITLE", s);
                    startActivity(editorIntent);
                }
            });
        /** 長押しクリックイベントで削除 */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView listView = (ListView)parent;
                    String s = (String)listView.getItemAtPosition(position);
                    if (Arrays.asList(fileList()).contains(s)) {
                        file = new File(s);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                        final ListView lv = listView;
                        builder.setMessage("削除するよ？");
                        builder.setCancelable(true);
                        builder.setPositiveButton("して", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteFile(file.toString());
                                    lv.invalidate();
                                    Toast.makeText(ListActivity.this, "削除した", Toast.LENGTH_LONG).show();                                    
                                }
                            });
                        builder.setNegativeButton("ダメ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                        builder.show();
                    }
                    return false;
                }
            });
    }
}
