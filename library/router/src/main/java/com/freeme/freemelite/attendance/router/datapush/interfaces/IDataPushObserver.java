package com.freeme.freemelite.attendance.router.datapush.interfaces;

public interface IDataPushObserver {
    void onDataPushBegin();

    void onDataPushSuccessful();

    void onDataPushFailed();
}
