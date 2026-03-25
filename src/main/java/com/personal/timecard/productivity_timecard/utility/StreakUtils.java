package com.personal.timecard.productivity_timecard.utility;

import com.personal.timecard.productivity_timecard.enums.WorkoutType;
import com.personal.timecard.productivity_timecard.model.SimpleActivity;
import com.personal.timecard.productivity_timecard.model.StreakData;
import com.personal.timecard.productivity_timecard.model.Timecard;
import com.personal.timecard.productivity_timecard.model.User;
import com.personal.timecard.productivity_timecard.repository.TimecardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StreakUtils {

    private final TimecardRepository timecardRepository;


    public Mono<Void> updateStreak(
            User user,
            Timecard todayCard,
            String habitName,
            boolean completedToday
    ) {

        Map<String, StreakData> streaks =
                user.getHabitStreaks() == null
                        ? new HashMap<>()
                        : user.getHabitStreaks();


        StreakData streakData =
                streaks.getOrDefault(habitName, new StreakData());

        if (streakData.getCurrent() == null)
            streakData.setCurrent(0);

        if (streakData.getHighest() == null)
            streakData.setHighest(0);


        // ❌ not completed today → reset streak
        if (!completedToday) {
            streakData.setCurrent(0);
            streaks.put(habitName, streakData);
            user.setHabitStreaks(streaks);
            return Mono.empty();
        }


        LocalDate yesterday =
                todayCard.getDate().minusDays(1);


        return timecardRepository
                .findByUserIdAndDate(user.getId(), yesterday)

                .flatMap(yesterdayCard -> {

                    boolean completedYesterday =
                            checkCompletion(yesterdayCard, habitName);


                    if (completedYesterday) {

                        int previous =
                                streakData.getCurrent() == null
                                        ? 0
                                        : streakData.getCurrent();

                        streakData.setCurrent(previous + 1);

                    } else {

                        streakData.setCurrent(1);
                    }

                    updateHighest(streakData);

                    streaks.put(habitName, streakData);

                    user.setHabitStreaks(streaks);

                    return Mono.empty();
                })


                // no yesterday card exists
                .switchIfEmpty(
                        Mono.defer(() -> {

                            streakData.setCurrent(1);

                            updateHighest(streakData);

                            streaks.put(habitName, streakData);

                            user.setHabitStreaks(streaks);

                            return Mono.empty();
                        })
                ).then();
    }


    private boolean checkCompletion(
            Timecard card,
            String habitName
    ) {

        switch (habitName) {

            case "dsa":

                return card.getDsa() != null
                        && card.getDsa().getProblemsSolved() != null
                        && card.getDsa().getProblemsSolved() > 0;


            case "gym":

                return card.getGym() != null
                        && card.getGym().getWorkoutType() != WorkoutType.REST;


            case "calories":

                return card.getCalories() != null
                        && Boolean.TRUE.equals(
                        card.getCalories().getTargetMet()
                );


            default:

                if (card.getHabits() == null)
                    return false;


                SimpleActivity habit =
                        card.getHabits().get(habitName);


                return habit != null
                        && Boolean.TRUE.equals(
                        habit.getCompleted()
                );
        }
    }


    private void updateHighest(StreakData data) {

        if (data.getCurrent() > data.getHighest()) {

            data.setHighest(data.getCurrent());
        }
    }
}