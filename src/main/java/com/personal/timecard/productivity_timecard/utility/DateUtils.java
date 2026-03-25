package com.personal.timecard.productivity_timecard.utility;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class DateUtils {

    private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");

    public LocalDate today() {

        return LocalDate.now(INDIA_ZONE);
    }

    public LocalDate yesterday() {

        return today().minusDays(1);
    }
}
