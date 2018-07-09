package com.freeme.freemelite.attendance.router.facerecognition.interfaces;

import android.app.Application;

import com.freeme.freemelite.attendance.router.facerecognition.model.CameraConfig;


public interface FaceManagerWrapper<L,Q> {
    //注册
    void init(Application application);

    //开始人脸检测
    void startFrameDetect(L l, CameraConfig cameraConfig);

    //停止人脸检测
    void stopFrameDetect();

    //对比人脸
    boolean compareFace(float[] face1,float[] face2);

    //设置人脸检测回调
    void setOnFacePassListener(Q q);

    //释放人脸检测资源
    void releaseFrameDetect();

    //SDK释放资源
    void release();
}
