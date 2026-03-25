package com.personal.timecard.productivity_timecard.service;

import com.personal.timecard.productivity_timecard.enums.WorkoutType;
import com.personal.timecard.productivity_timecard.error.UserNotFoundException;
import com.personal.timecard.productivity_timecard.model.Timecard;
import com.personal.timecard.productivity_timecard.model.User;
import com.personal.timecard.productivity_timecard.repository.TimecardRepository;
import com.personal.timecard.productivity_timecard.repository.UserRepository;
import com.personal.timecard.productivity_timecard.utility.DateUtils;
import com.personal.timecard.productivity_timecard.utility.StreakUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
                        new UserNotFoundException("User not found")
                ))
                .flatMap(user ->
                        timecardRepository
                                .save(timecard)
                                .flatMap(savedCard ->
                                        updateAllStreaks(user, savedCard)
                                                .then(userRepository.save(user))
                                                .thenReturn(savedCard)
                                )
                );
    }

    private Mono<Void> updateAllStreaks(
            User user,
            Timecard todayCard
    ) {
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
                        "dsa",
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
                        "gym",
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
                        "calories",
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
}
