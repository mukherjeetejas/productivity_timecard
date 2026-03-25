package com.personal.timecard.productivity_timecard.service;

import com.personal.timecard.productivity_timecard.error.UserNotFoundException;
import com.personal.timecard.productivity_timecard.model.Timecard;
import com.personal.timecard.productivity_timecard.repository.TimecardRepository;
import com.personal.timecard.productivity_timecard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class TimecardService {

    @Autowired
    TimecardRepository timecardRepository;

    @Autowired UserRepository userRepository;

    private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");

    public Mono<Timecard> addTimecard(Timecard timecard, String userId) {

        timecard.setDate(LocalDate.now(INDIA_ZONE));

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found: " + userId)))
                .flatMap(user ->
                        timecardRepository
                                .findByUserIdAndDate(userId, timecard.getDate())
                                .flatMap(existing -> {
                                    timecard.setId(existing.getId());
                                    return timecardRepository.save(timecard);
                                })
                                .switchIfEmpty(timecardRepository.save(timecard))
                );
    }
}
