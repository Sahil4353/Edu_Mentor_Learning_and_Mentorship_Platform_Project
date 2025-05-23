package com.example.edumentorlearningandmentorshipplatformproject.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "enrolled_courses")
public class EnrolledCourse implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String instructor;
    private int progress;
    private int hoursRemaining;
    private int imageResId;
    private String userId;

    public EnrolledCourse(String title, String instructor, int progress, int hoursRemaining, int imageResId, String userId) {
        this.title = title;
        this.instructor = instructor;
        this.progress = progress;
        this.hoursRemaining = hoursRemaining;
        this.imageResId = imageResId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUserId() {
        return userId;
    }
}
