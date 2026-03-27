package com.personal.timecard.productivity_timecard.controller;

import com.personal.timecard.productivity_timecard.model.Timecard;
import com.personal.timecard.productivity_timecard.service.TimecardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
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
        log.info("Timecard received: {}", timecard);
        return timecardService.addTimecard(timecard, userId);
    }

    @GetMapping("/{userId}/today")
    public Mono<Timecard> getTodayTimecard(@PathVariable String userId) {
        log.info("Fetching today's timecard for {}", userId);
        return timecardService.getTodayTimecard(userId);
    }

    @GetMapping("/{userId}/{date}")
    public Mono<Timecard> getTimecardByDate(@PathVariable String userId, @PathVariable LocalDate date) {
        log.info("Fetching timecard for {} on {}", userId, date);
        return timecardService.getTimecardByDate(userId, date);
    }

    @GetMapping("/{userId}")
    public Flux<Timecard> getTimecardsBetweenDates(@PathVariable String userId, @RequestParam LocalDate start, @RequestParam LocalDate end) {
        log.info("Fetching timecards for {} between {} and {}", userId, start, end);
        return timecardService.getTimecardsBetweenDates(userId, start, end);
    }
}
