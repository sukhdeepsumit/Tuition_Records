package com.example.tuitionrecords.StudentActivity.Authentication;

public class StudentModel {
    String name,  myEmail, myContact, myStandard, myCity, myState, myDescription, myGender, myUri;

    public StudentModel() {
    }

    public StudentModel(String name, String myEmail, String myGender, String myContact, String myStandard, String myCity, String myState, String myDescription, String myUri) {

        this.name = name;
        this.myEmail = myEmail;
        this.myContact = myContact;
        this.myStandard = myStandard;
        this.myCity = myCity;
        this.myState = myState;
        this.myDescription = myDescription;
        this.myGender = myGender;
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

    public String getMyContact() {
        return myContact;
    }

    public void setMyContact(String myContact) {
        this.myContact = myContact;
    }

    public String getMyStandard() {
        return myStandard;
    }

    public void setMyStandard(String myStandard) {
        this.myStandard = myStandard;
    }

    public String getMyCity() {
        return myCity;
    }

    public void setMyCity(String myCity) {
        this.myCity = myCity;
    }

    public String getMyState() {
        return myState;
    }

    public void setMyState(String myState) {
        this.myState = myState;
    }

    public String getMyDescription() {
        return myDescription;
    }

    public void setMyDescription(String myDescription) {
        this.myDescription = myDescription;
    }

    public String getMyGender() {
        return myGender;
    }

    public void setMyGender(String myGender) {
        this.myGender = myGender;
    }

    public String getMyUri() {
        return myUri;
    }

    public void setMyUri(String myUri) {
        this.myUri = myUri;
    }


}
