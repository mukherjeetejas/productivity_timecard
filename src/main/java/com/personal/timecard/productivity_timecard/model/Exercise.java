package com.personal.timecard.productivity_timecard.model;

import lombok.Data;

import java.util.List;

@Data
public class Exercise {

    private String name;

    private List<SetEntry> sets;
}
