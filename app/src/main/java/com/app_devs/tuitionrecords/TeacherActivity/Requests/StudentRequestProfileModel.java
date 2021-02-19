package com.app_devs.tuitionrecords.TeacherActivity.Requests;

public class StudentRequestProfileModel {
    String name, myDescription, myUri;

    public StudentRequestProfileModel() {
    }

    public StudentRequestProfileModel(String name, String myDescription, String myUri) {
        this.name = name;
        this.myDescription = myDescription;
        this.myUri = myUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyDescription() {
        return myDescription;
    }

    public void setMyDescription(String myDescription) {
        this.myDescription = myDescription;
    }

    public String getMyUri() {
        return myUri;
    }

    public void setMyUri(String myUri) {
        this.myUri = myUri;
    }
}
