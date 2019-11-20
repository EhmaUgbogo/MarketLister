package com.ehmaugbogo.marketlister.DataStoreArchitecture;

import com.ehmaugbogo.marketlister.model.ListItemHolder;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ListItemHolderDao {

    @Insert
    Long insertHolder(ListItemHolder holder);

    @Update
    void updateHolder(ListItemHolder holder);

    @Delete
    void deleteHolder(ListItemHolder holder);

    @Query("DELETE FROM ListItemHolder")
    void deleteAllHolder();

    @Query("SELECT * FROM ListItemHolder ORDER BY updated_at DESC")
    LiveData<List<ListItemHolder>> getAllHolder();
}
