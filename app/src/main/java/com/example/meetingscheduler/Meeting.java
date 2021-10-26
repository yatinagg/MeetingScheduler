package com.example.meetingscheduler;

/**
 * Meeting class
 */
public class Meeting {
    private final String startTime;
    private final String endTime;
    private final String description;

    /**
     * Constructor of meeting class
     *
     * @param startTime   start time of the meeting
     * @param endTime     end time of the meeting
     * @param description description of the meeting
     */
    public Meeting(String startTime, String endTime, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    /**
     * getter for start time
     *
     * @return start time of the meeting
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * getter for end time
     *
     * @return end time of the meeting
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * getter for description
     *
     * @return description of the meeting
     */
    public String getDescription() {
        return description;
    }

}
