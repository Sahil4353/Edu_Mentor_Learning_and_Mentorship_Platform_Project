package com.example.edumentorlearningandmentorshipplatformproject.models;

import java.io.Serializable;

public class EnrolledCourse implements Serializable {
    private String title;
    private String instructor;
    private int progress;
    private int hoursRemaining;
    private int imageResId;

    public EnrolledCourse(String title, String instructor, int progress, int hoursRemaining, int imageResId) {
        this.title = title;
        this.instructor = instructor;
        this.progress = progress;
        this.hoursRemaining = hoursRemaining;
        this.imageResId = imageResId;
    }


    public String getTitle() {
        return title;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getProgress() {
        return progress;
    }

    public int getHoursRemaining() {
        return hoursRemaining;
    }

    public int getImageResId() {
        return imageResId;
    }
}
