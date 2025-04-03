package com.example.edumentorlearningandmentorshipplatformproject.models;

public class RecommendedCourse {
    private String title;
    private String subtitle;
    private float rating;
    private String price;
    private int imageRes;

    public RecommendedCourse(String title, String subtitle, float rating, String price, int imageRes) {
        this.title = title;
        this.subtitle = subtitle;
        this.rating = rating;
        this.price = price;
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public float getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public int getImageRes() {
        return imageRes;
    }
}
