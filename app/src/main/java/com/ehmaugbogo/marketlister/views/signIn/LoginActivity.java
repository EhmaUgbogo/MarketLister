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
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.utils.Common;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "slike.Views.Login";
    public static final String USER_LOGIN_KEY_INTENT_EXTRA ="com.example.solarcalculator_USER_KEY";

    private TextInputLayout emailEditText, passwordEditText;
    private ProgressBar progressBar;
    //private UserViewModel viewModel;
    boolean isNowLoggedIn;

    public static void start(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initViews();

        //UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);


    }


    private void initViews() {
        emailEditText = findViewById(R.id.login_activity_email_TextInputLayout);
        passwordEditText = findViewById(R.id.login_activity_password_TextInputLayout);
        TextView registerHere = findViewById(R.id.login_activity_register_text);
        Button loginbtn = findViewById(R.id.login_activity_button);

        registerHere.setOnClickListener(this);
        loginbtn.setOnClickListener(this);


        progressBar = findViewById(R.id.login_activity_progressbar);
        //viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_activity_button:
                loginViaRoom();
                break;
            case R.id.login_activity_register_text:
                RegisterActivity.start(LoginActivity.this);
                break;
        }
    }


    private void loginViaRoom() {
        showProgressbar();
        String email = emailEditText.getEditText().getText().toString().trim();
        String password = passwordEditText.getEditText().getText().toString().trim();

        if(!Common.checkNetwork(this)){
            showToast("You do not have internet connection, Check internet or log in with email");
            hideProgressbar();
            return;
        }

        if (!validateEmailPassword(email, password)) {
            hideProgressbar();
            isNowLoggedIn =false;
            return;
        }

    }


    private boolean validateEmailPassword(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required");
            return false;
        } else if (password.length() > 15) {
            passwordEditText.setError("Password too long");
            return false;
        }
        return true;
    }


    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

}
