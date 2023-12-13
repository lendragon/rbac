package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import java.util.Date;

public class User {
    Integer ID;
    String FNo;
    String FName;
    Integer FDept;
    Integer FSex;
    Date FBirthDay;
    String FIdentityNum;
    String FPhoneNum;
    Date FEnterTime;

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", FNo='" + FNo + '\'' +
                ", FName='" + FName + '\'' +
                ", FDept=" + FDept +
                ", FSex=" + FSex +
                ", FBirthDay=" + FBirthDay +
                ", FIdentityNum='" + FIdentityNum + '\'' +
                ", FPhoneNum='" + FPhoneNum + '\'' +
                ", FEnterTime=" + FEnterTime +
                '}';
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getFNo() {
        return FNo;
    }

    public void setFNo(String FNo) {
        this.FNo = FNo;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public Integer getFDept() {
        return FDept;
    }

    public void setFDept(Integer FDept) {
        this.FDept = FDept;
    }

    public Integer getFSex() {
        return FSex;
    }

    public void setFSex(Integer FSex) {
        this.FSex = FSex;
    }

    public Date getFBirthDay() {
        return FBirthDay;
    }

    public void setFBirthDay(Date FBirthDay) {
        this.FBirthDay = FBirthDay;
    }

    public String getFIdentityNum() {
        return FIdentityNum;
    }

    public void setFIdentityNum(String FIdentityNum) {
        this.FIdentityNum = FIdentityNum;
    }

    public String getFPhoneNum() {
        return FPhoneNum;
    }

    public void setFPhoneNum(String FPhoneNum) {
        this.FPhoneNum = FPhoneNum;
    }

    public Date getFEnterTime() {
        return FEnterTime;
    }

    public void setFEnterTime(Date FEnterTime) {
        this.FEnterTime = FEnterTime;
    }

    public User(Integer ID, String FNo, String FName, Integer FDept, Integer FSex, Date FBirthDay, String FIdentityNum, String FPhoneNum, Date FEnterTime) {
        this.ID = ID;
        this.FNo = FNo;
        this.FName = FName;
        this.FDept = FDept;
        this.FSex = FSex;
        this.FBirthDay = FBirthDay;
        this.FIdentityNum = FIdentityNum;
        this.FPhoneNum = FPhoneNum;
        this.FEnterTime = FEnterTime;
    }

    public User() {
    }
}
