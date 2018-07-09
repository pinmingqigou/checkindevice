package com.freeme.freemelite.attendance.datapush.receive.service;

import android.content.Context;
import android.util.Log;

import com.freeme.freemelite.attendance.router.datapush.model.BaseReceivedModel;
import com.freeme.freemelite.attendance.router.datapush.subject.DataReceiveSubject;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

public class PushDataReceiver extends GTIntentService {
    private static final String TAG = "PushDataReceiver";

    @Override
    public void onReceiveServicePid(Context context, int i) {
        Log.e(TAG, ">>>>>>>>>>>>>>>onReceiveServicePid:" + i);
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        Log.e(TAG, ">>>>>>>>>>>>>>>>onReceiveClientId:" + s);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        Log.e(TAG, ">>>>>>>>>>>>>>>>onReceiveMessageData:" + gtTransmitMessage.toString());
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        Log.e(TAG, ">>>>>>>>>>>>>>>onReceiveOnlineState:" + b);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.e(TAG, ">>>>>>>>>>>>>>>>>onReceiveCommandResult:" + gtCmdMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>onNotificationMessageArrived:" + gtNotificationMessage.getTitle() + "/" + gtNotificationMessage.getContent());
        BaseReceivedModel receiveModel = new BaseReceivedModel();
        receiveModel.content = gtNotificationMessage.getContent();
        DataReceiveSubject.getInstance().receiveData(receiveModel);
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        Log.e(TAG, ">>>>>>>>>>>>>>>>>onNotificationMessageClicked:" + gtNotificationMessage);
    }
}
