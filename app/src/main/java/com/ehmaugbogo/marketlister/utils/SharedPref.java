package com.ehmaugbogo.marketlister.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {
    private static final String ID_KEY="com.ehmaugbogo.marketlister_ID_KEY";

    private static SharedPref INSTANCE;

    public static synchronized SharedPref getINSTANCE(Context context) {
        if(INSTANCE==null){
            //noinspection deprecation
            INSTANCE = new SharedPref(PreferenceManager.getDefaultSharedPreferences(context));
        }
        return INSTANCE;
    }



    private SharedPreferences sharedPreferences;

    private SharedPref(SharedPreferences sharedPreferences) {
        //this.sharedPreferences = context.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        this.sharedPreferences=sharedPreferences;

    }

    public void setLoggedUserId(Long id){
        sharedPreferences.edit().putLong(ID_KEY,id).apply();
    }

    public Long getLoggedUserId(){
        return sharedPreferences.getLong(ID_KEY,-1);
    }

    public void getLoginUserMethod(){

    }


}
