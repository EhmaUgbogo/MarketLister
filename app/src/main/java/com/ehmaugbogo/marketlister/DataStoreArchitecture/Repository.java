package com.ehmaugbogo.marketlister.DataStoreArchitecture;

import android.content.Context;

import com.ehmaugbogo.marketlister.model.EditHistory;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.model.User;

import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {
    private UserDao userDao;
    private final EditHistoryDao editHistoryDao;
    private ListItemDao listItemDao;
    private final ListItemHolderDao holderDao;


    public Repository(Context context) {
        MarketListDatabase marketListDatabase = MarketListDatabase.getInstance(context);
        userDao = marketListDatabase.userDao();
        editHistoryDao = marketListDatabase.historyDao();
        listItemDao = marketListDatabase.listItemDao();
        holderDao = marketListDatabase.listHolderDao();
    }

    //Getters for User
    public Long insertUser(User user){
        return userDao.insertUser(user);
    }

    public void updateUser(User user){
        userDao.updateUser(user);
    }

    public void deleteUser(User user){
        userDao.deleteUser(user);
    }

    public List<User> getUser(String uid){
        return userDao.getUser(uid);
    }


    //Getters for ListItem
    public Long insertListItem(ListItem item){
        return listItemDao.insertListItem(item);
    }

    public void updateListItem(ListItem item){
        listItemDao.updateListItem(item);
    }

    public void deleteListItem(ListItem item){
        listItemDao.deleteListItem(item);
    }

    public void deleteAllListItem(long userId){
        listItemDao.deleteAllListItem(userId);
    }

    public LiveData<List<ListItem>> getItemsWithHolderId(long holder_id) {
        return listItemDao.getItemsWithHolderId(holder_id);
    }

    //Getters for Holder
    public Long insertHolder(ListItemHolder holder){
        return holderDao.insertHolder(holder);
    }

    public void updateHolder(ListItemHolder holder){
        holderDao.updateHolder(holder);
    }

    public void deleteHolder(ListItemHolder holder){
        holderDao.deleteHolder(holder);
    }

    public void deleteAllHolder(){
        holderDao.deleteAllHolder();
    }

    public LiveData<List<ListItemHolder>> getAllHolder() {
        return holderDao.getAllHolder();
    }

    //Getters for EditHistory
    public Long insertEditHistory(EditHistory editHistory){
        return editHistoryDao.insertEditHistory(editHistory);
    }

    public void updateEditHistory(EditHistory editHistory){
        editHistoryDao.updateEditHistory(editHistory);
    }

    public void deleteEditHistory(EditHistory editHistory){
        editHistoryDao.deleteEditHistory(editHistory);
    }

    public void deleteAllEditHistory(long holder_id){
        editHistoryDao.deleteAllEditHistory(holder_id);
    }

    public LiveData<List<EditHistory>> getEditHistoriesWithHolderId(long holder_id) {
        return editHistoryDao.getEditHistoriesWithHolderId(holder_id);
    }
}
