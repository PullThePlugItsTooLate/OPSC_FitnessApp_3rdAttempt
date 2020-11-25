package com.sm19003564.fitnessapp;

public class UserHealth {

    private String height;
    private String weight;

    public UserHealth() {

    }

    public UserHealth(String height, String weight) {
        this.height = height;
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String toString() {
        return height + " " + weight + " ";
    }
}
