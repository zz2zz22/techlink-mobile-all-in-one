package com.example.techlinkmobileallinone.model;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

public class ButtonDataClass {
    private String dataTitle;
    private boolean dataPermission;
    private String dataDept;
    private int dataImage;
    private Class dataActivity;
    private String userCode;
    private String userName;
    public String getDataTitle() {
        return dataTitle;
    }
    public boolean getDataPermission() {
        return dataPermission;
    }
    public String getDataDept() {
        return dataDept;
    }
    public int getDataImage() {
        return dataImage;
    }
    public Class getDataActivity() {
        return dataActivity;
    }
    public String getUserCode() {
        return userCode;
    }
    public String getUserName() {return userName;}
    public ButtonDataClass(String dataTitle, boolean dataPermission, String dataDept, int dataImage, Class dataActivity, String userCode, String userName) {
        this.dataTitle = dataTitle;
        this.dataPermission = dataPermission;
        this.dataDept = dataDept;
        this.dataImage = dataImage;
        this.dataActivity = dataActivity;
        this.userCode = userCode;
        this.userName = userName;
    }
}
