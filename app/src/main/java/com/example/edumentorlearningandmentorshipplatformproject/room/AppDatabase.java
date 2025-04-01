package com.example.edumentorlearningandmentorshipplatformproject.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.edumentorlearningandmentorshipplatformproject.models.User;
import com.example.edumentorlearningandmentorshipplatformproject.models.EnrolledCourse;

@Database(entities = {User.class, EnrolledCourse.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract EnrolledCourseDao enrolledCourseDao();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "edumentor_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
