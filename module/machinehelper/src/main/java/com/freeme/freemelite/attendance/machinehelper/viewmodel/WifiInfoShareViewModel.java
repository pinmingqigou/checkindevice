package com.freeme.freemelite.attendance.machinehelper.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.adapter.WifiSsidListAdapter;
import com.freeme.freemelite.attendance.machinehelper.model.WifiScanResultWrapper;
import com.freeme.freemelite.attendance.router.util.BuildUtil;
import com.freeme.freemelite.attendance.router.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class WifiInfoShareViewModel extends ViewModel {
    private static final String TAG = "WifiInfoShareViewModel";
    //0代表网络输入框
    public static final int EDIT_TEXT_NETWORK = 0;
    //1代表密码输入框
    public static final int EDIT_TEXT_PW = 1;

    public MutableLiveData<WifiScanResultWrapper> mWifiScanResultLiveData = new MutableLiveData();

    public MutableLiveData<List<String>> mSsidListLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> mWifiListItemClickLiveData = new MutableLiveData<>();

    public WifiReceiver bindWifiReceiver(LifecycleOwner lifecycleOwner, Application context) {
        return new WifiReceiver(lifecycleOwner, context);
    }

    public WifiSsidListAdapter bindViewAdapter(LifecycleOwner lifecycleOwner) {
        return new WifiSsidListAdapter(this, lifecycleOwner);
    }

    public TextWatcher getTextWatcher(int type) {
        return new TextWatcherImpl(type);
    }

    public void dealWithScanResult(WifiScanResultWrapper wifiScanResultWrapper) {
        List<ScanResult> scanResults = wifiScanResultWrapper.scanResults;
        List<String> ssidList = new ArrayList<>();
        for (int i = 0; i < scanResults.size(); i++) {
            String ssid = scanResults.get(i).SSID;
            if (!TextUtils.isEmpty(ssid) && !ssidList.contains(ssid)) {
                ssidList.add(ssid);
            }
        }
        if (!ssidList.isEmpty()) {
            mSsidListLiveData.setValue(ssidList);
        }
    }

    public class WifiReceiver extends BroadcastReceiver implements LifecycleObserver {
        private Application mContext;
        private WifiManager mWifiManager;

        public WifiReceiver(LifecycleOwner lifecycleOwner, Application context) {
            this.mContext = context;
            lifecycleOwner.getLifecycle().addObserver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> scanResults = getWifiManager().getScanResults();
                WifiScanResultWrapper wifiScanResultWrapper = mWifiScanResultLiveData.getValue();
                if (wifiScanResultWrapper == null) {
                    wifiScanResultWrapper = new WifiScanResultWrapper();
                }
                wifiScanResultWrapper.scanResults = scanResults;
                mWifiScanResultLiveData.setValue(wifiScanResultWrapper);
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void registerReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            mContext.registerReceiver(this, intentFilter);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void unRegisterReceiver() {
            mContext.unregisterReceiver(this);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void getConnectedWifi() {
            if (isGpsEnable()) {
                String ssid = getWifiManager().getConnectionInfo().getSSID();
                if (!TextUtils.isEmpty(ssid)) {
                    WifiScanResultWrapper wifiScanResultWrapper = new WifiScanResultWrapper();
                    wifiScanResultWrapper.setSsidSelected(ssid);
                    mWifiScanResultLiveData.setValue(wifiScanResultWrapper);
                }
            } else {
                ToastUtil.show(mContext, "请至设置中打开位置服务开关");
            }

        }

        //@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void scanWifiResult(Activity activity) {
            if (BuildUtil.ATLEAST_MARSHMALLOW && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Config.PermissionRequest.LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                if (isGpsEnable()) {
                    getWifiManager().startScan();
                } else {
                    ToastUtil.show(mContext, "请至设置中打开位置服务开关");
                }
            }
        }

        private WifiManager getWifiManager() {
            if (mWifiManager == null) {
                mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            }
            return mWifiManager;
        }

        private boolean isGpsEnable() {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
    }

    public class TextWatcherImpl implements TextWatcher {
        private int type;

        public TextWatcherImpl(int type) {
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>afterTextChanged");
            try {
                if (type == EDIT_TEXT_PW) {
                    WifiScanResultWrapper wifiScanResultWrapper = mWifiScanResultLiveData.getValue();
                    String ssidSelected = wifiScanResultWrapper.getSsidSelected();
                    String pw = s.toString().trim();
                    if (!TextUtils.isEmpty(ssidSelected)) {
                        wifiScanResultWrapper.pw = pw;
                    }
                    mWifiScanResultLiveData.setValue(wifiScanResultWrapper);
                }
            } catch (Exception e) {
                Log.e(TAG, ">>>>>>>>>>>>handle Edittext text change error:" + e);
            }

        }
    }

}
