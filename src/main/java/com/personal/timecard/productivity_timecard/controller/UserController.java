package com.personal.timecard.productivity_timecard.controller;

import com.personal.timecard.productivity_timecard.dto.UserRequest;
import com.personal.timecard.productivity_timecard.model.User;
import com.personal.timecard.productivity_timecard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
