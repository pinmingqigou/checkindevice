package com.freeme.freemelite.attendance.router.datapush.subject;

import com.freeme.freemelite.attendance.router.datapush.interfaces.IDataReceiveObserver;
import com.freeme.freemelite.attendance.router.datapush.model.BaseReceivedModel;

public class DataReceiveSubject extends BaseSubject<IDataReceiveObserver> {
    private static class Instance {
        private static DataReceiveSubject dataReceiveSubject = new DataReceiveSubject();
    }

    private DataReceiveSubject() {

    }

    public static DataReceiveSubject getInstance() {
        return DataReceiveSubject.Instance.dataReceiveSubject;
    }

    public void receiveData(BaseReceivedModel baseReceivedModel) {
        if (weakReference != null) {
            IDataReceiveObserver iDataReceiveObserver = weakReference.get();
            if (iDataReceiveObserver != null) {
                iDataReceiveObserver.onDataReceived(baseReceivedModel);
            }
        }
    }
}
