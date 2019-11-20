package com.ehmaugbogo.marketlister.views.ui.profile;


import com.ehmaugbogo.marketlister.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private static final String TAG = "PostViewModel";

    private MutableLiveData<String> mText;
    private FirebaseFirestore db;
    private MutableLiveData<User> user=new MutableLiveData<>();

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("find a new friend...");
        db = FirebaseFirestore.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }


    public void updateUser(User updatedUser) {
        user.setValue(updatedUser);
    }

    public LiveData<User> getUpdateUser() {
        return user;
    }
}