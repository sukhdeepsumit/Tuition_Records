package com.example.tuitionrecords.Schedule;

public class ScheduleModel {
    String timing, subject, batch, order;

    public ScheduleModel() {
    }

    public ScheduleModel(String timing, String subject, String batch, String order) {
        this.timing = timing;
        this.subject = subject;
        this.batch = batch;
        this.order = order;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
