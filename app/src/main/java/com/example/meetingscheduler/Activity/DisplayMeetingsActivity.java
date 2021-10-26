package com.example.meetingscheduler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingscheduler.Adapter.MeetingAdapter;
import com.example.meetingscheduler.AllMeetings;
import com.example.meetingscheduler.Meeting;
import com.example.meetingscheduler.R;
import com.example.meetingscheduler.SharedPrefHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
        setData();
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
    private void setData() {
        List<Meeting> meetingsList = new ArrayList<>();

        Objects.requireNonNull(getSupportActionBar()).hide();
        SharedPrefHelper.getJsonArray();
        tvDate.setText(dateString);

        if (AllMeetings.meetingMap == null)
            AllMeetings.meetingMap = new HashMap<>();

        List<Meeting> list = AllMeetings.meetingMap.get(dateString);

        if (list != null) {
            tvNoAddressDisplay.setVisibility(View.INVISIBLE);
            for (int i = 0; i < list.size(); i++) {
                meetingsList.add(new Meeting(list.get(i).getStartTime(), list.get(i).getEndTime(), list.get(i).getDescription()));
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
            setData();
        });
        buttonNext.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_pattern), Locale.US);
            dateString = dateFormat.format(calendar.getTime());
            date = calendar.getTime();
            setData();
        });
    }

}
