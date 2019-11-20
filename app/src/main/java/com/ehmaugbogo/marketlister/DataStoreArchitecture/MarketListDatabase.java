package com.ehmaugbogo.marketlister.DataStoreArchitecture;

import android.content.Context;

import com.ehmaugbogo.marketlister.model.EditHistory;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.model.User;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {User.class, ListItem.class, ListItemHolder.class, EditHistory.class},version = 1,exportSchema = false)
public abstract class MarketListDatabase extends RoomDatabase {
    private static MarketListDatabase INSTANCE;

    public static synchronized MarketListDatabase getInstance(Context context){
        if(INSTANCE==null){
            return Room.databaseBuilder(context, MarketListDatabase.class,"List_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract UserDao userDao();
    public abstract EditHistoryDao historyDao();
    public abstract ListItemDao listItemDao();
    public abstract ListItemHolderDao listHolderDao();
}

