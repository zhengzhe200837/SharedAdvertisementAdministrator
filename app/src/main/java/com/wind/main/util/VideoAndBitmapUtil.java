package com.wind.main.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

/**
 * Created by zhuyuqiang on 17-11-23.
 */

public class VideoAndBitmapUtil {

    private final static int ICON_IMAGE_WIDTH = 150;
    private final static int ICON_IMAGE_HEIGHT = 150;
    private final static int IMAGE_WIDTH = 500;
    private final static int IMAGE_HEIGHT = 500;

    public static Bitmap getImageByVideoPath(String path){
        MediaMetadataRetriever r = new MediaMetadataRetriever();
        Bitmap result = null;
        try {
            r.setDataSource(path);
            result = r.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            r.release();
            return result;
        }
    }

    public static Bitmap getImageByVideoUri(Context context, Uri uri){
        MediaMetadataRetriever r = new MediaMetadataRetriever();
        Bitmap result = null;
        try {
            r.setDataSource(context,uri);
            result = r.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            r.release();
            return result;
        }
    }

    public static Bitmap getIconImageByBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) ICON_IMAGE_WIDTH) / width;
        float scaleHeight = ((float) ICON_IMAGE_HEIGHT) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap getBitmapByPath(String path){
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,op);
        if(op.outWidth>IMAGE_WIDTH || op.outHeight>IMAGE_HEIGHT){
            op.inSampleSize = Math.max(op.outWidth/IMAGE_WIDTH,op.outHeight/IMAGE_HEIGHT);
        }
        op.inJustDecodeBounds = false;
        Bitmap result = BitmapFactory.decodeFile(path,op);
        return result;
    }

}
