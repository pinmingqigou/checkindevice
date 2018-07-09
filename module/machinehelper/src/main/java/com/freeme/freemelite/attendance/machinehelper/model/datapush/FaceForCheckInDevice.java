package com.freeme.freemelite.attendance.machinehelper.model.datapush;

public class FaceForCheckInDevice {
    public String name;
    public float[] faceFeature;

    public FaceForCheckInDevice(String name, float[] faceFeature) {
        this.name = name;
        this.faceFeature = faceFeature;
    }
}
