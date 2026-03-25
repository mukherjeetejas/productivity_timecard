package com.personal.timecard.productivity_timecard.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@Document(collection = "timecard")
@Data
public class Timecard {
    @Id
    private String id;

    private String userId;

    private LocalDate date;

    private DsaActivity dsa;

    private GymActivity gym;

    private CalorieActivity calories;

    private Map<String, SimpleActivity> habits;

    private String notes;
}
