package com.freeme.freemelite.attendance.machinehelper.impl;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.dao.FaceProviderPartner;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.FaceRegistrationViewModel;
import com.freeme.freemelite.attendance.machinehelper.viewmodel.SpecifiedEmployeeFaceRegistrationViewModel;
import com.pingan.ai.face.common.PaFacePassMessage;
import com.pingan.ai.face.manager.impl.OnPaFacePassDetectListener;
import com.pingan.ai.face.result.PaFrameFaceDetectResult;

public class OnPaFacePassDetectListenerImpl extends OnPaFacePassDetectListener {
    private static final String TAG = "PaFrameDetect";
    private ViewModel mViewModel;
    private Application mContext;

    public OnPaFacePassDetectListenerImpl(Application application, ViewModel viewModel) {
        this.mViewModel = viewModel;
        mContext = application;
    }

    @Override
    public void onFaceDetectResult(PaFrameFaceDetectResult result) {
        int message = result.message;
        switch (message) {
            case PaFacePassMessage.RESULT_OK:
                Log.e(TAG, "====================RESULT_OK");
                if (mViewModel instanceof FaceRegistrationViewModel) {
                    if (!FaceProviderPartner.isFaceRegistered(mContext, result.feature)) {
                        ((FaceRegistrationViewModel) mViewModel).mFaceStateLiveData.setValue(Config.FaceState.UNREGISTERED);
                        ((FaceRegistrationViewModel) mViewModel).mFrameFaceLiveData.setValue(result);
                    } else {
                        ((FaceRegistrationViewModel) mViewModel).mFaceStateLiveData.setValue(Config.FaceState.REGISTERED);
                    }
                } else if (mViewModel instanceof SpecifiedEmployeeFaceRegistrationViewModel) {
                    if (!FaceProviderPartner.isFaceRegistered(mContext, result.feature)) {
                        ((SpecifiedEmployeeFaceRegistrationViewModel) mViewModel).mFaceStateLiveData.setValue(Config.FaceState.UNREGISTERED);
                        ((SpecifiedEmployeeFaceRegistrationViewModel) mViewModel).mFaceDetectResultLiveData.setValue(result);
                    } else {
                        ((SpecifiedEmployeeFaceRegistrationViewModel) mViewModel).mFaceStateLiveData.setValue(Config.FaceState.REGISTERED);
                    }
                }
                break;
            case PaFacePassMessage.NO_FACE:
                Log.e(TAG, "====================NO_FACE");
                break;
            case PaFacePassMessage.HAS_FACE:
                Log.e(TAG, "====================HAS_FACE");
                break;
            case PaFacePassMessage.ILLEGAL_FACE:
                Log.e(TAG, "====================ILLEGAL_FACE");
                break;
            case PaFacePassMessage.NORMAL_FACE:
                Log.e(TAG, "====================NORMAL_FACE");
                break;
            case PaFacePassMessage.OUT_OF_MAX_FACE_NUM:
                Log.e(TAG, "====================OUT_OF_MAX_FACE_NUM");
                break;
            case PaFacePassMessage.ILLEGAL_ARGUMENT:
                Log.e(TAG, "====================ILLEGAL_ARGUMENT");
                break;
            case PaFacePassMessage.FACE_EXIST:
                Log.e(TAG, "====================FACE_EXIST");
                break;
            case PaFacePassMessage.FACE_NOT_EXIST:
                Log.e(TAG, "====================FACE_NOT_EXIST");
                break;
            case PaFacePassMessage.FILE_NOT_FOUND:
                Log.e(TAG, "====================FILE_NOT_FOUND");
                break;
            case PaFacePassMessage.ILLEGAL_FEATURE:
                Log.e(TAG, "====================ILLEGAL_FEATURE");
                break;
            case PaFacePassMessage.FACE_NOT_FOUND:
                Log.e(TAG, "====================FACE_NOT_FOUND");
                break;
            default:
                Log.e(TAG, "====================default");
                break;
        }
    }
}
