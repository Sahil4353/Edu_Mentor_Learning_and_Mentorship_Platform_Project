package com.example.edumentorlearningandmentorshipplatformproject.models;

public class Payment {
    private String userId;
    private String mentorName;
    private String courseName;
    private String coursePrice;
    private long timestamp;
    private String sessionDate;
    private String sessionTime;

    public Payment() {
    }

    public Payment(String userId, String mentorName, String courseName, String coursePrice, long timestamp, String sessionDate, String sessionTime) {
        this.userId = userId;
        this.mentorName = mentorName;
        this.courseName = courseName;
        this.coursePrice = coursePrice;
        this.timestamp = timestamp;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }
}
