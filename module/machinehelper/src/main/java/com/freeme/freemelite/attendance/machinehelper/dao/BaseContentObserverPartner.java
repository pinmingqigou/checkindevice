package com.freeme.freemelite.attendance.machinehelper.dao;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.freeme.freemelite.attendance.machinehelper.viewmodel.BaseViewModel;

import java.util.HashMap;
import java.util.Map;

public class BaseContentObserverPartner implements LifecycleObserver {
    private static final Map<LifecycleOwner, Boolean> PAIR = new HashMap<>();
    private Application mContext;
    private FaceProviderChangeObserver mObserver;
    private BaseViewModel mBaseViewModel;
    private LifecycleOwner mLifecyclerOwner;

    public BaseContentObserverPartner(BaseViewModel baseViewModel, LifecycleOwner lifecycleOwner, Application context) {
        lifecycleOwner.getLifecycle().addObserver(this);
        mLifecyclerOwner = lifecycleOwner;
        mContext = context;
        mBaseViewModel = baseViewModel;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void registerContentObserver() {
        if (PAIR.get(mLifecyclerOwner) == null || !PAIR.get(mLifecyclerOwner)) {
            if (mObserver == null) {
                mObserver = new FaceProviderChangeObserver(mBaseViewModel);
            }
            mContext.getContentResolver().registerContentObserver(FaceColumns.FACE_INFO_TABLE_URL, true, mObserver);
            PAIR.put(mLifecyclerOwner, true);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void unRegisterContentObserver() {
        if (mObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mObserver);
        }
    }

}
