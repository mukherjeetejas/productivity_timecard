package com.personal.timecard.productivity_timecard.service;

import com.personal.timecard.productivity_timecard.error.UserNotFoundException;
import com.personal.timecard.productivity_timecard.model.User;
import com.personal.timecard.productivity_timecard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.personal.timecard.productivity_timecard.constant.ApplicationConstants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> updateUser(String userId, User updatedUser) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)
                ))
                .flatMap(existingUser -> {

                    existingUser.setName(updatedUser.getName());
                    existingUser.setHabitStreaks(updatedUser.getHabitStreaks());

                    return userRepository.save(existingUser);
                });
    }

    public Mono<Void> deleteUser(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)
                ))
                .flatMap(userRepository::delete);
    }


    public Mono<User> getUserById(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)
                ));
    }


    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
}