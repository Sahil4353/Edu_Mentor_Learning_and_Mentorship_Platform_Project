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

    @Query("SELECT * FROM enrolled_courses")
    LiveData<List<EnrolledCourse>> getAllEnrolledCourses();

    @Query("SELECT * FROM enrolled_courses WHERE userId = :userId")
    LiveData<List<EnrolledCourse>> getEnrolledCoursesByUserId(String userId);

    @Query("SELECT * FROM enrolled_courses WHERE id = :id")
    EnrolledCourse getEnrolledCourseById(int id);

    @Query("SELECT * FROM enrolled_courses WHERE title = :name")
    EnrolledCourse getEnrolledCourseByName(String name);

    @Query("SELECT * FROM enrolled_courses WHERE instructor = :instructor")
    LiveData<List<EnrolledCourse>> getEnrolledCoursesByInstructor(String instructor);

    @Insert
    long insertEnrolledCourse(EnrolledCourse enrolledCourse);

    @Update
    int updateEnrolledCourse(EnrolledCourse enrolledCourse);
    @Delete
    int deleteEnrolledCourse(EnrolledCourse enrolledCourse);
}
