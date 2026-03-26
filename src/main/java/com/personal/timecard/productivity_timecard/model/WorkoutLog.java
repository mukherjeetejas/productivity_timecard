package com.personal.timecard.productivity_timecard.model;

import com.personal.timecard.productivity_timecard.enums.WorkoutType;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutLog {

    private WorkoutType workoutType;

    private Integer cardioMinutes;

    private String cardioExercises;

    private Boolean absDone;

    private List<Exercise> exercises;

    private String additionalNotes;
}
