package com.example.meetingscheduler.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingscheduler.Meeting;
import com.example.meetingscheduler.R;
import com.example.meetingscheduler.SharedPrefHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonDate;
    private Button buttonStartTime;
    private Button buttonEndTime;
    private Button buttonSubmit;
    private EditText etDescription;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    public static JSONArray jsonArray = new JSONArray();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        setupView();
    }

    /**
     * setupView to set the view
     */
    private void setupView() {
        buttonDate = findViewById(R.id.button_date);
        buttonDate.setOnClickListener(this);
        buttonStartTime = findViewById(R.id.button_start_time);
        buttonStartTime.setOnClickListener(this);
        buttonEndTime = findViewById(R.id.button_end_time);
        buttonEndTime.setOnClickListener(this);
        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(this);
        etDescription = findViewById(R.id.tv_description);
    }

    /**
     * On click listeners for buttons
     *
     * @param view the view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        int mHour;
        int mMinute;
        // on click listener for buttonDate
        if (view == buttonDate) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        buttonDate.setText(date);
                    }, mYear, mMonth, mDay);
            Calendar calendar = Calendar.getInstance();
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        }
        // on click listener for buttonStartTime
        else if (view == buttonStartTime) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (TimePicker view12, int hourOfDay, int minute) -> {
                        if (minute < 10)
                            startTime = hourOfDay + ":0" + minute;
                        else
                            startTime = hourOfDay + ":" + minute;
                        buttonStartTime.setText(startTime);
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        // on click listener for buttonEndTime
        else if (view == buttonEndTime) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (TimePicker view12, int hourOfDay, int minute) -> {
                        if (minute < 10)
                            endTime = hourOfDay + ":0" + minute;
                        else
                            endTime = hourOfDay + ":" + minute;
                        buttonEndTime.setText(endTime);
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        // on click listener for buttonSubmit
        else if (view == buttonSubmit) {
            description = String.valueOf(etDescription.getText());
            try {
                if (!validateFields())
                    return;
                storeData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
    }

    /**
     * storeDate() to store the data in the app specific storage
     *
     * @throws JSONException throws json Exception
     */
    private void storeData() throws JSONException {
        JSONObject dataMap = new JSONObject();
        boolean meetingFound = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject js = jsonArray.getJSONObject(i);
            if (js.has(date)) {
                JSONArray dataDateArray = js.getJSONArray(date);
                JSONObject dataMeeting = new JSONObject();
                dataMeeting.put(getString(R.string.st_key), startTime);
                dataMeeting.put(getString(R.string.et_key), endTime);
                dataMeeting.put(getString(R.string.desc_key), description);
                dataDateArray.put(dataMeeting);
                dataMap.put(date, dataDateArray);
                meetingFound = true;
                break;
            }

        }

        JSONArray st = new JSONArray();
        if (!meetingFound) {
            JSONObject dataMeeting = new JSONObject();
            dataMeeting.put(getString(R.string.st_key), startTime);
            dataMeeting.put(getString(R.string.et_key), endTime);
            dataMeeting.put(getString(R.string.desc_key), description);
            st.put(dataMeeting);
            dataMap.put(date, st);
            jsonArray.put(dataMap);
        }
        SharedPrefHelper.saveJsonArray(jsonArray);
    }

    /**
     * validateFields for field validation
     *
     * @return validation is true
     */
    private boolean validateFields() {
        if (date == null) {
            Toast.makeText(this, R.string.enter_date, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startTime == null) {
            Toast.makeText(this, R.string.enter_start_time, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endTime == null) {
            Toast.makeText(this, R.string.enter_end_time, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description == null) {
            Toast.makeText(this, R.string.enter_desc, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (startTime.compareTo(endTime) >= 0) {
            Toast.makeText(this, R.string.invalid_time, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Meeting.meetingMap == null)
            Meeting.meetingMap = new HashMap<>();

        List<List<String>> list = Meeting.meetingMap.get(date);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (!(startTime.compareTo(list.get(i).get(1)) > 0 || endTime.compareTo(list.get(i).get(0)) < 0)) {
                    Toast.makeText(this, R.string.slot_not_available, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

}
