package com.example.edumentorlearningandmentorshipplatformproject.models;

public class UpcomingSession {
    private String userName;
    private String date;
    private String time;
    private String imageUrl;

    public UpcomingSession() {
    }

    public UpcomingSession(String userName, String date, String time, String imageUrl) {
        this.userName = userName;
        this.date = date;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public String getUserName() { return userName; }
    public String getDate()     { return date; }
    public String getTime()     { return time; }
    public String getImageUrl() { return imageUrl; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setDate(String date)         { this.date = date; }
    public void setTime(String time)         { this.time = time; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
