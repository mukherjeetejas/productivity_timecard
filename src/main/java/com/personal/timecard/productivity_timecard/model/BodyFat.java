package com.personal.timecard.productivity_timecard.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BodyFat {
    private double neckCircumference;
    private double hipCircumference;
    private double waistCircumference;
    private double calculatedFatPercentage;
    private LocalDate recordedDate;
}