package com.example.edumentorlearningandmentorshipplatformproject.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.edumentorlearningandmentorshipplatformproject.models.EnrolledCourse;
import java.util.List;

@Dao
public interface EnrolledCourseDao {

    // Get all enrolled courses as LiveData for real-time updates
    @Query("SELECT * FROM enrolled_courses")
    LiveData<List<EnrolledCourse>> getAllEnrolledCourses();

    // Get all enrolled courses for a specific user (by userId)
    @Query("SELECT * FROM enrolled_courses WHERE userId = :userId")
    LiveData<List<EnrolledCourse>> getEnrolledCoursesByUserId(String userId);

    // Get course details by the course's auto-generated ID
    @Query("SELECT * FROM enrolled_courses WHERE id = :id")
    EnrolledCourse getEnrolledCourseById(int id);

    // Get course details by course title (name)
    @Query("SELECT * FROM enrolled_courses WHERE title = :name")
    EnrolledCourse getEnrolledCourseByName(String name);

    // Get all courses taught by a specific instructor as LiveData
    @Query("SELECT * FROM enrolled_courses WHERE instructor = :instructor")
    LiveData<List<EnrolledCourse>> getEnrolledCoursesByInstructor(String instructor);

    // Insert a new enrolled course
    @Insert
    long insertEnrolledCourse(EnrolledCourse enrolledCourse);

    // Update an existing enrolled course
    @Update
    int updateEnrolledCourse(EnrolledCourse enrolledCourse);

    // Delete an enrolled course
    @Delete
    int deleteEnrolledCourse(EnrolledCourse enrolledCourse);
}
