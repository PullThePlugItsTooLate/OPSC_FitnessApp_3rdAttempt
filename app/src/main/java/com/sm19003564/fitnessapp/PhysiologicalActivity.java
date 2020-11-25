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

public class PhysiologicalActivity extends AppCompatActivity {

    EditText etHeight;
    EditText etWeight;
    TextView tvHeightMeasure;
    TextView tvWeightMeasure;
    TextView tvHeightConvert;
    TextView tvWeightConvert;
    Button btnSaveValues;
    FirebaseAuth mFirebaseAuth;
    UserHealth userHealth;
    Settings settings;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userHealthRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference settingsRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physiological);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        mFirebaseAuth = FirebaseAuth.getInstance();
        tvHeightMeasure = findViewById(R.id.tvHeightMeasure);
        tvWeightMeasure = findViewById(R.id.tvWeightMeasure);
        tvHeightConvert = findViewById(R.id.tvHeightConvert);
        tvWeightConvert = findViewById(R.id.tvWeightConvert);

        btnSaveValues = findViewById(R.id.btnSaveChanges);

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
                    IntentHelper.openIntent(PhysiologicalActivity.this, HomeActivity.class);
                }
                else if (id == R.id.iProfile){
                    IntentHelper.openIntent(PhysiologicalActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.iPhysiologicalInfo){
                    IntentHelper.openIntent(PhysiologicalActivity.this, PhysiologicalActivity.class);
                }
                else if (id == R.id.iTargetGoals){
                    IntentHelper.openIntent(PhysiologicalActivity.this, TargetGoalActivity.class);
                }
                else if (id == R.id.iLogWeightChange){
                    IntentHelper.openIntent(PhysiologicalActivity.this, DailyWeightChangeActivity.class);
                }
                else if (id == R.id.iSnapMeal){
                    IntentHelper.openIntent(PhysiologicalActivity.this, SnapMealActivity.class);
                }
                else if (id == R.id.iSettings){
                    IntentHelper.openIntent(PhysiologicalActivity.this, SettingsActivity.class);
                }
                else if (id == R.id.iLogout){
                    mFirebaseAuth.getInstance().signOut();
                    IntentHelper.openIntent(PhysiologicalActivity.this, LoginActivity.class);
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
                    tvHeightMeasure.setText(" CM");
                    tvWeightMeasure.setText(" KG");
                }else{
                    if (settings.isImperial()){
                        tvHeightMeasure.setText("FT");
                        tvWeightMeasure.setText(" LB");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PhysiologicalActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(etHeight.getText())) {
                    tvHeightConvert.setVisibility(View.INVISIBLE);
                    return;
                }
                updateConversions(tvHeightConvert,
                        MeasurementConversion.convertToCM(etHeight.getText().toString()),
                        MeasurementConversion.convertToFT(etHeight.getText().toString()),
                        settings);
            }
        });

        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (TextUtils.isEmpty(etWeight.getText())) {
                    tvWeightConvert.setVisibility(View.INVISIBLE);
                    return;
                }
                updateConversions(tvWeightConvert,
                        MeasurementConversion.convertToKG(etWeight.getText().toString()),
                        MeasurementConversion.convertToLB(etWeight.getText().toString()),
                        settings);
            }
        });

        btnSaveValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String height = etHeight.getText().toString().trim();
                String weight = etWeight.getText().toString().trim();

               if (!TextUtils.isEmpty(etWeight.getText()) && !TextUtils.isEmpty(etHeight.getText())){
                    userHealth = new UserHealth(height, weight);
                    userHealthRef.child("PhysioData").push().setValue(userHealth);
                   IntentHelper.openIntent(PhysiologicalActivity.this, HomeActivity.class);
                }
                else{
                    Toast.makeText(PhysiologicalActivity.this, "Please complete all fields", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}