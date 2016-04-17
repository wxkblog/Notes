package com.wang.administrator.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.sql.SQLData;
import java.sql.SQLDataException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button textButton, imageButton, videoButton;
    private ListView listView;
    private Intent intent;
    private MyAdapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        listView = (ListView) findViewById(R.id.list);
        textButton = (Button) findViewById(R.id.text);
        imageButton = (Button) findViewById(R.id.image);
        videoButton = (Button) findViewById(R.id.video);
        textButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);

        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
    }

    @Override
    public void onClick(View v) {
        intent = new Intent(this, AddContent.class);
        switch (v.getId()) {
            case R.id.text:
                intent.putExtra("flag", "1");
                break;
            case R.id.image:
                intent.putExtra("flag", "2");
                break;
            case R.id.video:
                intent.putExtra("flag", "3");
                break;
        }
        startActivity(intent);
    }

    public void selectDB() {
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        adapter = new MyAdapter(this, cursor);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
