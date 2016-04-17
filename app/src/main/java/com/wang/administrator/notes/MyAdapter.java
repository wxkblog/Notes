package com.wang.administrator.notes;

import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by Administrator on 2016/4/16.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout linearLayout;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.item, null);
        TextView contentView = (TextView) linearLayout.findViewById(R.id.list_text);
        TextView timeView = (TextView) linearLayout.findViewById(R.id.list_time);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.list_image);
        ImageView videoView = (ImageView) linearLayout.findViewById(R.id.list_video);

        cursor.moveToPosition(position);

        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String url = cursor.getString(cursor.getColumnIndex("path"));
        String videoURL = cursor.getString(cursor.getColumnIndex("video"));

        contentView.setText(content);
        timeView.setText(time);
        imageView.setImageBitmap(getImage(url, 200, 200));
        videoView.setImageBitmap(getVideoImage(videoURL,200,200,
                MediaStore.Images.Thumbnails.MICRO_KIND));
        return linearLayout;
    }

    public Bitmap getImage(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public Bitmap getVideoImage(String uri, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}