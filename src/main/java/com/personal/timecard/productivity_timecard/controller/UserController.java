package com.personal.timecard.productivity_timecard.controller;

import com.personal.timecard.productivity_timecard.model.User;
import com.personal.timecard.productivity_timecard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<User> createUser(@RequestBody User user) {

        return userService.createUser(user);
    }


    @PutMapping("/{userId}")
    public Mono<User> updateUser(
            @PathVariable String userId,
            @RequestBody User user
    ) {

        return userService.updateUser(userId, user);
    }


    @DeleteMapping("/{userId}")
    public Mono<Void> deleteUser(@PathVariable String userId) {

        return userService.deleteUser(userId);
    }


    @GetMapping("/{userId}")
    public Mono<User> getUser(@PathVariable String userId) {

        return userService.getUserById(userId);
    }


    @GetMapping
    public Flux<User> getAllUsers() {

        return userService.getAllUsers();
    }
}
