package com.freeme.freemelite.attendance.router.datapush.defaultimpl;

import android.app.Application;
import android.util.Log;

import com.freeme.freemelite.attendance.router.datapush.interfaces.IDataPush;
import com.freeme.freemelite.attendance.router.datapush.model.BaseDataPushRequestModel;

public class DefaultDataPushImpl implements IDataPush {
    private static final String TAG = "DefaultDataPushImpl";

    @Override
    public void init(Application context) {
        Log.e(TAG, ">>>>>>>>>>>>>>>>init");
    }

    @Override
    public void pushSingleData(BaseDataPushRequestModel baseRequestModel) {

    }

    @Override
    public void pushGroupData() {

    }


    @Override
    public void release() {

    }
}
