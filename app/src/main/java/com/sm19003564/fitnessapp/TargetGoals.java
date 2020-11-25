package com.sm19003564.fitnessapp;

public class TargetGoals {
    private String weight;
    private String calorieIntake;

    public TargetGoals() {

    }

    public TargetGoals(String weight, String calorieIntake) {
        this.weight = weight;
        this.calorieIntake = calorieIntake;
    }

    public String getCalorieIntake() {
        return calorieIntake;
    }

    public void setCalorieIntake(String calorieIntake) {
        this.calorieIntake = calorieIntake;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
