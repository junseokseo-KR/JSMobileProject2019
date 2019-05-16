package com.example.jsMobileProject2019;

import java.io.Serializable;

public class UserData implements Serializable {
    private String email,name,college,major, opic, toeicSpeaking;
    private long award, license,intern, overseas,toeic;
    private double grade;
    
    public UserData(String email, String name, String college, String major, String opic, String toeicSpeaking, double grade, long toeic, long award, long license, long intern, long overseas){
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
}
