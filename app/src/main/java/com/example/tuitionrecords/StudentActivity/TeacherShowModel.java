package com.example.tuitionrecords.StudentActivity;

public class TeacherShowModel {

    String name, email, content, city, state,  standard;
    String myUri;

    public TeacherShowModel() {
    }

    public TeacherShowModel(String name, String email, String content, String city, String state, String standard, String myUri) {
        this.name = name;
        this.email = email;
        this.content = content;
        this.city = city;
        this.state = state;
        this.standard = standard;
        this.myUri = myUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getMyUri() {
        return myUri;
    }

    public void setMyUri(String myUri) {
        this.myUri = myUri;
    }
}
