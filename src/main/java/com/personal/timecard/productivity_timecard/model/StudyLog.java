package com.personal.timecard.productivity_timecard.model;

import lombok.Data;

@Data
public class StudyLog {

    private Integer problemsSolved;

    private Double minutesSpent;

    private String topic;

    private String description;
}