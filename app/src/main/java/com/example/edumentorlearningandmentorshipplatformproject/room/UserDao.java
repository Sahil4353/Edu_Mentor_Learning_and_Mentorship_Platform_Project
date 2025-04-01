package com.example.edumentorlearningandmentorshipplatformproject.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser(com.example.edumentorlearningandmentorshipplatformproject.room.UserEntity user);

    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getUserById(String userId);

    @Query("SELECT * FROM users")
    List<UserEntity> getAllUsers();

    @Update
    void updateUser(UserEntity user);

    @Delete
    void deleteUser(UserEntity user);
}
