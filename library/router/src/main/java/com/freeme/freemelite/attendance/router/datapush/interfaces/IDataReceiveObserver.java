package com.freeme.freemelite.attendance.router.datapush.interfaces;

import com.freeme.freemelite.attendance.router.datapush.model.BaseReceivedModel;

public interface IDataReceiveObserver {
    void onDataReceived(BaseReceivedModel baseReceivedModel);
}
