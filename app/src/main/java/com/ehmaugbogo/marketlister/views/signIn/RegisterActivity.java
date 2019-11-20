package com.ehmaugbogo.marketlister.views.signIn;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ehmaugbogo.marketlister.views.BaseActivity;
import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.UserViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import androidx.lifecycle.ViewModelProviders;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "slike.Views.Register";

    private TextInputEditText fNameEditext, lNameEditext, emailEditText, passwordEditText;
    private ProgressBar progressBar;
    private UserViewModel viewModel;


    public static void start(Context context){
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        initViews();

        //PreferenceManager.getDefaultSharedPreferences(this)

    }

    private void initViews() {
        fNameEditext = findViewById(R.id.registration_activity_fname);
        lNameEditext = findViewById(R.id.registration_activity_lname);
        emailEditText = findViewById(R.id.registration_activity_email);
        passwordEditText = findViewById(R.id.registration_activity_password);
        TextView loginHere = findViewById(R.id.registration_activity_login_text);
        Button registerBtn = findViewById(R.id.registration_activity_button);
        progressBar =findViewById(R.id.registration_activity_progressbar);


        loginHere.setOnClickListener(this);
        registerBtn.setOnClickListener(this);


        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registration_activity_login_text: LoginActivity.start(this);
                break;
            case R.id.registration_activity_button: register();
                break;
        }
    }


    private void register(){
        showProgressbar();
        String fName = Objects.requireNonNull(fNameEditext.getText()).toString().trim();
        String lName = Objects.requireNonNull(lNameEditext.getText()).toString().trim();
        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim();


        if (!validateInputs(fName, lName, email)){
            hideProgressbar();
            return;
        }


        User newUser=new User(fName,lName,email);
        Long insertedUserId = viewModel.insertUser(newUser);
        //TODO getUserId from Firebase
        //newUser.setUid();


    }


    private boolean validateInputs(String fname, String lname, String email) {
        if(TextUtils.isEmpty(fname)){
            fNameEditext.setError("Required");
            return false;
        }

        if(TextUtils.isEmpty(lname)){
            lNameEditext.setError("Required");
            return false;
        }

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Required");
            return false;
        }

        return true;
    }


    private void showProgressbar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar(){
        progressBar.setVisibility(View.GONE);
    }
}
