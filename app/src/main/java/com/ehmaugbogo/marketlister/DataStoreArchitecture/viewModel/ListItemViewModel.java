package com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel;

import android.app.Application;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.Repository;
import com.ehmaugbogo.marketlister.model.EditHistory;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ListItemViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> adapterPosition =new MutableLiveData<>();
    private final Repository repository;

    public ListItemViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    //Getters for ListItem
    public Long insertListItem(ListItem item){
        return repository.insertListItem(item);
    }

    public void updateListItem(ListItem item){
        repository.updateListItem(item);
    }

    public void deleteListItem(ListItem item){
        repository.deleteListItem(item);
    }

    public void deleteAllListItem(long Holder_id){
        repository.deleteAllListItem(Holder_id);
    }

    public LiveData<List<ListItem>> getItemsWithHolderId(long Holder_id) {
        return repository.getItemsWithHolderId(Holder_id);
    }

    //Getters for Holder
    public Long insertHolder(ListItemHolder holder){
        return repository.insertHolder(holder);
    }

    public void updateHolder(ListItemHolder holder){
        repository.updateHolder(holder);
    }

    public void deleteHolder(ListItemHolder holder){
        repository.deleteHolder(holder);
    }

    public void deleteAllHolder(){
        repository.deleteAllHolder();
    }

    public LiveData<List<ListItemHolder>> getAllHolders() {
        return repository.getAllHolder();
    }


    //Getters for EditHistory
    public Long insertEditHistory(EditHistory editHistory){
        return repository.insertEditHistory(editHistory);
    }

    public void updateEditHistory(EditHistory editHistory){
        repository.updateEditHistory(editHistory);
    }

    public void deleteEditHistory(EditHistory editHistory){
        repository.deleteEditHistory(editHistory);
    }

    public void deleteAllEditHistory(long holder_id){
        repository.deleteAllEditHistory(holder_id);
    }

    public LiveData<List<EditHistory>> getEditHistoriesWithHolderId(long holder_id) {
        return repository.getEditHistoriesWithHolderId(holder_id);
    }



    //BaseHolderFragment Adapter position Listener
    public LiveData<Integer> getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(Integer position) {
        adapterPosition.setValue(position);
    }
}
