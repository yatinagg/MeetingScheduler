package com.example.meetingscheduler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingscheduler.Adapter.MeetingAdapter;
import com.example.meetingscheduler.Meeting;
import com.example.meetingscheduler.R;
import com.example.meetingscheduler.SharedPrefHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DisplayMeetingsActivity extends AppCompatActivity {

    private Button buttonSchedule;
    private Button buttonPrev;
    private Button buttonNext;
    private Date date;
    private String dateString;
    private TextView tvNoAddressDisplay;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_display);
        SharedPrefHelper.create(this);

        date = new Date();
        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_pattern), Locale.US);
        dateString = dateFormat.format(date);
        setupView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * setupView to set the view
     */
    private void setupView() {
        buttonSchedule = findViewById(R.id.button_schedule);
        buttonPrev = findViewById(R.id.button_prev);
        buttonNext = findViewById(R.id.button_next);
        tvNoAddressDisplay = findViewById(R.id.tv_no_address_display);
        tvDate = findViewById(R.id.tv_date);
    }

    /**
     * setData to set the data
     */
    private void setData() throws JSONException {
        List<Meeting> meetingsList = new ArrayList<>();

        Objects.requireNonNull(getSupportActionBar()).hide();
        AddMeetingActivity.jsonArray = SharedPrefHelper.getJsonArray();
        getData();
        tvDate.setText(dateString);

        if (Meeting.meetingMap == null)
            Meeting.meetingMap = new HashMap<>();

        List<List<String>> list = Meeting.meetingMap.get(dateString);

        if (list != null) {
            tvNoAddressDisplay.setVisibility(View.INVISIBLE);
            for (int i = 0; i < list.size(); i++) {
                meetingsList.add(new Meeting(list.get(i).get(0), list.get(i).get(1), list.get(i).get(2)));
            }
        } else {
            tvNoAddressDisplay.setText(R.string.no_meeting);
            tvNoAddressDisplay.setVisibility(View.VISIBLE);
        }
        MeetingAdapter meetingAdapter = new MeetingAdapter(this, meetingsList);

        ListView meetingListView = findViewById(R.id.list_view);
        meetingListView.setAdapter(meetingAdapter);
    }

    /**
     * setupListeners for onClick listeners
     */

    private void setupListeners() {
        buttonSchedule.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddMeetingActivity.class)));
        buttonPrev.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_pattern), Locale.US);
            dateString = dateFormat.format(calendar.getTime());
            date = calendar.getTime();
            try {
                setData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        buttonNext.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_pattern), Locale.US);
            dateString = dateFormat.format(calendar.getTime());
            date = calendar.getTime();
            try {
                setData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * getData to convert jsonArray into Map
     *
     * @throws JSONException throws json Exception
     */

    private void getData() throws JSONException {
        if (Meeting.meetingMap == null)
            Meeting.meetingMap = new HashMap<>();
        if (AddMeetingActivity.jsonArray == null) {
            AddMeetingActivity.jsonArray = new JSONArray();
        }
        for (int i = 0; i < AddMeetingActivity.jsonArray.length(); i++) {
            JSONObject js = AddMeetingActivity.jsonArray.getJSONObject(i);
            Iterator<?> keys = js.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                List<List<String>> listBeforeSort = new ArrayList<>();
                for (int j = 0; j < js.getJSONArray(key).length(); j++) {
                    JSONObject jsonObj = (JSONObject) js.getJSONArray(key).get(j);
                    listBeforeSort.add(Arrays.asList(String.valueOf(jsonObj.get("start_time")), String.valueOf(jsonObj.get("end_time")), String.valueOf(jsonObj.get("description"))));
                }
                Collections.sort(listBeforeSort, (Comparator<List>) (o1, o2) -> ((String) o1.get(0)).compareTo((String) o2.get(0)));
                Meeting.meetingMap.put(key, listBeforeSort);
            }
        }
    }
}
