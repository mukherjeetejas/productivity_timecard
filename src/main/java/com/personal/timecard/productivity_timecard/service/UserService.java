package com.personal.timecard.productivity_timecard.service;

import com.personal.timecard.productivity_timecard.dto.DashboardResponse;
import com.personal.timecard.productivity_timecard.dto.BodyFatRequest;
import com.personal.timecard.productivity_timecard.dto.TempAuthentication;
import com.personal.timecard.productivity_timecard.dto.UserRequest;
import com.personal.timecard.productivity_timecard.error.AuthenticationException;
import com.personal.timecard.productivity_timecard.error.UserAlreadyExistsException;
import com.personal.timecard.productivity_timecard.error.UserNotFoundException;
import com.personal.timecard.productivity_timecard.model.*;
import com.personal.timecard.productivity_timecard.repository.TimecardRepository;
import com.personal.timecard.productivity_timecard.repository.UserRepository;
import com.personal.timecard.productivity_timecard.utility.DateUtils;
import com.personal.timecard.productivity_timecard.utility.BodyFatUtils;
import com.personal.timecard.productivity_timecard.utility.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.personal.timecard.productivity_timecard.constant.ApplicationConstants.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final TimecardRepository timecardRepository;
    private final DateUtils dateUtils;
    private final BodyFatUtils bodyFatUtils;

    public Mono<User> createUser(UserRequest user) {
        return userRepository.existsById(user.getId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new UserAlreadyExistsException(
                                "User already exists with id: " + user.getId()));
                    }
                    User newUser = createUserFromUserRequest(user);
                    return userRepository.save(newUser);
                });
    }

    public Mono<User> updateUser(String userId, UserRequest updatedUser) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)
                ))
                .flatMap(existingUser -> {
                    userUtils.updateUserFields(existingUser, updatedUser);
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

    private User createUserFromUserRequest(UserRequest request) {
        User user = new User();
        user.setId(request.getId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setHeight(request.getHeight());
        user.setGender(request.getGender());
        user.setTempAuthentication(request.getTempAuthentication());
        return user;
    }

    public Mono<Map<String, StreakData>> getUserStreaks(String userId) {
        return userRepository
                .findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(
                                USER_NOT_FOUND_MESSAGE + userId
                        )
                ))
                .map(user -> {
                    if (user.getHabitStreaks() == null) {
                        return Map.of();
                    }
                    return user.getHabitStreaks();
                });
    }

    public Mono<DashboardResponse> getDashboard(String userId) {
        LocalDate today = dateUtils.today();
        Mono<Boolean> todaySubmittedMono =
                timecardRepository
                        .findByUserIdAndDate(userId, today)
                        .hasElement();

        Mono<User> userMono =
                userRepository
                        .findById(userId)
                        .switchIfEmpty(Mono.error(
                                new UserNotFoundException(
                                        USER_NOT_FOUND_MESSAGE + userId
                                )
                        ));

        return Mono.zip(todaySubmittedMono, userMono)
                .map(tuple ->
                        userUtils.buildDashboardResponse(
                                tuple.getT2(),
                                tuple.getT1()
                        )
                );
    }

    public Mono<Double> calculateBodyFat(String userId, BodyFatRequest request) {

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(
                                USER_NOT_FOUND_MESSAGE + userId
                        )
                ))
                .flatMap(user -> {

                    double fatPercentage =
                            bodyFatUtils.calculateFatPercentage(
                                    user.getGender(),
                                    user.getHeight(),
                                    request
                            );

                    BodyFat fat = new BodyFat();

                    fat.setNeckCircumference(request.getNeckCircumference());

                    fat.setWaistCircumference(request.getWaistCircumference());

                    fat.setHipCircumference(request.getHipCircumference());

                    fat.setCalculatedFatPercentage(fatPercentage);

                    fat.setRecordedDate(dateUtils.today());

                    if (user.getBodyFats() == null) {
                        user.setBodyFats(new ArrayList<>());
                    }

                    user.getBodyFats().add(fat);

                    return userRepository
                            .save(user)
                            .thenReturn(fatPercentage);
                });
    }

    public Flux<BodyFat> getBodyFatHistory(String userId) {

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(
                                USER_NOT_FOUND_MESSAGE + userId
                        )
                ))
                .flatMapMany(user -> {
                    if (user.getBodyFats() == null) {
                        return Flux.empty();
                    }
                    return Flux.fromIterable(
                            user.getBodyFats()
                    );
                });
    }

    public Flux<Weight> getUserWeightHistory(String userId) {

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(
                                USER_NOT_FOUND_MESSAGE + userId
                        )
                ))
                .flatMapMany(user -> {

                    if (user.getUserWeights() == null) {
                        return Flux.empty();
                    }

                    return Flux.fromIterable(
                            user.getUserWeights()
                    );
                });
    }

    public Mono<Weight> addWeight(String userId, WeightRequest request) {

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(
                                USER_NOT_FOUND_MESSAGE + userId
                        )
                ))
                .flatMap(user -> {

                    Weight weight = new Weight();

                    weight.setWeight(request.getWeight());
                    weight.setDate(dateUtils.today());

                    if (user.getUserWeights() == null) {
                        user.setUserWeights(new ArrayList<>());
                    }

                    user.getUserWeights().add(weight);

                    return userRepository
                            .save(user)
                            .thenReturn(weight);
                });
    }

    public Mono<User> tempAuthenticate(String userId, TempAuthentication tempAuthentication) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)))
                .flatMap(user -> {
                    if(!user.getTempAuthentication().equals(tempAuthentication.getTempAuthentication())) {
                        return Mono.error(new AuthenticationException(AUTHENTICATION_EXCEPTION_MESSAGE));
                    }
                    return Mono.just(user);
                });
    }

    public Mono<List<String>> getHabits(String userId) {

        return userRepository.findById(userId)
                .switchIfEmpty(
                        Mono.error(
                                new UserNotFoundException(
                                        USER_NOT_FOUND_MESSAGE + userId
                                )
                        )
                )
                .map(user -> {

                    if (user.getHabitStreaks() == null) {
                        return Collections.emptyList();
                    }

                    return user.getHabitStreaks()
                            .keySet()
                            .stream()
                            .filter(habit ->
                                    !habit.equals(STUDY_LOG)
                                            && !habit.equals(WORKOUT_LOG)
                                            && !habit.equals(NUTRITION_LOG)
                            )
                            .toList();
                });
    }
}