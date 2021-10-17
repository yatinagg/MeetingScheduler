package com.example.meetingscheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AddMeeting extends AppCompatActivity implements View.OnClickListener {

    private Button buttonDate;
    private Button buttonStartTime;
    private Button buttonEndTime;
    private Button buttonSubmit;
    private EditText editText;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    public static JSONArray jsonArray = new JSONArray();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_meeting_layout);

        buttonDate = findViewById(R.id.button_date);
        buttonDate.setOnClickListener(this);
        buttonStartTime = findViewById(R.id.button_start_time);
        buttonStartTime.setOnClickListener(this);
        buttonEndTime = findViewById(R.id.button_end_time);
        buttonEndTime.setOnClickListener(this);
        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(this);
        editText = findViewById(R.id.tv_description);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        int mHour;
        int mMinute;
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
            datePickerDialog.show();
        } else if (view == buttonStartTime) {
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
        } else if (view == buttonEndTime) {
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
        } else if (view == buttonSubmit) {
            description = String.valueOf(editText.getText());
            try {
                if (!validateFields())
                    return;
                storeData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void storeData() throws JSONException {
        JSONObject student1 = new JSONObject();
        boolean flag = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject js = jsonArray.getJSONObject(i);
            if (js.has(date)) {
                JSONArray student3 = js.getJSONArray(date);
                JSONObject student2 = new JSONObject();
                student2.put("start_time", startTime);
                student2.put("end_time", endTime);
                student2.put("description", description);
                student3.put(student2);
                student1.put(date, student3);
                flag = true;
                break;
            }

        }

        System.out.println("len" + jsonArray.length());
        JSONArray st = new JSONArray();
        if (!flag) {
            JSONObject student2 = new JSONObject();
            student2.put("start_time", startTime);
            student2.put("end_time", endTime);
            student2.put("description", description);
            st.put(student2);
            student1.put(date, st);
            jsonArray.put(student1);
        }
        getData();
        SharedPrefHelper.store();
        startActivity(new Intent(this, DisplayMeetings.class));
    }

    private void getData() throws JSONException {
        if (Meeting.meetingMap == null)
            Meeting.meetingMap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject js = jsonArray.getJSONObject(i);
            Iterator<?> keys = js.keys();

            while (keys.hasNext()) {
                List<String> lst = new ArrayList<>();
                String key = (String) keys.next();
                for (int j = 0; j < js.getJSONArray(key).length(); j++) {
                    JSONObject jsonObj = (JSONObject) js.getJSONArray(key).get(j);
                    lst.add(String.valueOf(jsonObj.get("start_time")));
                    lst.add(String.valueOf(jsonObj.get("end_time")));
                    lst.add(String.valueOf(jsonObj.get("description")));
                }
                Meeting.meetingMap.put(key, lst);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validateFields() throws ParseException, JSONException {
        Log.d("hello1", "control");
        if (date == null) {
            Toast.makeText(this, "Enter Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startTime == null) {
            Toast.makeText(this, "Enter Start Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endTime == null) {
            Toast.makeText(this, "Enter End Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description == null) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String currentDate = dateFormat.format(new Date());
        Date date1 = dateFormat.parse(date);
        if (!((new Date()).before(date1)) && !(currentDate.equals(date))) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            return false;
        }

        LocalTime time = LocalTime.now();
        String t = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println(t);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        Date dateStart = simpleDateFormat.parse(startTime);
        Date currTime = simpleDateFormat.parse(t);
        if (currTime != null && currTime.after(dateStart)) {
            Toast.makeText(this, "Invalid time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Meeting.meetingMap == null)
            Meeting.meetingMap = new HashMap<>();
        getData();
        List<String> list = Meeting.meetingMap.get(date);

        Date dateStartCurr = simpleDateFormat.parse(startTime);
        Date dateEndCurr = simpleDateFormat.parse(endTime);
        if (dateEndCurr != null && dateStartCurr != null && dateStartCurr.after(dateEndCurr)) {
            Toast.makeText(this, "Invalid time interval", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (list != null) {
            for (int i = 0; i < list.size(); i += 3) {
                Date dateS = simpleDateFormat.parse(list.get(i));
                Date dateE = simpleDateFormat.parse(list.get(i + 1));
                if (dateE != null && dateStartCurr != null && dateS != null && dateEndCurr != null && !(dateStartCurr.after(dateE) || dateEndCurr.before(dateS))) {
                    Toast.makeText(this, "Slot not available", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

}
