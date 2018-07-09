package com.freeme.freemelite.attendance;

import android.app.Application;

import com.freeme.freemelite.attendance.router.datapush.DataPushSelector;
import com.freeme.freemelite.attendance.router.facerecognition.FacePassManagerSelector;


public class AttendanceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //人脸识别注册
        FacePassManagerSelector.initalize(this);
        //推送注册
        DataPushSelector.initalize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //人脸识别注销
        FacePassManagerSelector.onTerminate();
        //推送注销
        DataPushSelector.onTerminate();
    }
}
