package com.freeme.freemelite.attendance.machinehelper.model;

import android.net.wifi.ScanResult;

import java.io.Serializable;
import java.util.List;

public class WifiScanResultWrapper implements Serializable {
    public List<ScanResult> scanResults;

    private String ssidSelected;

    public String pw;

    public String getSsidSelected() {
        if (ssidSelected != null && ssidSelected.contains("\"")) {
            StringBuffer stringBuffer = new StringBuffer(ssidSelected);
            stringBuffer.deleteCharAt(0);
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            return stringBuffer.toString();
        } else {
            return ssidSelected;
        }
    }

    public void setSsidSelected(String ssid) {
        ssidSelected = ssid;
    }
}
