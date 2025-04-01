package com.example.edumentorlearningandmentorshipplatformproject.models;

import java.io.Serializable;

public class CombinedEnrolledCourse implements Serializable {
    private EnrolledCourse pythonCourse;
    private EnrolledCourse flutterCourse;

    public CombinedEnrolledCourse(EnrolledCourse pythonCourse, EnrolledCourse flutterCourse) {
        this.pythonCourse = pythonCourse;
        this.flutterCourse = flutterCourse;
    }

    public EnrolledCourse getPythonCourse() {
        return pythonCourse;
    }

    public EnrolledCourse getFlutterCourse() {
        return flutterCourse;
    }
}
