package com.freeme.freemelite.attendance.machinehelper.dao;

import android.database.ContentObserver;
import android.os.Handler;

import com.freeme.freemelite.attendance.machinehelper.viewmodel.BaseViewModel;

public class FaceProviderChangeObserver extends ContentObserver {
    private static final String TAG = "FaceContentObserver";
    private static final Handler HANDLER = new Handler();
    private BaseViewModel mViewModel;

    /**
     * Creates a content observer.
     */
    public FaceProviderChangeObserver(BaseViewModel viewModel) {
        super(HANDLER);
        mViewModel = viewModel;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        mViewModel.contentObserverOnchange();
    }
}
