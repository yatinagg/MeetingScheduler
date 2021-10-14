package com.example.meetingscheduler;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

// Shared preference helper
public class SharedPrefHelper {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    // creating the shared preference
    public static void create(Context context) {
        sharedPreferences = context.getSharedPreferences("SharedPref1", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // storing the data to shared preference
    public static void store() {
        editor.putString("value", String.valueOf(AddMeeting.jsonArray));
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    // getters

    public static String getJsonArray() {
        return SharedPrefHelper.getSharedPreferences().getString("value", null);
    }

}

