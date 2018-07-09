package com.freeme.freemelite.attendance.machinehelper.viewmodel;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.dao.FaceProviderPartner;
import com.freeme.freemelite.attendance.machinehelper.impl.OnPaFacePassDetectListenerImpl;
import com.freeme.freemelite.attendance.machinehelper.model.datapush.FaceForCheckInDevice;
import com.freeme.freemelite.attendance.machinehelper.utils.BitmapUtil;
import com.freeme.freemelite.attendance.pingan.cameraview.callback.PreviewCallback;
import com.freeme.freemelite.attendance.pingan.cameraview.surfaceview.CameraSurfaceHelper;
import com.freeme.freemelite.attendance.router.AsyncHandler;
import com.freeme.freemelite.attendance.router.datapush.DataPushSelector;
import com.freeme.freemelite.attendance.router.datapush.GlobalConfig;
import com.freeme.freemelite.attendance.router.datapush.model.BaseDataPushRequestModel;
import com.freeme.freemelite.attendance.router.facerecognition.FacePassManagerSelector;
import com.freeme.freemelite.attendance.router.facerecognition.model.CameraConfig;
import com.freeme.freemelite.attendance.router.util.BuildUtil;
import com.freeme.freemelite.attendance.router.util.PreferencesUtil;
import com.freeme.freemelite.attendance.router.util.StringUtil;
import com.freeme.freemelite.attendance.router.util.encrypt.MD5Util;
import com.google.gson.Gson;
import com.pingan.ai.face.result.PaFacePassFrame;
import com.pingan.ai.face.result.PaFrameFaceDetectResult;

import java.util.List;

public class SpecifiedEmployeeFaceRegistrationViewModel extends ViewModel implements PreviewCallback {

    public MutableLiveData<PaFrameFaceDetectResult> mFaceDetectResultLiveData = new MutableLiveData<>();

    public MutableLiveData<Integer> mFaceStateLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> mHasCametaPermissionLiveData = new MutableLiveData<>();

    private CameraSurfaceHelper mCameraSurfaceHelper;
    private int mEmployeeId;

    public String handleIntent(Intent intent) {
        mEmployeeId = intent.getIntExtra(Config.IntentConfig.INTENT_SPECIFIED_EMPLOYEE_ID_KEY, -1);
        return intent.getStringExtra(Config.IntentConfig.INTENT_SPECIFIED_EMPLOYEE_NAME_KEY);
    }

    public CameraSurfaceHelper bindCameraSurfaceHelper(Context context) {
        if (mCameraSurfaceHelper == null) {
            mCameraSurfaceHelper = new CameraSurfaceHelper(context);
        }
        return mCameraSurfaceHelper;
    }

    public LifecycleObserverImpl bindLifecycleObserver(LifecycleOwner lifecycleOwner, Application application) {
        return new LifecycleObserverImpl(lifecycleOwner, application);
    }

    public void pushFaceInfoToCheckInDevice(Context context, String name, float[] faceFeature) {
        BaseDataPushRequestModel baseDataPushRequestModel = new BaseDataPushRequestModel();
        baseDataPushRequestModel.clientId = PreferencesUtil.getString(context, GlobalConfig.SharepreFerenceConfig.CHECK_IN_DEVICE_CLIENT_ID_KEY);
        String json = new Gson().toJson(new FaceForCheckInDevice(name, faceFeature));
        String encypt = MD5Util.encypt(json);
        baseDataPushRequestModel.title = encypt;
        List<String> messages = StringUtil.getStrList(json, 500);
        for (String message : messages) {
            baseDataPushRequestModel.message = message + ":" + messages.indexOf(message);
            DataPushSelector.getInstance().pushSingleData(baseDataPushRequestModel);
        }
    }

    public void updateSpecifiedEmployeeFaceInfo(final Context context, final PaFrameFaceDetectResult paFrameFaceDetectResult) {
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                PaFacePassFrame facePassFrame = paFrameFaceDetectResult.facePassFrame;
                Bitmap bitmap = BitmapUtil.getBitmap(facePassFrame.frame, facePassFrame.frmaeWidth, facePassFrame.frameHeight, facePassFrame.frameOri);
                FaceProviderPartner.updateSpecifiedFace(context, mEmployeeId, bitmap);
            }
        });
    }

    @Override
    public void onPreviewFrame(Application application, byte[] frameData, int mWidth, int mHeight, int mCameraDisplayOrientation, int mCameraMode) {
        CameraConfig cameraConfig = new CameraConfig();
        cameraConfig.cameraWidth = mWidth;
        cameraConfig.cameraHeight = mHeight;
        cameraConfig.orientation = mCameraDisplayOrientation;
        cameraConfig.cameraMode = mCameraMode;
        FacePassManagerSelector.getInstance().startFrameDetect(frameData, cameraConfig);
        FacePassManagerSelector.getInstance().setOnFacePassListener(new OnPaFacePassDetectListenerImpl(application, this));
    }

    public class LifecycleObserverImpl implements LifecycleObserver {
        private Application mContext;

        private LifecycleObserverImpl(LifecycleOwner lifecycleOwner, Application application) {
            lifecycleOwner.getLifecycle().addObserver(this);
            mContext = application;
        }

        void releaseFrameDetect() {
            FacePassManagerSelector.getInstance().stopFrameDetect();
            FacePassManagerSelector.getInstance().releaseFrameDetect();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void surfaceHelperOnResume() {
            if (BuildUtil.ATLEAST_MARSHMALLOW && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission();
            } else {
                mHasCametaPermissionLiveData.setValue(true);
                if (mCameraSurfaceHelper != null) {
                    mCameraSurfaceHelper.openCamera();
                    mCameraSurfaceHelper.startPreview();
                    mCameraSurfaceHelper.setPreviewCallback(SpecifiedEmployeeFaceRegistrationViewModel.this);
                }
            }
        }

        private void requestCameraPermission() {
            mHasCametaPermissionLiveData.setValue(false);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void onStop() {
            releaseFrameDetect();
            if (mCameraSurfaceHelper != null) {
                mCameraSurfaceHelper.stopPreview();
                mCameraSurfaceHelper.relase();
            }
        }
    }
}
