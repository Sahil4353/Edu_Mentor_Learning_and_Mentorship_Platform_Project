package com.example.edumentorlearningandmentorshipplatformproject.models;

public class Course {
    private String title;
    private String category;
    private int imageResId;

    public Course() {
    }

    public Course(String title, String category, int imageResId) {
        this.title = title;
        this.category = category;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
