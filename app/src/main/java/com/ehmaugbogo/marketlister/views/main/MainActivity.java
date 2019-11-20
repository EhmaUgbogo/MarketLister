package com.ehmaugbogo.marketlister.views.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.views.BaseActivity;
import com.ehmaugbogo.marketlister.model.User;
import com.ehmaugbogo.marketlister.views.main.ui.future.FutureFragment;
import com.ehmaugbogo.marketlister.views.main.ui.list.BaseHolderFragment;
import com.ehmaugbogo.marketlister.views.main.ui.profile.ProfileActivity;
import com.ehmaugbogo.marketlister.views.main.ui.shared.SharedFragment;
import com.ehmaugbogo.marketlister.views.ui.profile.ProfileViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static final String USER_KEY_INTENT_EXTRA=MainActivity.class.getSimpleName()+"USER_KEY_INTENT_EXTRA";
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private User user = null;
    private ProfileViewModel profileViewModel;
    private Toolbar toolbar;


    public static void openMainActivity(Context context, User currentUser) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.USER_KEY_INTENT_EXTRA,currentUser);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MarketListr");


        initViews();

        //getCurrentUserFromFireStore(auth.getUid());

        if (savedInstanceState == null) {
            openHomeFragment();
        }


    }

    private void initViews() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        NavigationClickListeners();

    }

    private void NavigationClickListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_list:
                        fragment = new BaseHolderFragment();
                        break;
                    case R.id.nav_future:
                        fragment = new FutureFragment();
                        break;
                    case R.id.nav_shared:
                        fragment = new SharedFragment();
                        break;

                    case R.id.nav_add_new_account:
                        showToast("Add New Account Nav Clicked");
                        break;
                    case R.id.nav_logout:
                        showToast("Log Out Nav Clicked");
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                if (fragment != null) {
                    setUpFragment(fragment);
                }
                return true;
            }
        });
    }

    public void getCurrentUserFromFireStore(String uId) {
        db.collection("users").document(uId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);
                    profileViewModel.updateUser(user);
                    Log.d(TAG, "onSuccess: User Already exist: Name- " + user);
                } else {
                    openProfileSetupDialogSheet();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "getCurrentUserFromFireStore onFailure: " + e.getMessage());
            }
        });

    }

    private void openProfileSetupDialogSheet() {
        com.ehmaugbogo.marketlister.main.ui.profile.ProfileSetupBottomSheet setupBottomSheet = new com.ehmaugbogo.marketlister.main.ui.profile.ProfileSetupBottomSheet();
        setupBottomSheet.setCancelable(false);
        setupBottomSheet.show(getSupportFragmentManager(), null);
    }

    private void openHomeFragment() {
        BaseHolderFragment holderFragment = new BaseHolderFragment();
        setUpFragment(holderFragment);
        navigationView.setCheckedItem(R.id.nav_list);
    }

    private void setUpFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header_imageView:
                startActivity(new Intent(this, ProfileActivity.class));
                hideDrawer();
                break;
        }
    }

    private void signout() {
        /*auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();*/
    }


    private void setupProfile(User currentUser, View view) {
        CircleImageView navHeaderCircleImage = view.findViewById(R.id.nav_header_imageView);
        TextView navHeaderNameTv = view.findViewById(R.id.nav_header_name);
        TextView navHeaderEmailTv = view.findViewById(R.id.nav_header_email);

        navHeaderNameTv.setText(currentUser.getFirstName());
        navHeaderEmailTv.setText(currentUser.getEmail());

        Glide.with(this)
                .load(currentUser.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_person_pin_black_24dp)
                .into(navHeaderCircleImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                signout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else if (navigationView.getCheckedItem().getItemId()!=R.id.nav_list) {
            openHomeFragment();
        } else {
            super.onBackPressed();
        }
    }


    private void hideDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }


}
