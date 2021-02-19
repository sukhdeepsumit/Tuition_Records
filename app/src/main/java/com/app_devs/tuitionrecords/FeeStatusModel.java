package com.app_devs.tuitionrecords;

public class FeeStatusModel {

    String name, myEmail, myUri ;

    public FeeStatusModel()
    {

    }


    public FeeStatusModel(String name, String email, String myUri) {
        this.name = name;
        this.myEmail = email;
        this.myUri = myUri;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    public String getMyUri() {
        return myUri;
    }

    public void setMyUri(String myUri) {
        this.myUri = myUri;
    }
}
