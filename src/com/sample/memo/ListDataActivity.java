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


public class ListDataActivity extends Activity {
    private ListView listView = null;
    private ArrayAdapter<String> adapter = null;
    private File file = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = new ListView(this);
        adapter = new ArrayAdapter<String>(this, 
                                           android.R.layout.simple_list_item_1,
                                           fileList());
        listView.setAdapter(adapter);
        listView.setSelection(1);
        
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView lv = (ListView)parent;
                    String s = (String)lv.getItemAtPosition(position);
                    Intent editorIntent = new Intent(ListDataActivity.this, 
                                                     com.sample.memo.EditorActivity.class);
                    editorIntent.putExtra("TITLE", s);
                    startActivity(editorIntent);
                }
            });

        /** 長押しクリックイベントで削除 */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView lv = (ListView)parent;
                    String s = (String)lv.getItemAtPosition(position);
                    final ArrayAdapter<String> aa = (ArrayAdapter<String>)parent.getAdapter();
                    if (Arrays.asList(fileList()).contains(s)) {
                        file = new File(s);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListDataActivity.this);
                        builder.setMessage("削除するよ？");
                        builder.setCancelable(true);
                        builder.setPositiveButton("して", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // notifyDataSetChangedで更新できるはず...(テスト環境 nexus7 2012)
                                    deleteFile(file.toString());
                                    aa.remove(file.toURI().toString());
                                    aa.notifyDataSetChanged();
                                    Intent intent = new Intent(ListDataActivity.this,
                                                               com.sample.memo.ListDataActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    ListDataActivity.this.finish();
                                    Toast.makeText(ListDataActivity.this, "削除した", Toast.LENGTH_LONG).show();
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
