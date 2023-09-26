package com.example.techlinkmobileallinone.utils;

public class Department {
    private String deptID;
    private String deptName;

    public Department(String deptID, String deptName) {
        this.deptID = deptID;
        this.deptName = deptName;
    }
    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    //to display object as a string in spinner
    @Override
    public String toString() {
        return deptName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Department){
            Department c = (Department )obj;
            if(c.getDeptName().equals(deptName) && c.getDeptID()==deptID ) return true;
        }
        return false;
    }
}
