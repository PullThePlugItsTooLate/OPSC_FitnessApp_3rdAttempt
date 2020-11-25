package com.sm19003564.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;

import static com.sm19003564.fitnessapp.MeasurementConversion.*;

public class DailyWeightChangeActivity extends AppCompatActivity {

    Button btnSaveLog;
    Button btnDate;
    EditText etCurrentWeight;
    FirebaseAuth mFirebaseAuth;

    int mYear;
    int mMonth;
    int mDay;
    String logDate;
    TextView tvLogDate;
    TextView tvCurrentWeightMeasure;
    TextView tvCurrentWeightConvert;
    DailyWeightChange dailyWeightChange;
    Settings settings;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dailyLogRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference settingsRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weight_change);


        mFirebaseAuth = FirebaseAuth.getInstance();
        btnSaveLog = findViewById(R.id.btnSaveLog);
        btnDate = findViewById(R.id.btnDate);
        etCurrentWeight = findViewById(R.id.etCurrentWeight);
        tvLogDate = findViewById(R.id.tvLogDate);
        tvCurrentWeightMeasure = findViewById(R.id.tvCurrentWeightMeasure);
        tvCurrentWeightConvert = findViewById(R.id.tvCurrentWeightConvert);
        dailyWeightChange = new DailyWeightChange();

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
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, HomeActivity.class);
                }
                else if (id == R.id.iProfile){
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, ProfileActivity.class);
                }
                else if (id == R.id.iPhysiologicalInfo){
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, PhysiologicalActivity.class);
                }
                else if (id == R.id.iTargetGoals){
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, TargetGoalActivity.class);
                }
                else if (id == R.id.iLogWeightChange){
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, DailyWeightChangeActivity.class);
                }
                else if (id == R.id.iSnapMeal){
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, SnapMealActivity.class);
                }
                else if (id == R.id.iSettings){
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, SettingsActivity.class);
                }
                else if (id == R.id.iLogout){
                    mFirebaseAuth.getInstance().signOut();
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, LoginActivity.class);
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
                    tvCurrentWeightMeasure.setText(" KG");
                }else{
                    if (settings.isImperial()){
                        tvCurrentWeightMeasure.setText(" LB");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyWeightChangeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        etCurrentWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(etCurrentWeight.getText())) {
                    tvCurrentWeightConvert.setVisibility(View.INVISIBLE);
                    return;
                }
                updateConversions(tvCurrentWeightConvert,
                        convertToKG(etCurrentWeight.getText().toString()),
                        convertToLB(etCurrentWeight.getText().toString()),
                        settings);
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                tvLogDate.setVisibility(View.VISIBLE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DailyWeightChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        logDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        tvLogDate.setText(logDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnSaveLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentWeight = etCurrentWeight.getText().toString().trim();

                if (!TextUtils.isEmpty(etCurrentWeight.getText().toString()) && !TextUtils.isEmpty(logDate)){
                    dailyWeightChange.setCurrentWeight(currentWeight);
                    dailyWeightChange.setDate(logDate);
                    dailyLogRef.child("DailyLogData").push().setValue(dailyWeightChange);
                    IntentHelper.openIntent(DailyWeightChangeActivity.this, HomeActivity.class);
                }
                else{
                    Toast.makeText(DailyWeightChangeActivity.this, "Please complete all fields", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}