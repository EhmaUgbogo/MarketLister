package com.ehmaugbogo.marketlister.DataStoreArchitecture;

import com.ehmaugbogo.marketlister.model.EditHistory;
import com.ehmaugbogo.marketlister.model.ListItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EditHistoryDao {

    @Insert
    Long insertEditHistory(EditHistory editHistory);

    @Update
    void updateEditHistory(EditHistory editHistory);

    @Delete
    void deleteEditHistory(EditHistory editHistory);

    @Query("DELETE FROM EditHistory WHERE holderId=:holder_id")
    void deleteAllEditHistory(long holder_id);

    @Query("SELECT * FROM EditHistory WHERE holderId=:holder_id")
    LiveData<List<EditHistory>> getEditHistoriesWithHolderId(long holder_id);
}
