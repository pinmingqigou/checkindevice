package com.freeme.freemelite.attendance.machinehelper.viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.machinehelper.model.PhoneModel;
import com.freeme.freemelite.attendance.machinehelper.model.datapush.ServerDataForWifiConnected;
import com.freeme.freemelite.attendance.router.datapush.GlobalConfig;
import com.freeme.freemelite.attendance.router.datapush.interfaces.IDataReceiveObserver;
import com.freeme.freemelite.attendance.router.datapush.model.BaseReceivedModel;
import com.freeme.freemelite.attendance.router.datapush.subject.DataReceiveSubject;
import com.freeme.freemelite.attendance.router.util.PreferencesUtil;
import com.google.gson.Gson;

public class QRCodeViewModel extends ViewModel {
    private static final String TAG = "QRCodeViewModel";
    private static final String CLIENT_ID_KEY = "client_id_key";

    public MutableLiveData<ServerDataForWifiConnected> mServerDataForWifiConnectedLiveData = new MutableLiveData<>();

    public void changeCheckInDeviceNetworkState(Context context, boolean networkConnected) {
        PreferencesUtil.putBoolean(context, Config.CheckInDeviceState.CHECK_IN_DEVICE_NETWORK_CONNECTED_KEY, networkConnected);
    }

    public String transformPhoneModel(Context context, String wifi, String pw) {
        PhoneModel phoneModel = new PhoneModel();
        phoneModel.wifi = wifi;
        phoneModel.pw = pw;
        phoneModel.clientId = PreferencesUtil.getString(context.getApplicationContext(), CLIENT_ID_KEY);
        Gson gson = new Gson();
        return gson.toJson(phoneModel);
    }

    public void bindLifecyclerObserverImpl(Context context, LifecycleOwner lifecycleOwner) {
        new LifecyclerObserverImpl(context, lifecycleOwner);
    }

    class LifecyclerObserverImpl implements LifecycleObserver, IDataReceiveObserver {
        private Context mContext;

        public LifecyclerObserverImpl(Context context, LifecycleOwner lifecycleOwner) {
            mContext = context.getApplicationContext();
            lifecycleOwner.getLifecycle().addObserver(this);
        }


        @Override
        public void onDataReceived(BaseReceivedModel baseReceivedModel) {
            String content = baseReceivedModel.content;
            Gson gson = new Gson();
            ServerDataForWifiConnected serverDataForWifiConnected = gson.fromJson(content, ServerDataForWifiConnected.class);
            if (serverDataForWifiConnected.serverClientId != null) {
                PreferencesUtil.putString(mContext, GlobalConfig.SharepreFerenceConfig.CHECK_IN_DEVICE_CLIENT_ID_KEY, serverDataForWifiConnected.serverClientId);
            }
            Log.e(TAG, ">>>>>>>>>>>>>>onDataReceived:" + serverDataForWifiConnected.toString());
            mServerDataForWifiConnectedLiveData.postValue(serverDataForWifiConnected);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void registerDataReceiveObserver() {
            DataReceiveSubject.getInstance().registerObserver(this);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void unregisterDataReceiveObserver() {
            DataReceiveSubject.getInstance().unregisterObserver();
        }
    }
}
