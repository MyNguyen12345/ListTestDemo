package com.example.listdemotest.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.listdemotest.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao  {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void  insertUser(List<UserEntity> userEntity);

    @Query("select *from user_table")
    List<UserEntity> getUserEntity();
}
