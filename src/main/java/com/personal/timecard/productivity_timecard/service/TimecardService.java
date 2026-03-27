package com.personal.timecard.productivity_timecard.service;

import com.personal.timecard.productivity_timecard.enums.WorkoutType;
import com.personal.timecard.productivity_timecard.error.TimecardAlreadyExistsException;
import com.personal.timecard.productivity_timecard.error.UserNotFoundException;
import com.personal.timecard.productivity_timecard.model.Timecard;
import com.personal.timecard.productivity_timecard.model.User;
import com.personal.timecard.productivity_timecard.repository.TimecardRepository;
import com.personal.timecard.productivity_timecard.repository.UserRepository;
import com.personal.timecard.productivity_timecard.utility.DateUtils;
import com.personal.timecard.productivity_timecard.utility.StreakUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.personal.timecard.productivity_timecard.constant.ApplicationConstants.*;

@Service
@RequiredArgsConstructor
public class TimecardService {

    private final TimecardRepository timecardRepository;

    private final UserRepository userRepository;

    private final DateUtils dateUtils;

    private final StreakUtils streakUtils;


    public Mono<Timecard> addTimecard(
            Timecard timecard,
            String userId
    ) {

        LocalDate today = dateUtils.today();

        timecard.setUserId(userId);
        timecard.setDate(today);

        return userRepository
                .findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)
                ))
                .flatMap(user ->

                        timecardRepository
                                .findByUserIdAndDate(userId, today)
                                .hasElement()

                                .flatMap(exists -> {

                                    if (exists) {
                                        return Mono.error(
                                                new TimecardAlreadyExistsException(
                                                        TIMECARD_EXISTS_MESSAGE
                                                )
                                        );
                                    }

                                    return timecardRepository
                                            .save(timecard)
                                            .flatMap(savedCard ->

                                                    updateAllStreaks(user, savedCard)
                                                            .then(userRepository.save(user))
                                                            .thenReturn(savedCard)
                                            );
                                })
                );
    }

    private Mono<Void> updateAllStreaks(
            User user,
            Timecard todayCard
    ) {

        if (user.getHabitStreaks() == null) {
            user.setHabitStreaks(new HashMap<>());
        }

        List<Mono<Void>> updates = new ArrayList<>();
        // DSA
        boolean dsaCompleted =
                todayCard.getDsa() != null &&
                        todayCard.getDsa().getProblemsSolved() != null &&
                        todayCard.getDsa().getProblemsSolved() > 0;

        updates.add(
                streakUtils.updateStreak(
                        user,
                        todayCard,
                        STUDY_LOG,
                        dsaCompleted
                )
        );

        // Gym
        boolean gymCompleted =
                todayCard.getGym() != null &&
                        todayCard.getGym().getWorkoutType() != WorkoutType.REST;

        updates.add(
                streakUtils.updateStreak(
                        user,
                        todayCard,
                        WORKOUT_LOG,
                        gymCompleted
                )
        );

        // Calories
        boolean caloriesCompleted =
                todayCard.getCalories() != null &&
                        Boolean.TRUE.equals(
                                todayCard.getCalories().getTargetMet()
                        );

        updates.add(
                streakUtils.updateStreak(
                        user,
                        todayCard,
                        NUTRITION_LOG,
                        caloriesCompleted
                )
        );

        // Dynamic habits
        if (todayCard.getHabits() != null) {

            todayCard.getHabits()
                    .forEach((habitName, habit) ->

                            updates.add(
                                    streakUtils.updateStreak(
                                            user,
                                            todayCard,
                                            habitName,
                                            Boolean.TRUE.equals(
                                                    habit.getCompleted()
                                            )
                                    )
                            )
                    );
        }
        return Mono.when(updates);
    }

    public Mono<Timecard> getTodayTimecard(String userId) {
        LocalDate today = dateUtils.today();
        return timecardRepository.findByUserIdAndDate(userId, today);
    }

    public Mono<Timecard> getTimecardByDate(String userId, LocalDate date) {
        return timecardRepository.findByUserIdAndDate(userId, date);
    }

    public Flux<Timecard> getTimecardsBetweenDates(String userId, LocalDate start, LocalDate end) {
        return timecardRepository.findByUserIdAndDateBetween(userId, start, end);
    }
}
