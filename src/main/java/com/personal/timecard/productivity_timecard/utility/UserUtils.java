package com.personal.timecard.productivity_timecard.utility;

import com.personal.timecard.productivity_timecard.dto.DashboardResponse;
import com.personal.timecard.productivity_timecard.dto.UserRequest;
import com.personal.timecard.productivity_timecard.model.StreakData;
import com.personal.timecard.productivity_timecard.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserUtils {

    public DashboardResponse buildDashboardResponse(
            User user,
            boolean todaySubmitted
    ) {

        Map<String, StreakData> streaks =
                user.getHabitStreaks();

        if (streaks == null || streaks.isEmpty()) {

            return DashboardResponse.builder()
                    .todaySubmitted(todaySubmitted)
                    .activeStreaks(Map.of())
                    .longestStreaks(Map.of())
                    .build();
        }

        Map<String, Integer> active =
                streaks.entrySet()
                        .stream()
                        .filter(e -> e.getValue().getCurrent() != null
                                && e.getValue().getCurrent() > 0)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().getCurrent()
                        ));

        Map<String, Integer> highest =
                streaks.entrySet()
                        .stream()
                        .filter(e -> e.getValue().getHighest() != null
                                && e.getValue().getHighest() > 0)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().getHighest()
                        ));

        return DashboardResponse.builder()
                .todaySubmitted(todaySubmitted)
                .activeStreaks(active)
                .longestStreaks(highest)
                .build();
    }

    public void updateUserFields(User existingUser, UserRequest updatedUser) {

        Optional.ofNullable(updatedUser.getName())
                .ifPresent(existingUser::setName);

        Optional.ofNullable(updatedUser.getTempAuthentication())
                .ifPresent(existingUser::setTempAuthentication);

        Optional.ofNullable(updatedUser.getEmail())
                .ifPresent(existingUser::setEmail);

        Optional.ofNullable(updatedUser.getHeight())
                .ifPresent(existingUser::setHeight);

        Optional.ofNullable(updatedUser.getGender())
                .ifPresent(existingUser::setGender);
    }
}
