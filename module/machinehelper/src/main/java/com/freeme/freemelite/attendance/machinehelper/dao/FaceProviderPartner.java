package com.freeme.freemelite.attendance.machinehelper.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.freeme.freemelite.attendance.machinehelper.model.RegistrationFaceBean;
import com.freeme.freemelite.attendance.machinehelper.utils.BitmapUtil;
import com.freeme.freemelite.attendance.router.facerecognition.FacePassManagerSelector;

import java.util.ArrayList;
import java.util.List;

public class FaceProviderPartner {
    private static final String TAG = "FaceProviderPartner";


    public static Uri insertRegistrationFace(Context context, String userName, Bitmap photoBitmap, float[] faceFeature) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceColumns.COLUMN_USER_NAME, userName);
        if (photoBitmap != null) {
            contentValues.put(FaceColumns.COLUMN_PHOTO, BitmapUtil.bitmap2Bytes(photoBitmap));
        }
        if (faceFeature != null) {
            StringBuilder feature = new StringBuilder();
            for (float v : faceFeature) {
                feature.append(v).append("/");
            }
            contentValues.put(FaceColumns.COLUMN_FACE_FEATURE, feature.toString());
        }

        contentValues.put(FaceColumns.COLUMN_RECENT_FLAG, 1);
        return context.getContentResolver().insert(FaceColumns.FACE_INFO_TABLE_URL, contentValues);
    }

    //将数据库中所有最近的标记置为０，即清除最近标记
    public static int removeAllRecentFlag(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceColumns.COLUMN_RECENT_FLAG, 0);
        return contentResolver.update(FaceColumns.FACE_INFO_TABLE_URL, contentValues, FaceColumns.COLUMN_RECENT_FLAG + "=?", new String[]{"1"});
    }

    public static int deleteSpecifiedFace(Context context, String faceFeature) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(FaceColumns.FACE_INFO_TABLE_URL, FaceColumns.COLUMN_FACE_FEATURE + "=?", new String[]{faceFeature});
    }

    public static int deleteSpecifiedFace(Context context, int id) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.delete(FaceColumns.FACE_INFO_TABLE_URL, FaceColumns._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static RegistrationFaceBean queryRecentFace(Context context) {
        RegistrationFaceBean faceBean = new RegistrationFaceBean();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(FaceColumns.FACE_INFO_TABLE_URL, null, FaceColumns.COLUMN_RECENT_FLAG + "=?", new String[]{"1"}, null);
        if (cursor != null && cursor.moveToFirst()) {
            faceBean.name = cursor.getString(cursor.getColumnIndex(FaceColumns.COLUMN_USER_NAME));
            byte[] photoData = cursor.getBlob(cursor.getColumnIndex(FaceColumns.COLUMN_PHOTO));
            faceBean.photo = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
            faceBean.feature = cursor.getString(cursor.getColumnIndex(FaceColumns.COLUMN_FACE_FEATURE));
            faceBean.id = cursor.getInt(cursor.getColumnIndex(FaceColumns._ID));
            cursor.close();
        }
        return faceBean;
    }

    public static List<RegistrationFaceBean> queryAllRegistrationFaces(Context context) {
        List<RegistrationFaceBean> mFaceBeans = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(FaceColumns.FACE_INFO_TABLE_URL, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RegistrationFaceBean faceBean = new RegistrationFaceBean();
                faceBean.name = cursor.getString(cursor.getColumnIndex(FaceColumns.COLUMN_USER_NAME));
                byte[] photoData = cursor.getBlob(cursor.getColumnIndex(FaceColumns.COLUMN_PHOTO));
                if (photoData != null) {
                    faceBean.photo = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
                }
                faceBean.feature = cursor.getString(cursor.getColumnIndex(FaceColumns.COLUMN_FACE_FEATURE));
                faceBean.id = cursor.getInt(cursor.getColumnIndex(FaceColumns._ID));
                mFaceBeans.add(faceBean);
            }
            cursor.close();
        }
        return mFaceBeans;
    }

    public static boolean isFaceRegistered(Context context, float[] faceFeature) {
        List<RegistrationFaceBean> registrationFaceBeans = queryAllRegistrationFaces(context);
        for (RegistrationFaceBean registrationFaceBean : registrationFaceBeans) {
            if (registrationFaceBean.feature == null) continue;
            String[] strings = registrationFaceBean.feature.split("/");
            float[] feature = new float[strings.length];
            for (int i = 0; i < strings.length; i++) {
                feature[i] = Float.valueOf(strings[i]);
            }
            boolean compareFace = FacePassManagerSelector.getInstance().compareFace(faceFeature, feature);
            if (compareFace) {
                return true;
            }
        }
        return false;
    }

    public static void updateSpecifiedFace(Context context, int id, Bitmap bitmap) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FaceColumns.COLUMN_PHOTO, BitmapUtil.bitmap2Bytes(bitmap));
        contentResolver.update(FaceColumns.FACE_INFO_TABLE_URL, contentValues, FaceColumns._ID + "=?", new String[]{String.valueOf(id)});
    }
}
