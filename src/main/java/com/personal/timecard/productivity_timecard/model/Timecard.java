package com.personal.timecard.productivity_timecard.model;

import com.personal.timecard.productivity_timecard.enums.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "timecard")
@Data
public class Timecard {
    @Id
    private String id;
    User user;
    Activity activity;
}
