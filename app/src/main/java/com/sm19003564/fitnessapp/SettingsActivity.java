package com.sm19003564.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    Button btnSaveChanges;
    RadioButton rbMetric;
    RadioButton rbImperial;
    Settings settings;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference settingsRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFirebaseAuth = FirebaseAuth.getInstance();
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        rbMetric = findViewById(R.id.rbMetric);
        rbImperial = findViewById(R.id.rbImperial);
        settings = new Settings(true, false);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final NavigationView navView = findViewById(R.id.navView);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (id == R.id.iHome){
                    IntentHelper.openIntent(SettingsActivity.this, HomeActivity.class);
                }
                else if (id == R.id.iProfile){
                    IntentHelper.openIntent(SettingsActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.iPhysiologicalInfo){
                    IntentHelper.openIntent(SettingsActivity.this, PhysiologicalActivity.class);
                }
                else if (id == R.id.iTargetGoals){
                    IntentHelper.openIntent(SettingsActivity.this, TargetGoalActivity.class);
                }
                else if (id == R.id.iLogWeightChange){
                    IntentHelper.openIntent(SettingsActivity.this, DailyWeightChangeActivity.class);
                }
                else if (id == R.id.iSnapMeal){
                    IntentHelper.openIntent(SettingsActivity.this, SnapMealActivity.class);
                }
                else if (id == R.id.iSettings){
                    IntentHelper.openIntent(SettingsActivity.this, SettingsActivity.class);
                }
                else if (id == R.id.iLogout){
                    mFirebaseAuth.getInstance().signOut();
                    IntentHelper.openIntent(SettingsActivity.this, LoginActivity.class);
                }
                return true;
            }
        });

        settingsRef.child("SettingsData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataInfo: snapshot.getChildren()){
                    settings = dataInfo.getValue(Settings.class);
                }
                if (settings.isMetric()){
                    rbMetric.setChecked(true);
                }else{
                    if (settings.isImperial()){
                        rbImperial.setChecked(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbMetric.isChecked()){
                    settings = new Settings(true, true);
                }
                else {
                    if (rbImperial.isChecked()){
                        settings = new Settings(false, true);
                    }
                }

                settingsRef.child("SettingsData").push().setValue(settings);
                IntentHelper.openIntent(SettingsActivity.this, HomeActivity.class);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}