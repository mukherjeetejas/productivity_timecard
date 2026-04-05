package com.personal.timecard.productivity_timecard.controller;

import com.personal.timecard.productivity_timecard.dto.DashboardResponse;
import com.personal.timecard.productivity_timecard.dto.BodyFatRequest;
import com.personal.timecard.productivity_timecard.dto.TempAuthentication;
import com.personal.timecard.productivity_timecard.dto.UserRequest;
import com.personal.timecard.productivity_timecard.model.*;
import com.personal.timecard.productivity_timecard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/health")
    Mono<String> health() {
        return Mono.just("The user service is up and running!");
    }

    @PostMapping
    public Mono<User> createUser(@RequestBody UserRequest user) {
        log.info("User create request received: {}", user);
        return userService.createUser(user);
    }


    @PutMapping("/{userId}")
    public Mono<User> updateUser(
            @PathVariable String userId,
            @RequestBody UserRequest user
    ) {
        log.info("User update request received: {}", user);
        return userService.updateUser(userId, user);
    }


    @DeleteMapping("/{userId}")
    public Mono<Void> deleteUser(@PathVariable String userId) {
        log.info("User deletion request received: {}", userId);
        return userService.deleteUser(userId);
    }


    @GetMapping("/{userId}")
    public Mono<User> getUser(@PathVariable String userId) {
        log.info("User request received: {}", userId);
        return userService.getUserById(userId);
    }


    @GetMapping
    public Flux<User> getAllUsers() {
        log.info("Get all users called");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}/streaks")
    public Mono<Map<String, StreakData>> getUserStreaks(@PathVariable String userId) {
        log.info("Fetching streaks for user {}", userId);
        return userService.getUserStreaks(userId);
    }

    @GetMapping("/{userId}/dashboard")
    public Mono<DashboardResponse> getDashboard(@PathVariable String userId) {
        log.info("Fetching dashboard for user {}", userId);
        return userService.getDashboard(userId);
    }

    @PostMapping("/{userId}/bodyFat")
    public Mono<Double> calculateBodyFat(@PathVariable String userId, @RequestBody BodyFatRequest request) {
        log.info("Fat percentage calculation request received for user {}", userId);
        return userService.calculateBodyFat(userId, request);
    }


    @GetMapping("/{userId}/bodyFat")
    public Flux<BodyFat> getBodyFatHistory(@PathVariable String userId) {
        log.info("Fetching fat percentage history for user {}", userId);
        return userService.getBodyFatHistory(userId);
    }


    @GetMapping("/{userId}/weightHistory")
    public Flux<Weight> getUserWeightHistory(@PathVariable String userId) {
        log.info("Fetching weight history for user {}", userId);
        return userService.getUserWeightHistory(userId);
    }

    @PostMapping("/{userId}/weight")
    public Mono<Weight> addWeight(@PathVariable String userId, @RequestBody WeightRequest request) {
        log.info("Weight entry request received for user {}", userId);
        return userService.addWeight(userId, request);
    }

    @GetMapping("/{userId}/authenticate")
    public Mono<User> addWeight(@PathVariable String userId, @RequestBody TempAuthentication request) {
        log.info("Authenticating user with userId {}", userId);
        return userService.tempAuthenticate(userId, request);
    }
}
