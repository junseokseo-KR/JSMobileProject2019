package com.example.jsMobileProject2019;

import android.util.Log;

import java.io.Serializable;

public class UserData implements Serializable {
    private String email,name,college,major, opic, toeicSpeaking, sex;
    private long award, license,intern, overseas,toeic, volun;
    private double grade;

    
    public UserData(String email, String name, String college, String major, String opic, String toeicSpeaking, double grade, long toeic, long award, long license, long intern, long overseas, long volun, String sex){
        this.email = email;
        this.name = name;
        this.college = college;
        this.major = major;
        this.opic = opic;
        this.toeicSpeaking = toeicSpeaking;
        this.grade = grade;
        this.toeic = toeic;
        this.award = award;
        this.license = license;
        this.intern = intern;
        this.overseas = overseas;
        this.volun = volun;
        this.sex = sex;
        Log.i("사용자 등록", intern+" : "+volun+" : "+award+" : "+license);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getGrade() {
        return grade;
    }

    public long getToeic() {
        return toeic;
    }

    public String getCollege() {
        return college;
    }

    public String getMajor() {
        return major;
    }

    public String getOpic() {
        return opic;
    }

    public String getToeicSpeaking() {
        return toeicSpeaking;
    }

    public long getAward() {
        return award;
    }

    public long getLicense() {
        return license;
    }

    public long getIntern() {
        return intern;
    }

    public long getOverseas() {
        return overseas;
    }

    public long getVolun() { return volun;  }

    public String getSex() {
        return sex;
    }
}
