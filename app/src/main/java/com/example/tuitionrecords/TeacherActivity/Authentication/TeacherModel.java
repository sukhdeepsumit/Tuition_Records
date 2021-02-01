package com.example.tuitionrecords.TeacherActivity.Authentication;

public class TeacherModel {
    String name, email, contact,gender, city, state, content, standard, about, myUri, id;

    public TeacherModel() {
    }

    public TeacherModel(String name, String email, String contact, String gender,String city, String state, String content, String standard, String about, String myUri, String id) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.content = content;
        this.standard = standard;
        this.about = about;
        this.myUri = myUri;
        this.id = id;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getMyUri() {
        return myUri;
    }

    public void setMyUri(String myUri) {
        this.myUri = myUri;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
