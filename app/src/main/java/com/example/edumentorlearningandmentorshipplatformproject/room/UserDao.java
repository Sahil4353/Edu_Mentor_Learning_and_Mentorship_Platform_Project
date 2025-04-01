package com.example.edumentorlearningandmentorshipplatformproject.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import com.example.edumentorlearningandmentorshipplatformproject.models.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(String userId);

    @Insert
    void insertUser(User user);
}
