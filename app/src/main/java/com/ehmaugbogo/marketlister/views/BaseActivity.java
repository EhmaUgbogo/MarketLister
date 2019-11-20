package com.ehmaugbogo.marketlister.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ehmaugbogo.marketlister.utils.SharedPref;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private SharedPref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharePref=SharedPref.getINSTANCE(this);
    }

    public SharedPref getSharePref() {
        return sharePref;
    }

    protected void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    public void hideProgressBar(ProgressBar progressBar){
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showProgressBar(ProgressBar progressBar){
        progressBar.setVisibility(View.VISIBLE);
    }


}

