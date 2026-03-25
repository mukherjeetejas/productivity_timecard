package com.personal.timecard.productivity_timecard.model;

import lombok.Data;

@Data
public class Activity {
    private String activityName;
    private Integer activityHours;
    private String activityDescription;
}