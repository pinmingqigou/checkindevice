package com.freeme.freemelite.attendance.router.facerecognition.defaultimpl;

import android.app.Application;
import android.util.Log;

import com.freeme.freemelite.attendance.router.facerecognition.FacePassManagerSelector;
import com.freeme.freemelite.attendance.router.facerecognition.interfaces.FaceManagerWrapper;
import com.freeme.freemelite.attendance.router.facerecognition.model.CameraConfig;


public class DefaultFacePassManagerWrapper implements FaceManagerWrapper {
    @Override
    public void init(Application application) {
        Log.e(FacePassManagerSelector.TAG,"=====================init:no FaceManagerWrapper implemention except DefaultFacePassManagerWrapper");
    }

    @Override
    public void startFrameDetect(Object o, CameraConfig cameraConfig) {

    }

    @Override
    public void stopFrameDetect() {

    }

    @Override
    public boolean compareFace(float[] face1,float[] face2) {
        return false;
    }

    @Override
    public void setOnFacePassListener(Object o) {

    }

    @Override
    public void releaseFrameDetect() {

    }

    @Override
    public void release() {

    }
}
