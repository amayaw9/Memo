package com.sample.memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;


public class GridDataActivity extends Activity {
    private GridView gridView = null;
    private ImageGridViewAdapter adapter = null;
    private File file = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        adapter = new ImageGridViewAdapter(this, Arrays.asList(fileList()));
        
        gridView = new GridView(this);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(4);
        setContentView(gridView);

        /** クリックイベントで編集 */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GridView lv = (GridView)parent;
                    String s = (String)lv.getItemAtPosition(position);
                    Intent editorIntent = new Intent(GridDataActivity.this, 
                                                     com.sample.memo.EditorActivity.class);
                    editorIntent.putExtra("TITLE", s);
                    startActivity(editorIntent);
                    GridDataActivity.this.finish();
                }
            });
        
        /** 長押しクリックイベントで削除 */
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    GridView lv = (GridView)parent;
                    String s = (String)lv.getItemAtPosition(position);
                    final ImageGridViewAdapter igva = (ImageGridViewAdapter)parent.getAdapter();
                    if (Arrays.asList(fileList()).contains(s)) {
                        file = new File(s);
                        AlertDialog.Builder builder = new AlertDialog.Builder(GridDataActivity.this);
                        builder.setMessage("削除するよ？");
                        builder.setCancelable(true);
                        builder.setPositiveButton("して", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // notifyDataSetChangedで更新できるはず...(テスト環境 nexus7 2012)
                                    deleteFile(file.toString());
                                    igva.remove(file.toURI().toString());
                                    igva.notifyDataSetChanged();
                                    Intent gridDataIntent = new Intent(GridDataActivity.this,
                                                                       com.sample.memo.GridDataActivity.class);
                                    startActivity(gridDataIntent);
                                    overridePendingTransition(0, 0);
                                    GridDataActivity.this.finish();
                                    Toast.makeText(GridDataActivity.this, "削除した", Toast.LENGTH_LONG).show();
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
    
    /** ActionBar関連 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem actionItem = menu.add("一覧をリスト表示に変更");
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent listDataIntent = new Intent(GridDataActivity.this,
                                           com.sample.memo.ListDataActivity.class);
        startActivity(listDataIntent);
        overridePendingTransition(0, 0);
        GridDataActivity.this.finish();
        
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("VIEW_OF_TABLE", getResources().getInteger(R.integer.list));
        editor.commit();
        return true;
    }
}
