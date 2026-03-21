package com.personal.timecard.productivity_timecard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/timecard")
public class TimecardController {
    @GetMapping("/health")
    Mono<String> health() {
        return Mono.just("The service is up and running!");
    }

    @PostMapping("/submit")
    Mono<ResponseEntity> submitTimecard() {

    }
}
