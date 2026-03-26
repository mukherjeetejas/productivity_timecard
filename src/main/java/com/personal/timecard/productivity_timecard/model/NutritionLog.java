package com.personal.timecard.productivity_timecard.model;

import lombok.Data;

@Data
public class NutritionLog {

    private Integer calories;

    private Integer proteinGrams;

    private String description;

    private Boolean targetMet;
}
