package com.personal.timecard.productivity_timecard.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String name;

    private Map<String, StreakData> habitStreaks;
}
