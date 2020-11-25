package com.sm19003564.fitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SnapMealActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    private StorageReference storageReference;
    Button photoFab;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    ImageView img_cameraImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_meal);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final String [] permissions = {Manifest.permission.CAMERA};
        photoFab = findViewById(R.id.photoFab);
        img_cameraImage = findViewById(R.id.img_cameraImage);
        storageReference = FirebaseStorage.getInstance().getReference();

        if (!PermissionHelper.hasPermissions(SnapMealActivity.this, permissions)){
            ActivityCompat.requestPermissions(SnapMealActivity.this, permissions, 0);
        }
        photoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 0);
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final NavigationView navView = (NavigationView)findViewById(R.id.navView);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (id == R.id.iHome){
                    IntentHelper.openIntent(SnapMealActivity.this, HomeActivity.class);
                }
                else if (id == R.id.iProfile){
                    IntentHelper.openIntent(SnapMealActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.iPhysiologicalInfo){
                    IntentHelper.openIntent(SnapMealActivity.this, PhysiologicalActivity.class);
                }
                else if (id == R.id.iTargetGoals){
                    IntentHelper.openIntent(SnapMealActivity.this, TargetGoalActivity.class);
                }
                else if (id == R.id.iLogWeightChange){
                    IntentHelper.openIntent(SnapMealActivity.this, DailyWeightChangeActivity.class);
                }
                else if (id == R.id.iSnapMeal){
                    IntentHelper.openIntent(SnapMealActivity.this, SnapMealActivity.class);
                }
                else if (id == R.id.iSettings){
                    IntentHelper.openIntent(SnapMealActivity.this, SettingsActivity.class);
                }
                else if (id == R.id.iLogout){
                    mFirebaseAuth.getInstance().signOut();
                    IntentHelper.openIntent(SnapMealActivity.this, LoginActivity.class);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        img_cameraImage.setImageBitmap(bitmap);
    }
}