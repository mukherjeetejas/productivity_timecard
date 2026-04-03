package com.personal.timecard.productivity_timecard.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Weight {
    private double weight;
    private LocalDate date;
}