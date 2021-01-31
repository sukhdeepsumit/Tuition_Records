package com.example.tuitionrecords;

public class UsersShowModel {
    String name, myUri;
    public UsersShowModel()
    {

    }

    public UsersShowModel(String name, String myUri) {
        this.name = name;
        this.myUri = myUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyUri() {
        return myUri;
    }

    public void setMyUri(String myUri) {
        this.myUri = myUri;
    }
}
