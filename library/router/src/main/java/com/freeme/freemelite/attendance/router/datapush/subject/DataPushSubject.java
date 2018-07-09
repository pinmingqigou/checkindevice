package com.freeme.freemelite.attendance.router.datapush.subject;

import com.freeme.freemelite.attendance.router.datapush.interfaces.IDataPushObserver;

public class DataPushSubject extends BaseSubject<IDataPushObserver> {
    private static class Instance {
        private static DataPushSubject dataPushSubject = new DataPushSubject();
    }

    private DataPushSubject() {
    }

    public static DataPushSubject getInstance() {
        return Instance.dataPushSubject;
    }

    public void beginDataPush() {
        if (weakReference != null) {
            IDataPushObserver iDataPushObserver = weakReference.get();
            if (iDataPushObserver != null) {
                iDataPushObserver.onDataPushBegin();
            }
        }
    }

    public void dataPushSuccessful() {
        if (weakReference != null) {
            IDataPushObserver iDataPushObserver = weakReference.get();
            if (iDataPushObserver != null) {
                iDataPushObserver.onDataPushSuccessful();
            }
        }
    }

    public void dataPushFailed() {
        if (weakReference != null) {
            IDataPushObserver iDataPushObserver = weakReference.get();
            if (iDataPushObserver != null) {
                iDataPushObserver.onDataPushFailed();
            }
        }
    }
}
