package com.example.meetingscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingscheduler.Adapter.MeetingViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DisplayMeetings extends AppCompatActivity {

    private Button buttonSchedule;
    private Button buttonPrev;
    private Button buttonNext;
    private Date date;
    private String dateString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetings_display);
        SharedPrefHelper.create(this);

        Log.d("hello", "create");
        date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateString = dateFormat.format(date);
        setupView();
        setupListeners();
    }

    private void setupView() {
        buttonSchedule = findViewById(R.id.button_schedule);
        buttonPrev = findViewById(R.id.button_prev);
        buttonNext = findViewById(R.id.button_next);
        TextView textViewDate = findViewById(R.id.tv_date);
        List<MeetingView> meetingViews = new ArrayList<>();

        textViewDate.setText(dateString);
        if (Meeting.meetingMap == null)
            Meeting.meetingMap = new HashMap<>();
        List<String> list = Meeting.meetingMap.get(dateString);
        Log.d("hello", "how" + Meeting.meetingMap);
        Log.d("hello", String.valueOf(list));


        if (list != null) {
            for (int i = 0; i < list.size(); i += 3) {
                Meeting meeting = new Meeting(list.get(i), list.get(i + 1), list.get(i + 2));
                MeetingView meetingView = new MeetingView(meeting);
                meetingViews.add(meetingView);
            }
        }
        MeetingViewAdapter meetingViewAdapter = new MeetingViewAdapter(this, meetingViews);

        ListView meetingListView = findViewById(R.id.list_view);
        meetingListView.setAdapter(meetingViewAdapter);
    }

    private void setupListeners() {
        buttonSchedule.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddMeeting.class)));
        buttonPrev.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            dateString = dateFormat.format(calendar.getTime());
            date = calendar.getTime();
            setupView();
        });
        buttonNext.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            dateString = dateFormat.format(calendar.getTime());
            date = calendar.getTime();
            setupView();
        });
    }
}
