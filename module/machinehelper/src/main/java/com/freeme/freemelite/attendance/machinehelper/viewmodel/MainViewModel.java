package com.freeme.freemelite.attendance.machinehelper.viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.freeme.freemelite.attendance.machinehelper.Config;
import com.freeme.freemelite.attendance.router.util.PreferencesUtil;

public class MainViewModel extends ViewModel {
    public MutableLiveData<Boolean> mCheckInDeviceNetworkConnectedLiveData = new MutableLiveData<>();

    public void bindLifecyclerObserver(Context context, LifecycleOwner lifecycleOwner) {
        new MainLifecyclerObserverImpl(context, lifecycleOwner);
    }

    class MainLifecyclerObserverImpl implements LifecycleObserver {
        private Context mContext;

        public MainLifecyclerObserverImpl(Context context, LifecycleOwner lifecycleOwner) {
            lifecycleOwner.getLifecycle().addObserver(this);
            mContext = context.getApplicationContext();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void getCheckInDeviceNetworkState() {
            mCheckInDeviceNetworkConnectedLiveData.setValue(PreferencesUtil.getBoolean(mContext, Config.CheckInDeviceState.CHECK_IN_DEVICE_NETWORK_CONNECTED_KEY));
        }
    }
}
