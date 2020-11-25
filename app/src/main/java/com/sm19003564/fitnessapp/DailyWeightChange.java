package com.sm19003564.fitnessapp;

public class DailyWeightChange {
    private String currentWeight;
    private String date;

    public DailyWeightChange() {

    }

    public DailyWeightChange(String currentWeight, String date) {
        this.currentWeight = currentWeight;
        this.date = date;
    }

    public String toString(String wMeasure) {
        return "Weight: " + currentWeight + " " + wMeasure + "\t\t\tDate: " + date;
    }

    public String getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
