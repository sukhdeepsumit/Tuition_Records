package com.app_devs.tuitionrecords.StudentActivity.Request;

public class TeacherShowModel {

    String name, about;
    String myUri;

    public TeacherShowModel() {
    }

    public TeacherShowModel(String name, String about, String myUri) {
        this.name = name;
        this.about = about;
        this.myUri = myUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
