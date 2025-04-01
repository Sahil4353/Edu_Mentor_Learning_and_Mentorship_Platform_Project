package com.example.edumentorlearningandmentorshipplatformproject.models;

public class NotificationItem {
    private String studentName;
    private long timestamp;
    private String studentPhotoUrl;
    private String message;

    public NotificationItem() {}

    public NotificationItem(String studentName, long timestamp, String studentPhotoUrl, String message) {
        this.studentName = studentName;
        this.timestamp = timestamp;
        this.studentPhotoUrl = studentPhotoUrl;
        this.message = message;
    }

    public String getStudentName() { return studentName; }
    public long getTimestamp()     { return timestamp; }
    public String getStudentPhotoUrl() { return studentPhotoUrl; }
    public String getMessage()     { return message; }

    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setTimestamp(long timestamp)       { this.timestamp = timestamp; }
    public void setStudentPhotoUrl(String studentPhotoUrl) { this.studentPhotoUrl = studentPhotoUrl; }
    public void setMessage(String message)         { this.message = message; }
}
