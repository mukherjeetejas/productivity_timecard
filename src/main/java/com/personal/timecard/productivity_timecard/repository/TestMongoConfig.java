package com.personal.timecard.productivity_timecard.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
class TestMongoConfig {

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create(
                "mongodb+srv://mukherjeetejas:SlSdI9KZe3juspHe@cluster0.6peswns.mongodb.net/productivity"
        );
    }
}