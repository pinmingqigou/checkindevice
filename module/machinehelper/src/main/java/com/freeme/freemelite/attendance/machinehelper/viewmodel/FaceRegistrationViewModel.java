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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

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

public class FaceRegistrationViewModel extends ViewModel implements PreviewCallback {
    private static final String TAG = "FaceRegisterViewModel";
    private CameraSurfaceHelper mCameraSurfaceHelper;

    public MutableLiveData<PaFrameFaceDetectResult> mFrameFaceLiveData = new MutableLiveData<>();

    public MutableLiveData<String> mUserNameLiveData = new MutableLiveData<>();

    public MutableLiveData<Integer> mFaceStateLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> mHasCameraPermissionLiveData = new MutableLiveData<>();

    public FaceRegistrationObserver bindFaceRegistrationObserver(LifecycleOwner lifecycleOwner, Application application) {
        return new FaceRegistrationObserver(lifecycleOwner, application);
    }

    public CameraSurfaceHelper bindCameraSurfaceHelper(Context context) {
        if (mCameraSurfaceHelper == null) {
            mCameraSurfaceHelper = new CameraSurfaceHelper(context);
        }
        return mCameraSurfaceHelper;
    }

    public TextWatcher bindTextWatcher() {
        return new TextWatcherImp();
    }

    public Bitmap HandleFaceScanData(PaFrameFaceDetectResult frameFaceDetectResult) {
        PaFacePassFrame facePassFrame = frameFaceDetectResult.facePassFrame;
        Bitmap bitmap = BitmapUtil.getBitmap(facePassFrame.frame, facePassFrame.frmaeWidth, facePassFrame.frameHeight, facePassFrame.frameOri);
        return bitmap;
    }

    public void saveFaceRegistrationInfo(final Context context, final String userName, final Bitmap photoBitmap, final float[] faceFeature) {
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!FaceProviderPartner.isFaceRegistered(context, faceFeature)) {
                    FaceProviderPartner.removeAllRecentFlag(context);
                    FaceProviderPartner.insertRegistrationFace(context, userName, photoBitmap, faceFeature);
                }
            }
        });
    }

    /*
    * 由于个推一次只能推送1024个字符，切割需要上传的json,分段上传,在每段的message后拼接index,用户签到机识别并拼接完整的message;
    * title用完整message的MD5值进行标记，便于签到机识别连续接收到推送是否为同一人脸信息的分割
    * */
    public void pushFaceInfoToCheckInDevice(Context context, String name, float[] faceFeature) {
        BaseDataPushRequestModel baseDataPushRequestModel = new BaseDataPushRequestModel();
        baseDataPushRequestModel.clientId = PreferencesUtil.getString(context, GlobalConfig.SharepreFerenceConfig.CHECK_IN_DEVICE_CLIENT_ID_KEY);
        String json = new Gson().toJson(new FaceForCheckInDevice(name, faceFeature));
        String encypt = MD5Util.encypt(json);
        baseDataPushRequestModel.title = encypt;
        List<String> messages = StringUtil.getStrList(json, 500);
        for (String message : messages) {
            Log.e(TAG, ">>>>>>>>>>>>>>>pushFaceInfoToCheckInDevice:" + message);
            baseDataPushRequestModel.message = message + ":" + messages.indexOf(message);
            DataPushSelector.getInstance().pushSingleData(baseDataPushRequestModel);
        }
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

    public class FaceRegistrationObserver implements LifecycleObserver {
        private Application mContext;

        private FaceRegistrationObserver(LifecycleOwner lifecycleOwner, Application application) {
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
                mHasCameraPermissionLiveData.setValue(true);
                if (mCameraSurfaceHelper != null) {
                    mCameraSurfaceHelper.openCamera();
                    mCameraSurfaceHelper.startPreview();
                    mCameraSurfaceHelper.setPreviewCallback(FaceRegistrationViewModel.this);
                }
            }
        }

        private void requestCameraPermission() {
            mHasCameraPermissionLiveData.setValue(false);
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

    class TextWatcherImp implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String userName = s.toString().trim();
            mUserNameLiveData.setValue(userName);
        }
    }
}
