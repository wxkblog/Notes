package com.wang.administrator.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/16.
 */
public class AddContent extends Activity implements View.OnClickListener {

    private String flag;
    private Button saveButton, deleteButton;
    private EditText text;
    private ImageView image;
    private VideoView video;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private File phoneFile,videoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_content);
        flag = getIntent().getStringExtra("flag");

        initView();
    }

    private void initView() {
        saveButton = (Button) findViewById(R.id.save);
        deleteButton = (Button) findViewById(R.id.delete);
        text = (EditText) findViewById(R.id.add_text);
        image = (ImageView) findViewById(R.id.add_image);
        video = (VideoView) findViewById(R.id.add_video);
        saveButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

        hideView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addDB();
                finish();
                break;
            case R.id.delete:
                finish();
                break;
        }
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, text.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        Log.e("PATH",phoneFile+"");
        Log.e("videoPATH",videoFile+"");
        cv.put(NotesDB.PATH,phoneFile+"");
        cv.put(NotesDB.VIDEO,videoFile+"");
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy年mm月DD日 HH:mm:ss");
        Date date = new Date();
        String time = format.format(date);
        return time;
    }

    private void hideView() {
        if (flag.equals("1")) {
            image.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
        }
        if (flag.equals("2")) {
            image.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phoneFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".jpg");
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
            startActivityForResult(imageIntent,1);
        }
        if (flag.equals("3")) {
            image.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            Intent VideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".mp4");
            VideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(VideoIntent,2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            Bitmap bitmap= BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }
        if(requestCode ==2){
            video.setVideoURI(Uri.fromFile(videoFile));
            video.start();
        }
    }
}
