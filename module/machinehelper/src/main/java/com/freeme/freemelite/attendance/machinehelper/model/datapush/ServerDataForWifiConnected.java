package com.freeme.freemelite.attendance.machinehelper.model.datapush;

import com.freeme.freemelite.attendance.router.datapush.model.BaseDataPushRequestModel;

public class ServerDataForWifiConnected extends BaseDataPushRequestModel {
    public boolean wifiConnected;

    public String serverClientId;

    @Override
    public String toString() {
        return "ServerDataForWifiConnected{" +
                "wifiConnected=" + wifiConnected +
                ", serverClientId='" + serverClientId + '\'' +
                '}';
    }
}
