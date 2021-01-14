package com.example.tuitionrecords;

public class ScheduleModel {
    String timing, subject, subjectStatus;

    public ScheduleModel(String timing, String subject, String subjectStatus) {
        this.timing = timing;
        this.subject = subject;
        this.subjectStatus = subjectStatus;
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

    public String getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(String subjectStatus) {
        this.subjectStatus = subjectStatus;
    }
}
