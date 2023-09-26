package com.example.techlinkmobileallinone.utils;

public class ButtonDataClass {
    private String dataTitle;
    private boolean dataPermission;
    private String dataDept;
    private int dataImage;
    private Class dataActivity;
    private Boolean isLocked;
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
    public boolean getIsLocked(){return isLocked;}
    public String getUserCode() {
        return userCode;
    }
    public String getUserName() {return userName;}
    public ButtonDataClass(String dataTitle, boolean dataPermission, String dataDept, int dataImage, Class dataActivity, String userCode, String userName, boolean isLocked) {
        this.dataTitle = dataTitle;
        this.dataPermission = dataPermission;
        this.dataDept = dataDept;
        this.dataImage = dataImage;
        this.dataActivity = dataActivity;
        this.userCode = userCode;
        this.userName = userName;
        this.isLocked = isLocked;
    }
}
