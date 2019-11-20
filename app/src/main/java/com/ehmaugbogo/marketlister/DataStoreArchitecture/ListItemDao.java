package com.ehmaugbogo.marketlister.DataStoreArchitecture;

import com.ehmaugbogo.marketlister.model.ListItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ListItemDao {

    @Insert
    Long insertListItem(ListItem listItem);

    @Update
    void updateListItem(ListItem listItem);

    @Delete
    void deleteListItem(ListItem listItem);

    @Query("DELETE FROM listitem WHERE holder_id=:holder_id")
    void deleteAllListItem(long holder_id);

    @Query("SELECT * FROM listitem WHERE holder_id=:holder_id ORDER BY updated_at DESC")
    LiveData<List<ListItem>> getItemsWithHolderId(long holder_id);
}
