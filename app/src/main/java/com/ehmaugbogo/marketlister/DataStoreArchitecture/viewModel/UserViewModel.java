package com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel;

import android.app.Application;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.Repository;
import com.ehmaugbogo.marketlister.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class UserViewModel extends AndroidViewModel {

    private final Repository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public Long insertUser(User user){
        return repository.insertUser(user);
    }

    public void updateUser(User user){
        repository.updateUser(user);
    }

    public void deleteUser(User user){
        repository.deleteUser(user);
    }

    public List<User> getUser(String uid){
        return repository.getUser(uid);
    }

}
