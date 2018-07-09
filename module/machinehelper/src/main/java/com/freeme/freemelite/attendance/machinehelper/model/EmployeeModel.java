package com.freeme.freemelite.attendance.machinehelper.model;

public class EmployeeModel {

    private String name;
    private String feature;
    private String letters;//显示拼音的首字母
    private int id;

    public EmployeeModel(String name, String feature, String letters, int id) {
        this.name = name;
        this.feature = feature;
        this.letters = letters;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
