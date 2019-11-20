package com.ehmaugbogo.marketlister.DataStoreArchitecture;

import com.ehmaugbogo.marketlister.model.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    Long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user WHERE uid =:uid")
    List<User> getUser(String uid);
}
