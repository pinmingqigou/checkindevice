package com.freeme.freemelite.attendance.router.datapush;

import android.app.Application;
import android.util.Log;

import com.freeme.freemelite.attendance.router.datapush.defaultimpl.DefaultDataPushImpl;
import com.freeme.freemelite.attendance.router.datapush.interfaces.IDataPush;

public class DataPushSelector {
    public static final String TAG = "DataPushSelector";
    private static IDataPush sInstance;
    private static Object sInstanceLock = new Object();

    public static void initalize(Application context) {
        getInstance().init(context);
    }

    public static void onTerminate() {
        getInstance().release();
    }

    public static IDataPush getInstance() {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                Class clazz;
                try {
                    clazz = Class.forName("com.freeme.freemelite.attendance.datapush.GeTuiImpl");
                    sInstance = (IDataPush) clazz.newInstance();
                } catch (Exception e) {
                    Log.e(TAG, ">>>>>>>>>>>>>get IDataPush instance fail ", e);
                }

                if (sInstance == null) {
                    sInstance = new DefaultDataPushImpl();
                }
            }
            return sInstance;
        }
    }
}
