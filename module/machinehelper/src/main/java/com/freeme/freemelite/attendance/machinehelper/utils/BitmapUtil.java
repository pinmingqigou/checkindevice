package com.freeme.freemelite.attendance.machinehelper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhanqiang545 on 18/4/3.
 */

public class BitmapUtil {

    public final static int CV_ROTATE_90_CLOCKWISE = 0; //Rotate 90 degrees clockwise
    public final static int CV_ROTATE_180 = 1; //Rotate 180 degrees clockwise
    public final static int CV_ROTATE_90_COUNTERCLOCKWISE = 2; //Rotate 270 degrees clockwise
    public final static int CV_ROTATE_360 = 3; //Rotate 270 degrees clockwise

    //获取原bitmap图片
    public static Bitmap getBitmap(byte[] frame, int imageWidth, int imageHeight, int ori) {
        Bitmap bitmap = null;
        try {
            YuvImage image = new YuvImage(frame, ImageFormat.NV21, imageWidth, imageHeight, null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, imageWidth, imageHeight), 100, stream);
            bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
            stream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        float rotate;
        if (ori == CV_ROTATE_90_CLOCKWISE) {
            rotate = 90;
        } else if (ori == CV_ROTATE_90_COUNTERCLOCKWISE) {
            rotate = 270;
        } else if (ori == CV_ROTATE_180) {
            rotate = 180;
        } else {
            rotate = 360;
        }
        if (bitmap != null) {
            bitmap = rotateBitmap(bitmap, rotate);
        }
        return bitmap;
    }

    //获取人脸抠图
    public static Bitmap getSmallBitmap(byte[] frame, int x, int y, int w, int h, int width, int height, int ori, Bitmap bitmap) {

        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        // 获得成功的图片
        try {
            if (ori == CV_ROTATE_90_CLOCKWISE ||
                    ori == CV_ROTATE_90_COUNTERCLOCKWISE) {
                width = height;
                height = width;
            }
            RectF rect;
            if (x >= 0 && y >= 0 && w >= 0 && h >= 0) {
                rect = new RectF(x, y, w + x, h + y);
                if (frame.length != 0) {
                    // 扣图扩大1.2 / 0314加float ratio / int nHeight,int nWidth,
                    int nw, nh, xn, yn, x0, y0;
                    float ratio = 1.45f;
                    int x1 = (int) rect.left;
                    int y1 = (int) rect.top;
                    int w1 = (int) rect.width() + x1;
                    int h1 = (int) rect.height() + y1;

                    x0 = (int) (x1 - w1 * (ratio - 1) * 0.5);
                    xn = (int) (x0 + w1 * ratio);
                    if (x0 < 0) {
                        x0 = 0;
                    }
                    if (xn > width - 1) {
                        xn = width - 1;
                    }
                    nw = xn - x0 + 1;

                    y0 = (int) (y1 - h1 * (ratio - 1));
                    yn = (int) (y0 + h1 * (1 + (ratio - 1) * 1.5));
                    if (y0 < 0) {
                        y0 = 0;
                    }
                    if (yn > height - 1) {
                        yn = height - 1;
                    }
                    nh = yn - y0 + 1;
                    return Bitmap.createBitmap(bitmap, (int) x0, (int) y0, (int) nw, (int) nh);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Bitmap getCropBitmap(Bitmap bitmap,int x,int y,int w,int h){

        int x0 = x+w/2;
        int y0 = y+h/2;

        int w1 = (int)(w/2*1.45);
        int h1 = (int)(h/2*1.6);

        int newX = x0-w1;
        int newY = y0-h1;
        int newW = w1*2;
        int newH = h1+(int)(h/2*1.2);

        if(newX<0){
            newX=0;
        }

        if((newX+newW)>bitmap.getWidth()){
            newX = bitmap.getWidth()-newW;
        }

        if(newY<0){
            newY = 0;
        }

        if((newY+newH)>bitmap.getHeight()){
            newY = bitmap.getHeight()-newH;
        }

        return Bitmap.createBitmap(bitmap, newX, newY, newW, newH);
    }


    //旋转bitmap
    private static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }


    public static void saveBitmapToSD(Bitmap bm, String filePath, String filename) {
        File dir = new File(filePath);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, filename);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Bitmap getBitmapFromPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        return bitmap;
    }

}
