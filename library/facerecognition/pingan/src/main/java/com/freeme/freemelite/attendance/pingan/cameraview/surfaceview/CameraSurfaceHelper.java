package com.freeme.freemelite.attendance.pingan.cameraview.surfaceview;

import android.app.Application;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.freeme.freemelite.attendance.pingan.cameraview.callback.PreviewCallback;
import com.freeme.freemelite.attendance.pingan.cameraview.utils.CameraUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhanqiang545 on 18/1/9.
 */
@SuppressWarnings("deprecation")
public class CameraSurfaceHelper implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private Context mContext;
    private Camera mCamera;
    private Camera.Parameters mCameraParameters;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private int mWidth;
    private int mHeight;
    private int mCameraDisplayOrientation;
    private int mCameraMode = Camera.CameraInfo.CAMERA_FACING_FRONT;

    public CameraSurfaceHelper(Context context) {
        mContext = context;
    }

    public void initPreview(FrameLayout frameLayout, int cameraMode, int width, int height) {
        mCameraMode = cameraMode;
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        mSurfaceView = new SurfaceView(mContext);
        if (width == 0 || height == 0) {
            width = display.getWidth();
            height = display.getHeight();
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity = Gravity.CENTER;
        mSurfaceView.setLayoutParams(layoutParams);
        mHolder = mSurfaceView.getHolder();
        frameLayout.addView(mSurfaceView);
    }

    public void openCamera() {
        mHolder.addCallback(this);
//        if (mCamera != null) {
//            relaseCamera();
//        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraInfo.facing == mCameraMode) {
                mCamera = Camera.open(i);
                break;
            } else if (cameraCount == 1) {
                mCamera = Camera.open(i);
                mCameraMode = i;
            }
        }

        mCameraParameters = mCamera.getParameters();
        //获取支持的格式
        Camera.Size previewSize = CameraUtil.getPropPreviewSize(mCameraParameters.getSupportedPreviewSizes(), 640);
        mWidth = previewSize.width;
        mHeight = previewSize.height;

        //格式要求,安卓默认格式
        mCameraParameters.setPreviewFormat(ImageFormat.NV21);
        //建议预览尺寸为640*480，其它尺寸效果没有这么好
        mCameraParameters.setPreviewSize(mWidth, mHeight);
        List<String> focusModes = mCameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else {
            mCameraParameters.setFocusMode(focusModes.get(0));
        }
        mCameraParameters.setPreviewFrameRate(30);
        mCamera.setParameters(mCameraParameters);
    }

    public void startPreview() {
        if (mCamera != null) {
            try {
                mCameraDisplayOrientation = CameraUtil.getCameraDisplayOrientation(mContext, mCameraMode);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.setDisplayOrientation(mCameraDisplayOrientation);
                mCamera.setPreviewCallback(this);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    private void relaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void relase() {
        if (mContext != null) {
            mContext = null;
        }
        if (mPreviewCallback != null) {
            mPreviewCallback = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            stopPreview();
            startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
//        TODO:
        //relaseCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        try {
            mPreviewCallback.onPreviewFrame((Application) mContext.getApplicationContext(), data, mWidth, mHeight, mCameraDisplayOrientation, mCameraMode);
        } catch (Exception e) {
            Log.e("CameraSurfaceHelper", ">>>>>>onPreviewFrame error:" + e);
        }
    }

    private PreviewCallback mPreviewCallback;

    public void setPreviewCallback(PreviewCallback onFrameDataCallback) {
        mPreviewCallback = onFrameDataCallback;
    }
}
