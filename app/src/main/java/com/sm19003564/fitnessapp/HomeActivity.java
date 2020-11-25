package com.sm19003564.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    TextView tvHeight;
    TextView tvWeight;
    TextView tvWeightGoal;
    String wMeasure;
    ListView lvDailyLog;
    List<String> logList;
    ArrayAdapter<String> logAdapter;
    TextView tvCalorieIntakeGoal;
    UserHealth userHealth;
    TargetGoals targetGoals;
    Settings settings;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userHealthRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference targetGoalsRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference settingsRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference dailyLogRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvWeightGoal = findViewById(R.id.tvWeightGoal);
        tvCalorieIntakeGoal = findViewById(R.id.tvCalorieIntakeGoal);
        mFirebaseAuth = FirebaseAuth.getInstance();
        wMeasure = "KG";

        logList = new ArrayList<>(0);
        lvDailyLog = findViewById(R.id.lvDailyLog);

        userHealth = new UserHealth("0", "0");
        targetGoals = new TargetGoals("0", "0");
        settings = new Settings(true, false);
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
                    IntentHelper.openIntent(HomeActivity.this, HomeActivity.class);
                }
                else if (id == R.id.iProfile){
                    IntentHelper.openIntent(HomeActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.iPhysiologicalInfo){
                    IntentHelper.openIntent(HomeActivity.this, PhysiologicalActivity.class);
                }
                else if (id == R.id.iTargetGoals){
                    IntentHelper.openIntent(HomeActivity.this, TargetGoalActivity.class);
                }
                else if (id == R.id.iLogWeightChange){
                    IntentHelper.openIntent(HomeActivity.this, DailyWeightChangeActivity.class);
                }
                else if (id == R.id.iSnapMeal){
                    IntentHelper.openIntent(HomeActivity.this, SnapMealActivity.class);
                }
                else if (id == R.id.iSettings){
                    IntentHelper.openIntent(HomeActivity.this, SettingsActivity.class);
                }
                else if (id == R.id.iLogout){
                    mFirebaseAuth.getInstance().signOut();
                    IntentHelper.openIntent(HomeActivity.this, LoginActivity.class);
                }
                return true;
            }
        });

        userHealthRef.child("PhysioData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataInfo: snapshot.getChildren()){
                    userHealth = dataInfo.getValue(UserHealth.class);
                }
                tvHeight.setText("Height: " + userHealth.getHeight());
                tvWeight.setText("Weight: " + userHealth.getWeight());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        targetGoalsRef.child("TargetGoalsData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataInfo: snapshot.getChildren()){
                    targetGoals = dataInfo.getValue(TargetGoals.class);
                }
                tvWeightGoal.setText("Weight Goal " + targetGoals.getWeight());
                tvCalorieIntakeGoal.setText("Calorie Intake Goal: " + targetGoals.getCalorieIntake() + " CAL");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        settingsRef.child("SettingsData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataInfo: snapshot.getChildren()){
                    settings = dataInfo.getValue(Settings.class);
                }
                if (settings.isMetric()){
                    tvHeight.setText(tvHeight.getText().toString() + " CM");
                    tvWeight.setText(tvWeight.getText().toString() + " KG");
                    tvWeightGoal.setText(tvWeightGoal.getText().toString() + " KG");
                    wMeasure = "KG";
                }else{
                    if (settings.isImperial()){
                        tvHeight.setText(tvHeight.getText().toString() + " FT");
                        tvWeight.setText(tvWeight.getText().toString() + " LB");
                        tvWeightGoal.setText(tvWeightGoal.getText().toString() + " LB");
                        wMeasure = "LB";
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dailyLogRef.child("DailyLogData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot pulledLog : snapshot.getChildren()){
                    DailyWeightChange log = pulledLog.getValue(DailyWeightChange.class);
                    logList.add(log.toString(wMeasure));
                }

                logAdapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.support_simple_spinner_dropdown_item, logList);
                lvDailyLog.setAdapter(logAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}