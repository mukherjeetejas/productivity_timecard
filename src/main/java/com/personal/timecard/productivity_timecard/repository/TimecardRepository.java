package com.personal.timecard.productivity_timecard.repository;

import com.personal.timecard.productivity_timecard.model.Timecard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TimecardRepository
        extends ReactiveMongoRepository<Timecard, String> {
}
