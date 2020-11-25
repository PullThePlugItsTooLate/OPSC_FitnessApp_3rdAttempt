package com.sm19003564.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.sm19003564.fitnessapp.MeasurementConversion.updateConversions;

public class TargetGoalActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    Button btnSave;
    EditText etWeightGoal;
    EditText etCalorieIntake;
    TextView tvWeightGoalMeasure;
    TextView tvCalorieIntakeGoalMeasure;
    TextView tvWeightConvert;
    TargetGoals targetGoals;
    Settings settings;
    IntentHelper intAct;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference targetGoalsRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference settingsRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_goal);

        mFirebaseAuth = FirebaseAuth.getInstance();
        btnSave = findViewById(R.id.btnSave);
        etWeightGoal = findViewById(R.id.etWeightGoal);
        etCalorieIntake = findViewById(R.id.etCalorieIntakeGoal);
        tvWeightGoalMeasure = findViewById(R.id.tvWeightGoalMeasure);
        tvWeightConvert = findViewById(R.id.tvWeightConvert);
        tvCalorieIntakeGoalMeasure = findViewById(R.id.tvCalorieIntakeGoalMeasure);
        targetGoals = new TargetGoals();
        intAct = new IntentHelper();
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
                    IntentHelper.openIntent(TargetGoalActivity.this, HomeActivity.class);
                }
                else if (id == R.id.iProfile){
                    IntentHelper.openIntent(TargetGoalActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.iPhysiologicalInfo){
                    IntentHelper.openIntent(TargetGoalActivity.this, PhysiologicalActivity.class);
                }
                else if (id == R.id.iTargetGoals){
                    IntentHelper.openIntent(TargetGoalActivity.this, TargetGoalActivity.class);
                }
                else if (id == R.id.iLogWeightChange){
                    IntentHelper.openIntent(TargetGoalActivity.this, DailyWeightChangeActivity.class);
                }
                else if (id == R.id.iSnapMeal){
                    IntentHelper.openIntent(TargetGoalActivity.this, SnapMealActivity.class);
                }
                else if (id == R.id.iSettings){
                    IntentHelper.openIntent(TargetGoalActivity.this, SettingsActivity.class);
                }
                else if (id == R.id.iLogout){
                    mFirebaseAuth.getInstance().signOut();
                    IntentHelper.openIntent(TargetGoalActivity.this, LoginActivity.class);
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
                    tvWeightGoalMeasure.setText(" KG");
                }else{
                    if (settings.isImperial()){
                        tvWeightGoalMeasure.setText(" LB");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TargetGoalActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        etWeightGoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(etWeightGoal.getText())) {
                    tvWeightConvert.setVisibility(View.INVISIBLE);
                    return;
                }
                updateConversions(tvWeightConvert,
                        MeasurementConversion.convertToKG(etWeightGoal.getText().toString()),
                        MeasurementConversion.convertToLB(etWeightGoal.getText().toString()),
                        settings);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = etWeightGoal.getText().toString().trim();
                String calorieIntake = etCalorieIntake.getText().toString().trim();

                if (!TextUtils.isEmpty(etWeightGoal.getText().toString()) && !TextUtils.isEmpty(etCalorieIntake.getText().toString())){
                    targetGoals = new TargetGoals(weight, calorieIntake);

                    targetGoalsRef.child("TargetGoalsData").push().setValue(targetGoals);
                    IntentHelper.openIntent(TargetGoalActivity.this, HomeActivity.class);
                }
                else{
                    Toast.makeText(TargetGoalActivity.this, "Please complete all fields", Toast.LENGTH_SHORT);
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}