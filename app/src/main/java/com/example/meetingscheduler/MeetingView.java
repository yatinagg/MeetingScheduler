package com.example.meetingscheduler;

public class MeetingView {

    private final Meeting meeting;

    public MeetingView(Meeting meeting) {
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }
}
