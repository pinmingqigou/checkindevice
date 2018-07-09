package com.freeme.freemelite.attendance.router.facerecognition;

import android.app.Application;
import android.util.Log;

import com.freeme.freemelite.attendance.router.facerecognition.defaultimpl.DefaultFacePassManagerWrapper;
import com.freeme.freemelite.attendance.router.facerecognition.interfaces.FaceManagerWrapper;

public class FacePassManagerSelector {
    public static final String TAG = "FacePassManagerSelector";
    private static FaceManagerWrapper sInstance;
    private static Object sInstanceLock = new Object();

    public static void initalize(Application context) {
        getInstance().init(context);
    }

    public static void onTerminate() {
        getInstance().release();
    }

    public static FaceManagerWrapper getInstance() {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                Class clazz;
                try {
                    clazz = Class.forName("com.freeme.freemelite.attendance.pingan.PaFacePassManagerWrapper");
                    sInstance = (FaceManagerWrapper) clazz.newInstance();
                } catch (Exception e) {
                    Log.e(TAG, "===========get FaceManagerWrapper instance fail ", e);
                }

                if (sInstance == null) {
                    sInstance = new DefaultFacePassManagerWrapper();
                }
            }
            return sInstance;
        }
    }
}
