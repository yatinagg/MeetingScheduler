package com.example.meetingscheduler;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

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
     *
     * @param jsonArray jsonArray of data
     */
    public static void saveJsonArray(JSONArray jsonArray) {
        sharedPreferences.edit().putString(JSON_ARRAY, String.valueOf(jsonArray)).apply();
    }

    /**
     * get Json Array from app specific storage
     *
     * @return Json Array which contains data
     * @throws JSONException throws json Exception
     */
    public static JSONArray getJsonArray() throws JSONException {
        if (sharedPreferences.getString(JSON_ARRAY, null) == null)
            return new JSONArray();
        else
            return new JSONArray(sharedPreferences.getString(JSON_ARRAY, null));
    }

}