package com.personal.timecard.productivity_timecard.dto;

import com.personal.timecard.productivity_timecard.enums.Gender;
import lombok.Data;

@Data
public class UserRequest {
    private String id;
    private String tempAuthentication; // update password (temp)
    private String name;
    private String email;
    private Gender gender;
    private double height;
}