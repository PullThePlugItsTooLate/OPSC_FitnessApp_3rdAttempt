package com.sm19003564.fitnessapp;

import android.view.View;
import android.widget.TextView;

public class MeasurementConversion {
    public static String convertToCM(String input)
    {
        double ft = Double.parseDouble(input);
        double cm = Math.round((ft * 30.48) * 100.0) / 100.0;
        return cm + " CM";
    }

    public static String convertToFT(String input)
    {
        double cm = Double.parseDouble(input);
        double ft = Math.round((cm / 30.48) * 100.0) / 100.0;
        return ft + " FT";

    }

    public static String convertToLB(String input)
    {
        double kg = Double.parseDouble(input);
        double lb = Math.round((kg * 2.205) * 100.0) / 100.0;
        return lb + " LB";
    }

    public static String convertToKG(String input)
    {
        double lb = Double.parseDouble(input);
        double kg = Math.round((lb / 2.205) * 100.0) / 100.0;
        return kg + " KG";
    }

    public static void updateConversions(TextView tv, String inputM, String inputI, Settings settings){
        if (settings.isMetric()){
            tv.setText(inputI);
            tv.setVisibility(View.VISIBLE);
        }
        else {
            if (settings.isImperial()){
                tv.setText(inputM);
                tv.setVisibility(View.VISIBLE);
            }
        }
    }
}
