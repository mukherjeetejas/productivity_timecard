package com.personal.timecard.productivity_timecard.repository;

import com.personal.timecard.productivity_timecard.model.Timecard;
import com.personal.timecard.productivity_timecard.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository
        extends ReactiveMongoRepository<User, String> {
}
