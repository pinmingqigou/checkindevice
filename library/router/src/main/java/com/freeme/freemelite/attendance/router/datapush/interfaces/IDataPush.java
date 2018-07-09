package com.freeme.freemelite.attendance.router.datapush.interfaces;

import android.app.Application;

import com.freeme.freemelite.attendance.router.datapush.model.BaseDataPushRequestModel;

public interface IDataPush {
    void init(Application context);

    void pushSingleData(BaseDataPushRequestModel baseRequestModel);

    void pushGroupData();

    void release();
}
