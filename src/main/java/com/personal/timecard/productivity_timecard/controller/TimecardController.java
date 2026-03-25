package com.personal.timecard.productivity_timecard.controller;

import com.personal.timecard.productivity_timecard.model.Timecard;
import com.personal.timecard.productivity_timecard.service.TimecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.sql.Time;

@RestController
@RequestMapping("/timecard")
public class TimecardController {

    @Autowired
    TimecardService timecardService;

    @GetMapping("/health")
    Mono<String> health() {
        return Mono.just("The timecard service is up and running!");
    }

    @PostMapping("/submit/{userId}")
    Mono<Timecard> submitTimecard(@PathVariable String userId, @RequestBody Timecard timecard) {
        timecard.setUserId(userId);
        System.out.println("Timecard received: " + timecard);
        return timecardService.addTimecard(timecard, userId);
    }
}
