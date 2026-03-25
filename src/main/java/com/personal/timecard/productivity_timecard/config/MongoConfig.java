package com.personal.timecard.productivity_timecard.config;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Override
    protected @NonNull String getDatabaseName() {
        return "productivity";
    }


    @Bean
    @Override
    public @NonNull MongoClient reactiveMongoClient() {
        return MongoClients.create(
                "mongodb+srv://mukherjeetejas:SlSdI9KZe3juspHe@cluster0.6peswns.mongodb.net/"
        );
    }
}