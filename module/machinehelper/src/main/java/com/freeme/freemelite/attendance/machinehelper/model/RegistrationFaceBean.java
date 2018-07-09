package com.freeme.freemelite.attendance.machinehelper.model;

import android.graphics.Bitmap;

public class RegistrationFaceBean {
    public String name;
    public Bitmap photo;
    public String feature;
    public int id;

    @Override
    public String toString() {
        return "RegistrationFaceBean{" +
                "name='" + name + '\'' +
                ", photo=" + photo +
                ", feature='" + feature + '\'' +
                ", id=" + id +
                '}';
    }
}
