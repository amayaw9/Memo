package com.sample.memo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.Intent;


public class TwitterActivity extends Activity {
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

        /** 認証画面の表示とOAuth */

        /** ツイートを取得して表示する 
        listView.setAdapter(adapter);
        */

        //listView.setSelection(1); /* 何の設定値？？？ */
        /** 個々のツイートを選択したら...
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        */

        /** フリックしたときに更新するListener登録 */
        setContentView(listView);
    }
}
