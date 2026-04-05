package com.personal.timecard.productivity_timecard.model;

import com.personal.timecard.productivity_timecard.enums.Gender;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String tempAuthentication; // temp authentication for frontend. will be replaced

    private String name;

    private String email;

    private Gender gender;

    private double height;

    private Map<String, StreakData> habitStreaks;

    private List<Weight> userWeights;

    private List<BodyFat> bodyFats;
}
