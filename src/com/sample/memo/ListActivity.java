package com.sample.memo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.Intent;


public class ListActivity extends Activity {
    private ListView listView = null;
    private AlertDialog.Builder dialog = null;
    private String[] files = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setContentView(R.layout.main);
        dialog = new AlertDialog.Builder(this);
        listView = new ListView(this);
        files = fileList();        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                                                                android.R.layout.simple_list_item_1,
                                                                files);
        listView.setAdapter(adapter);
        //listView.setSelection(1); /* 何の設定値？？？ */
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
        /* 長押しクリックイベントで削除 */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView listView = (ListView)parent;
                    String s = (String)listView.getItemAtPosition(position);
                    //ここに処理を書く
                    return false;
                }
            });        
        setContentView(listView);
    }
}
