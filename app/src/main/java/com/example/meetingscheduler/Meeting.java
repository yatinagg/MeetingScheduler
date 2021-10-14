package com.example.meetingscheduler;

import java.util.List;
import java.util.Map;

public class Meeting {
    private String startTime;
    private String endTime;
    private String description;
    public static Map<String, List<String>> meetingMap;

    public Meeting(String startTime, String endTime, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }
}
