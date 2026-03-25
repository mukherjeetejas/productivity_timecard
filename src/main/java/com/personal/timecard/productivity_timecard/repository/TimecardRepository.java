package com.personal.timecard.productivity_timecard.repository;

import com.personal.timecard.productivity_timecard.model.Timecard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface TimecardRepository
        extends ReactiveMongoRepository<Timecard, String> {
    Mono<Timecard> findByUserIdAndDate(String userId, LocalDate date);
}
