package com.example.meetingscheduler;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

// Shared preference helper
public class SharedPrefHelper {

    private static SharedPreferences sharedPreferences;
    private static final String SHARED_PREF = "SharedPref1";
    private static final String JSON_ARRAY = "value";

    /**
     * creating the shared preference
     *
     * @param context the context
     */
    public static void create(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
    }

    /**
     * save the Json Array to app specific storage
     */
    public static void saveJsonArray() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(AllMeetings.meetingMap);
        sharedPreferences.edit().putString(JSON_ARRAY, jsonString).apply();
    }

    /**
     * get Json Array from app specific storage
     */
    public static void getJsonArray() {
        if (sharedPreferences.getString(JSON_ARRAY, null) != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Meeting>>>() {
            }.getType();
            AllMeetings.meetingMap = gson.fromJson(sharedPreferences.getString(JSON_ARRAY, null), type);
        }
    }

}