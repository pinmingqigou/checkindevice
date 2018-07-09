package com.freeme.freemelite.attendance.router.datapush.subject;

import java.lang.ref.WeakReference;

public class BaseSubject<T> {
    public WeakReference<T> weakReference;

    public void registerObserver(T t) {
        weakReference = new WeakReference<T>(t);
    }

    public void unregisterObserver() {
        weakReference.clear();
    }
}
