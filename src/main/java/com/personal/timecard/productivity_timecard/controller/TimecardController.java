package com.personal.timecard.productivity_timecard.controller;

import com.personal.timecard.productivity_timecard.dto.TimecardRequest;
import com.personal.timecard.productivity_timecard.model.Timecard;
import com.personal.timecard.productivity_timecard.repository.TimecardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/timecard")
public class TimecardController {
    @Autowired
    TimecardRepository timecardRepository;

    @GetMapping("/health")
    Mono<String> health() {
        return Mono.just("The service is up and running!");
    }

    @PostMapping("/submit")
    Mono<Timecard> submitTimecard(@RequestBody Timecard timecard) {
        System.out.println("Timecard received: " + timecard.toString());
        return timecardRepository.save(timecard);
    }
}
