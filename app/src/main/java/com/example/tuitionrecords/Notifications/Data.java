package com.example.tuitionrecords.Notifications;

public class Data {
    private String title, notif;

    public Data() {
    }

    public Data(String title, String notif) {
        this.title = title;
        this.notif = notif;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }
}
