package com.freeme.freemelite.attendance.pingan;

import android.app.Application;
import android.util.Log;

import com.freeme.freemelite.attendance.router.facerecognition.FacePassManagerSelector;
import com.freeme.freemelite.attendance.router.facerecognition.interfaces.FaceManagerWrapper;
import com.freeme.freemelite.attendance.router.facerecognition.model.CameraConfig;
import com.pingan.ai.face.common.PaFaceDetectConfig;
import com.pingan.ai.face.manager.PaFacePassManager;
import com.pingan.ai.face.manager.impl.OnPaFacePassDetectListener;

public class PaFacePassManagerWrapper implements FaceManagerWrapper<byte[], OnPaFacePassDetectListener> {

    private PaFacePassManager mInstance;

    @Override
    public void init(Application application) {
        Log.e(FacePassManagerSelector.TAG, "=====================PaFacePassManagerWrapper init");
        mInstance = PaFacePassManager.getInstance();
        mInstance.init(application);
        PaFaceDetectConfig paFaceDetectConfig = new PaFaceDetectConfig();
        paFaceDetectConfig.livenessEnabled = true;
        mInstance.setFaceDetectConfig(paFaceDetectConfig);
    }

    @Override
    public void startFrameDetect(byte[] bytes, CameraConfig cameraConfig) {
        mInstance.startFrameDetect();
        mInstance.offerFrameBuffer(bytes, cameraConfig.cameraWidth, cameraConfig.cameraHeight, cameraConfig.orientation, cameraConfig.cameraMode);
    }

    @Override
    public void stopFrameDetect() {
        mInstance.stopFrameDetect();
        mInstance.stopOffer();
    }

    @Override
    public boolean compareFace(float[] face1, float[] face2) {
        return mInstance.compareFace(face1, face2).cmopareScore >= 0.6;
    }

    @Override
    public void setOnFacePassListener(OnPaFacePassDetectListener onPaFacePassDetectListener) {
        mInstance.setOnPaFacePassListener(onPaFacePassDetectListener);
    }

    @Override
    public void releaseFrameDetect() {
        mInstance.relase();
    }

    @Override
    public void release() {
        mInstance.deInit();
    }
}
