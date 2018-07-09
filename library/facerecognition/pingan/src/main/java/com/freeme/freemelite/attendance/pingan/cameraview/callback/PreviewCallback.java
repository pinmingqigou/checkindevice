package com.freeme.freemelite.attendance.pingan.cameraview.callback;

import android.app.Application;

/**
 * Created by zhanqiang545 on 18/1/10.
 */

public interface PreviewCallback{

  void onPreviewFrame(Application application,byte[] frameData, int mWidth, int mHeight, int mCameraDisplayOrientation, int mCameraMode);

}
