package com.example.jsMobileProject2019;

public class UserDTO {
    private String email;
    private String name;
    private float grade;
    private int toeic;
    public UserDTO(String email, String name, float grade, int toeic){
        this.email = email;
        this.name = name;
        this.grade = grade;
        this.toeic = toeic;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getToeic() {
        return toeic;
    }

    public void setToeic(int toeic) {
        this.toeic = toeic;
    }
}
