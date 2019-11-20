package com.ehmaugbogo.marketlister.main.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.User;
import com.ehmaugbogo.marketlister.views.ui.profile.ProfileViewModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

public class ProfileSetupBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextInputEditText nameEditext;
    private TextInputEditText emailEditext;
    private TextInputEditText phoneEditext;

    private String imageUrl;

    private static final String TAG = "ProfileSetupBottomSheet";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ProfileViewModel profileViewModel;
    private FirebaseUser authCurrentUser;
    private ProgressBar spinKitProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_setup, container);

        nameEditext = rootView.findViewById(R.id.profile_setup_name);
        emailEditext = rootView.findViewById(R.id.profile_setup_email);
        phoneEditext = rootView.findViewById(R.id.profile_setup_phoneNo);

        TextView changeImageTextview = rootView.findViewById(R.id.profile_setup_add_prifile_image_btn);
        Button summitBtn = rootView.findViewById(R.id.profile_setup_summit_btn);
        changeImageTextview.setOnClickListener(this);
        summitBtn.setOnClickListener(this);

        spinKitProgressBar = rootView.findViewById(R.id.profile_setup_spin_kit);
        Sprite wave = new Wave();
        spinKitProgressBar.setIndeterminateDrawable(wave);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        authCurrentUser = auth.getCurrentUser();
        //TODO ensure viewmodel setup is correct
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);


        emailEditext.setText(authCurrentUser.getEmail());
        if (emailVerified()) {
            emailEditext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle_green_24dp, 0);
        }
        return rootView;
    }

    private boolean emailVerified() {
        return Objects.requireNonNull(authCurrentUser).isEmailVerified();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_setup_add_prifile_image_btn:
                showToast("Add Profile Image");
                break;
            case R.id.profile_setup_summit_btn:
                setupProfile();
                break;
        }
    }

    private void setupProfile() {
        showProgressbar();
        String name = nameEditext.getText().toString().trim();
        String email = emailEditext.getText().toString().trim();
        String phoneNo = phoneEditext.getText().toString().trim();

        if (!validateInput(name, email, phoneNo)) {
            hideProgressbar();
            return;
        }

        final User user = new User(name,name,email);
        user.setPhoneNo(phoneNo);
        user.setUid(auth.getUid());
        user.setImageUrl(imageUrl);
        user.setEmail(email);
        user.setEmailVerified(emailVerified());


        db.collection("users").document(Objects.requireNonNull(auth.getUid()))
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressbar();
                        showToast("Profile Setup Successful");
                        profileViewModel.updateUser(user);
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Profile Setup Failed: " + e.getMessage());
            }
        });

    }

    private boolean validateInput(String name, String email, String phoneNo) {
        if (TextUtils.isEmpty(name)) {
            nameEditext.setError("Required Field");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditext.setError("Required Field");
            return false;
        }

        if (TextUtils.isEmpty(phoneNo)) {
            phoneEditext.setError("Required Field");
            return false;
        }

        return true;
    }


    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void showProgressbar(){
        spinKitProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar(){
        spinKitProgressBar.setVisibility(View.GONE);
    }

}
