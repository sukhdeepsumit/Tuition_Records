package com.app_devs.tuitionrecords.Notifications;

public class Data {
    private String title, user, body, sent;
    int icon;

    public Data() {
    }

    public Data(String user, int icon, String body, String title, String sent) {
        this.title = title;
        this.user = user;
        this.body = body;
        this.sent = sent;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
